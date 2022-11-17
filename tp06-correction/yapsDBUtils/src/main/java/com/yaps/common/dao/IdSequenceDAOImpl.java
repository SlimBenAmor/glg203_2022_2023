package com.yaps.common.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.yaps.common.dao.exception.DataAccessException;

public class IdSequenceDAOImpl implements IdSequenceDAO{

	private ConnectionManager connectionManager;

	public IdSequenceDAOImpl(ConnectionManager connectionManager) {
		this.connectionManager = connectionManager;
	}

	/**
	 * Returns the maximal sequence id stored for items in a certain table. Doesn't
	 * check if it is actually the maximal id.
	 * 
	 * @param tableName the name of the table (lowercase).
	 * @return the maximal id, as stored by the sequence.
	 */
	public int getCurrentMaxId(String tableName) {
		String sql = "select max_id from sequence_id where table_name = ?";
		Connection connection = connectionManager.getConnection();
		try (PreparedStatement pst = connection.prepareStatement(sql)) {
			pst.setString(1, tableName.toLowerCase());
			try (ResultSet resultSet = pst.executeQuery()) {
				if (resultSet.next()) {
					return resultSet.getInt(1);
				} else {
					throw new DataAccessException(
							"Corrupted database, missing entry in sequence_id for table " + tableName);
				}
			}
		} catch (SQLException e) {
			throw new DataAccessException(sql, e);
		}
	}

	public void setCurrentMaxId(String tableName, int newMaxId) {
		if (newMaxId < 0)
			throw new DataAccessException("Invalid max id " + newMaxId);
		int currentMaxId = getCurrentMaxId(tableName);
		if (currentMaxId > newMaxId) {
			throw new DataAccessException("max id should increase : " + newMaxId + " < " + currentMaxId);
		}
		String sql = "update sequence_id set max_id = ? where table_name = ?";
		Connection connection = connectionManager.getConnection();
		try (PreparedStatement pst = connection.prepareStatement(sql)) {
			pst.setInt(1, newMaxId);
			pst.setString(2, tableName);
			pst.executeUpdate();
		} catch (SQLException e) {
			throw new DataAccessException(sql, e);
		}
	}
}
