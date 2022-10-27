package com.yaps.utils.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
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
    private final String[] fieldsNames;

    protected AbstractDAO(Connection connection, String tableName, String[] fieldsNames) {
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
    public abstract void save(T obj) throws DuplicateKeyException, CheckException;

    /**
     * Met à jour un objet déjà connu.
     * 
     * @param obj
     */
    public abstract void update(T obj) throws ObjectNotFoundException, CheckException;

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
    public abstract Optional<T> findById(String id);

    /**
     * Renvoie tous les objets.
     * 
     * @param obj
     */
    public abstract List<T> findAll();

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

    public int getCurrentMaxId() {
        String sql = "select max_id from sequence_id where table_name = ?";
        try (PreparedStatement pst = getConnection().prepareStatement(sql)) {
            pst.setString(1, getTableName());
            try (ResultSet resultSet = pst.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1);
                } else {
                    throw new DataAccessException(
                            "Corrupted database, missing entry in sequence_id for table " + getTableName());
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(sql, e);
        }
    }

    public void setCurrentMaxId(int newMaxId) {
        if (newMaxId < 0)
            throw new DataAccessException("Invalid max id " + newMaxId);
        String sql = "update sequence_id set max_id = ? where table_name = ?";
        try (PreparedStatement pst = getConnection().prepareStatement(sql)) {
            pst.setInt(1, newMaxId);
            pst.setString(2, getTableName());
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(sql, e);
        }
    }

}
