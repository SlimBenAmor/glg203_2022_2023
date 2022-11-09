package com.yaps.utils.dao;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

import com.yaps.utils.dao.exception.DataAccessException;
import com.yaps.utils.dao.exception.DuplicateKeyException;
import com.yaps.utils.dao.exception.ObjectNotFoundException;
import com.yaps.utils.model.CheckException;
import com.yaps.utils.model.DomainObject;

/**
 * Une base pour implémenter le pattern Data Access Object.
 * Dans cette version, les identifiants sont supposés être systématiquement des
 * Strings.
 */
public abstract class AbstractDAO<T extends DomainObject> {

    private Connection connection;
    private final String tableName;
    private final String [] fieldsNames;

    protected AbstractDAO(Connection connection, String tableName, String [] fieldsNames) {
        this.connection = connection;
        this.tableName = tableName;
        this.fieldsNames = fieldsNames.clone();
    }

    protected Connection getConnection() {
        return connection;
    }

    /**
     * Sauvegarde un objet en base.
     * 
     * @param obj
     */
    public void save(T obj) throws DuplicateKeyException, CheckException {
        obj.checkData();
        if (findById(obj.getId()).isEmpty()) {
            String sql = "insert into " + getTableName()
                    + " ( id, " + String.join(", ", getFieldsNames()) + ")"
                    + " values (?"+", ?".repeat(getFieldsNames().length)+")";
            try (PreparedStatement pst = getConnection().prepareStatement(sql)) {
                int fieldsOrder[] = { 1, 2, 3, 4 };
                fillPreparedStatement(pst,obj,fieldsOrder);
                pst.executeUpdate();
            } catch (SQLException e) {
                throw new DataAccessException(e.getMessage(), e);
            }
        } else {
            throw new DuplicateKeyException();
        }
    }

    /**
     * Met à jour un objet déjà connu.
     * 
     * @param obj
     */
    public void update(T obj) throws ObjectNotFoundException, CheckException {
        obj.checkData();
        if (findById(obj.getId()).isEmpty()) {
            throw new ObjectNotFoundException();
        }
        String sql = "update " + getTableName() + " set "+String.join(" = ?,  ", getFieldsNames())+" = ? where id = ?";
        try (PreparedStatement pst = getConnection().prepareStatement(sql)) {            
            int fieldsOrder[] = { 4, 1, 2, 3 };
            fillPreparedStatement(pst,obj,fieldsOrder);
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
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
        String sql = "delete from "+ getTableName() +" where id = ?";
        try (PreparedStatement pst = getConnection().prepareStatement(sql)) {
            pst.setString(1, id);
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(sql, e);
        }
    }

    /**
     * Cherche un objet par identifiant.
     * 
     * @param obj
     */
    public Optional<T> findById(String id) {
        if (id == null)
            throw new NullPointerException("id should not be null");
        String sql = "select * from " + getTableName() + " where id = ?";
        try (PreparedStatement pst = getConnection().prepareStatement(sql)) {
            pst.setString(1, id);
            try (ResultSet resultSet = pst.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(extractData(resultSet));
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage(), e);
        } catch (ObjectNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Renvoie tous les objets.
     * 
     * @param obj
     */
    public List<T> findAll(){
        String sql = "select * from " + getTableName();
        try (
                Statement st = getConnection().createStatement();
                ResultSet resultSet = st.executeQuery(sql);) {
            List<T> result = new ArrayList<>();
            while (resultSet.next()) {
                result.add(extractData(resultSet));
            }
            return result;
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage(), e);
        } catch (ObjectNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    
    /**
     * Le nom de la table (utile pour écrire le code SQL de manière générique)
     * @return
     */
    protected String getTableName() {
        return tableName;
    }

    /**
     * Un tableau avec le nom des champs.
     * @return
     */
    protected String[] getFieldsNames() {
        return fieldsNames;
    }

    /**
     * Extract Dara from the result of SQL query (ResultSet)
     * and construct a T object
     * 
     * @param res
     * @return
     */
    protected abstract T extractData(ResultSet res) throws SQLException,ObjectNotFoundException;
    
    /**
     * fill prepared statement with object fields
     * in the given order
     * 
     * @param pst
     * @param obj
     * @param fieldsOrder
     * 
     */
    protected abstract void fillPreparedStatement(PreparedStatement pst, T obj, int[] fieldsOrder) throws SQLException;
    
}
