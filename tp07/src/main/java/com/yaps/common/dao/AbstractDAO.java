package com.yaps.common.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import com.yaps.common.dao.exception.DuplicateKeyException;
import com.yaps.common.dao.exception.ObjectNotFoundException;
import com.yaps.common.dao.exception.YapsDataException;
import com.yaps.common.model.CheckException;
import com.yaps.common.model.DomainObject;

/**
 * Une base pour implémenter le pattern Data Access Object.
 * Dans cette version, les identifiants sont supposés être systématiquement des
 * Strings.
 */
public abstract class AbstractDAO<T extends DomainObject> implements DAO<T> {

    private Logger logger = LoggerFactory.getLogger(AbstractDAO.class);

    protected JdbcTemplate jdbcTemplate;

    private final String tableName;
    private final String[] fieldsNames;

    protected AbstractDAO(JdbcTemplate jdbcTemplate, String tableName, String[] fieldsNames) {
        this.jdbcTemplate = jdbcTemplate;
        this.tableName = tableName;
        this.fieldsNames = fieldsNames.clone();
    }

    /**
     * Détruit un objet dont on passe l'identifiant.
     * 
     * @param obj
     */
    public void remove(String id) throws ObjectNotFoundException {
        Optional<T> obj = findById(id);
        if (obj.isEmpty())
            throw new ObjectNotFoundException();
        String sql = "delete from " + getTableName() + " where id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public void removeAll() {
        jdbcTemplate.update("delete from " + getTableName());
    }

    @Override
    public void save(T entity) throws DuplicateKeyException, CheckException {
        if (findById(entity.getId()).isEmpty()) {

            entity.checkData();

            // Build the "insert into" sql request...
            // build the "?,?,?..." sequence used to insert arguments.
            int numberOfArgs = 1 + getFieldsNames().length;
            String[] argsCommas = new String[numberOfArgs];
            Arrays.fill(argsCommas, "?");

            String sql = "insert into " + getTableName() + " (id, "
                    + String.join(", ", getFieldsNames())
                    + ") values (" + String.join(", ", argsCommas) + ")";

            jdbcTemplate.update(sql, getIdFollowedbyFieldValues(entity));
        } else {
            throw new DuplicateKeyException();
        }
    }

    @Override
    public void update(T entity) throws ObjectNotFoundException, CheckException {
        entity.checkData();
        if (findById(entity.getId()).isEmpty()) {
            throw new ObjectNotFoundException();
        }
        // Build the sequence for the field setting :
        String fieldsSets = String.join(", ",
                Arrays.stream(getFieldsNames()).map(s -> s + " = ?").toList());

        String sql = "update " + getTableName()
                + " set " + fieldsSets
                + " where id = ?";
        jdbcTemplate.update(sql, getFieldValuesFollowedById(entity));
    }

    @Override
    public Optional<T> findById(String id) {
        if (id == null)
            throw new NullPointerException("id should not be null");
        String sql = "select * from " + getTableName() + " where id = ?";
        List<T> res = jdbcTemplate.query(sql, (rs, n) -> extractEntity(rs), id);
        if (res.size() == 1) {
            logger.info("found {} {}", getTableName(), id);
            return Optional.of(res.get(0));
        } else if (res.isEmpty()) {
            logger.info("{} not found {}", getTableName(), id);
            return Optional.empty();        
        } else {
            throw new YapsDataException("Unexpected result, got multiple values for primary key %s".formatted(id));
        }
    }

    @Override
    public List<T> findAll() {
        String sql = "select * from " + getTableName();
        List<T> res = jdbcTemplate.query(
                sql, (rs, n) -> extractEntity(rs));
        logger.info("found {} {}",res.size(), getTableName());
        return res;
    }

    /**
     * Le nom de la table (utile pour écrire le code SQL de manière générique)
     * 
     * @return
     */
    protected String getTableName() {
        return tableName;
    }

    /**
     * Un tableau avec le nom des champs.
     * 
     * @return
     */
    protected String[] getFieldsNames() {
        return fieldsNames;
    }

    /**
     * Returns an array with all fields values relevant for save and update, except
     * the id.
     * Note that for linked objects, the corresponding value should be the foreign
     * key.
     * 
     * The field order should be the same as in getFieldsNames().
     * 
     * @param entity
     * @return
     */
    protected abstract Object[] getFieldValues(T entity);

    /**
     * Returns an array with field values, plus id.
     * 
     * @param entity
     * @return
     */
    private Object[] getFieldValuesFollowedById(T entity) {
        Object[] fields = getFieldValues(entity);
        Object[] res = Arrays.copyOf(fields, fields.length + 1);
        res[res.length - 1] = entity.getId();
        return res;
    }

    private Object[] getIdFollowedbyFieldValues(T entity) {
        Object[] fields = getFieldValues(entity);
        Object[] result = new Object[1 + fields.length];
        result[0] = entity.getId();
        System.arraycopy(fields, 0, result, 1, fields.length);
        return result;
    }

    /**
     * Define this method to extract an entity from a resultset line.
     * 
     * @param rs
     * @return
     */
    protected abstract T extractEntity(ResultSet rs);


    /**
     * Extract a string value for a field, ensuring it is not null.
     * if the original field is null, an empty String is returned.
     * @param resultSet
     * @param fieldName
     * @return a non-null string
     * @throws SQLException
     */
    protected static String extractString(ResultSet resultSet, String fieldName) throws SQLException {
        String result = resultSet.getString(fieldName);
        if (result == null)
            return "";
        else
            return result;
    }
}
