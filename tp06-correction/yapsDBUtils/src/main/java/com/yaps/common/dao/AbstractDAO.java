package com.yaps.common.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

import com.yaps.common.dao.exception.DataAccessException;
import com.yaps.common.dao.exception.ObjectNotFoundException;
import com.yaps.common.model.DomainObject;

/**
 * Une base pour implémenter le pattern Data Access Object.
 * Dans cette version, les identifiants sont supposés être systématiquement des
 * Strings.
 */
public abstract class AbstractDAO<T extends DomainObject> implements DAO<T> {

    private ConnectionManager connectionManager;
    
    private final String tableName;
    private final String[] fieldsNames;

    protected AbstractDAO(ConnectionManager connectionManager, String tableName, String[] fieldsNames) {
        this.connectionManager = connectionManager;
        this.tableName = tableName;
        this.fieldsNames = fieldsNames.clone();
    }


    protected ConnectionManager getConnectionManager() {
		return connectionManager;
	}
    
    protected Connection getConnection() {
    	return connectionManager.getConnection();
    }
   
    /**
     * Détruit un objet dont on passe l'identifiant.
     * 
     * @param obj
     */
    public void remove(String id) throws ObjectNotFoundException {
    	// On aimerait utiliser connectionManager.doWithConnection(), 
    	// mais les lambda et les exceptions ne s'entendent pas très bien...
        Optional<T> obj = findById(id);
        if (obj.isEmpty())
            throw new ObjectNotFoundException();
        String sql = "delete from " + getTableName() + " where id = ?";
        Connection connection = connectionManager.getConnection();
        try (PreparedStatement pst =connection.prepareStatement(sql)) {
            pst.setString(1, id);
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(sql, e);
        }
    }

    @Override
    public void removeAll() {
        Connection connection = connectionManager.getConnection();
        try (Statement st =connection.createStatement()) {            
            st.executeUpdate("delete from "+ getTableName());
        } catch (SQLException e) {
            throw new DataAccessException("error when deleting from table " + getTableName(), e);
        }
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


}
