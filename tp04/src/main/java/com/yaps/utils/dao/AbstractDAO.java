package com.yaps.utils.dao;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.IntStream;
import java.lang.reflect.Field;
import org.apache.commons.lang3.ArrayUtils;

import com.yaps.utils.dao.exception.DataAccessException;
import com.yaps.utils.dao.exception.DuplicateKeyException;
import com.yaps.utils.dao.exception.ObjectNotFoundException;
import com.yaps.utils.model.CheckException;
import com.yaps.utils.model.DomainObject;
import com.yaps.petstore.domain.annotation.propertyMetaData;

/**
 * Une base pour implémenter le pattern Data Access Object.
 * Dans cette version, les identifiants sont supposés être systématiquement des
 * Strings.
 */
public abstract class AbstractDAO<T extends DomainObject> {

    private Connection connection;
    private final String tableName;
    private final Class<T> type;
    private final String[] fieldsNames;
    

    public AbstractDAO(Connection connection, String tableName, Class<T> type) {
        this.connection = connection;
        this.tableName = tableName;
        this.type = type;
        this.fieldsNames = createFieldsNames();//fieldsNames.clone();
        
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
                int fieldsOrder[] = IntStream.rangeClosed(1, getFieldsNames().length+1).toArray();
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
            int N = getFieldsNames().length;
            int[] fieldsOrder = IntStream.rangeClosed(0, N).toArray();
            fieldsOrder[0] = N+1;
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

    protected String[] createFieldsNames() {
        Field[] declaredFields = type.getDeclaredFields();
        List<String> myList = new ArrayList<String>();
        for (Field field : declaredFields) {
            propertyMetaData ann = field.getAnnotation(propertyMetaData.class);
            myList.add(ann.columnName());
        }
        return myList.toArray(new String[0]);
    }

    /**
     * Extract Dara from the result of SQL query (ResultSet)
     * and construct a T object
     * 
     * @param res
     * @return
     */
    protected T extractData(ResultSet res) throws SQLException, ObjectNotFoundException {
        // String id = res.getString("id");
        List<Object> argList = new ArrayList<>();
        argList.add(res.getString("id"));
        String[] columns = getFieldsNames();
        for(String col : columns){
            argList.add(res.getString(col));
        }
        return extractSpecificData(argList);
        // return type.getDeclaredConstructor(List.class).newInstance(argList);
    }
    
    protected abstract T extractSpecificData(List<Object> argList) throws ObjectNotFoundException;
    
    /**
     * fill prepared statement with object fields
     * in the given order
     * 
     * @param pst
     * @param obj
     * @param fieldsOrder
     * 
     */
    protected void fillPreparedStatement(PreparedStatement pst, T obj, int[] fieldsOrder) throws SQLException{
        Field[] declaredFields = obj.getClass().getDeclaredFields();
        Field[] declaredParentFields = obj.getClass().getSuperclass().getDeclaredFields();
        Field[] declaredAllFields = ArrayUtils.addAll(declaredParentFields, declaredFields);
        try {
            for (Field field : declaredAllFields) {
                field.setAccessible(true);
                propertyMetaData ann = field.getAnnotation(propertyMetaData.class);
                int pos = (ann.order() + fieldsOrder[0] - 1 - 2 * fieldsOrder.length) % fieldsOrder.length
                        + fieldsOrder.length;
                Object fieldObj= field.get(obj);
                if (fieldObj instanceof DomainObject) {
                    pst.setObject(pos, ((DomainObject) fieldObj).getId());
                }
                else {
                    pst.setObject(pos, fieldObj);
                }
            } 
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
    
}
