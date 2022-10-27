package com.yaps.common.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.yaps.common.dao.exception.DataAccessException;

public class ConnectionManagerImpl implements ConnectionManager {

	/**
	 * Compute the number of DB opening to close them.
	 */
	private int numberOfOpen = 0;

	private Connection currentConnection;

	private String url;
	private String userName;
	private String password;

	

	/**
	 * @param url
	 * @param userName
	 * @param password
	 */
	public ConnectionManagerImpl(String url, String userName, String password) {
		this.url = url;
		this.userName = userName;
		this.password = password;
	}

	@Override
	public void open() throws DataAccessException {
		try {
			if (numberOfOpen == 0) {
				currentConnection = DriverManager.getConnection(url, userName, password);
			}
			numberOfOpen++;
		} catch (SQLException e) {
			throw new DataAccessException("Could not connect to database", e);
		}
	}

	@Override
	public void close() throws DataAccessException {
		if (numberOfOpen == 0) {
			throw new DataAccessException("No connection to close");
		} else {
			numberOfOpen--;
			if (numberOfOpen == 0) {
				try {
					currentConnection.close();
				} catch (SQLException e) {
					throw new DataAccessException("Could not close connection", e);
				} finally {
					currentConnection = null;
				}

			}
		}

	}

	@Override
	public Connection getConnection() throws DataAccessException {
		if (currentConnection != null) {
			return currentConnection;
		} else
			throw new DataAccessException("Connection is not opened !");
	}

}
