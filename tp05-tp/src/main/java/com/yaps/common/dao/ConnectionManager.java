package com.yaps.common.dao;

import java.sql.Connection;
import java.util.function.Consumer;
import java.util.function.Function;

import com.yaps.common.dao.exception.DataAccessException;

/**
 * A ConnectionManager knows how to connect to a database, and manages
 * opening/closing connections.
 * 
 * Note that ConnectionManager is not AutoCloseable. An newly created ConnectionManager
 * is normally closed, and would be opened on demand.
 * @author rosmord
 */
public interface ConnectionManager  { 

	/**
	 * Open a connection if necessary.
	 * 
	 * @throws DataAccessException
	 */
	void open() throws DataAccessException;

	/**
	 * Closes a connection if possible. open/close should be matched. Typically this
	 * method should be in a finally.
	 * 
	 * Connection is really closed if the number of closes matches the number of open.
	 * 
	 * <p> Precondition : open() has been called more times than close().
	 * @throws DataAccessException if the connection is not opened.
	 */
	void close() throws DataAccessException;

	/**
	 * gets the current connection if available.
	 * <p>Pre-condition: the connection should be opened.
	 * @throws DataAccessException if none is.
	 * @return
	 */
	Connection getConnection() throws DataAccessException;

	/**
	 * Performs an operation which needs a database connection.
	 * @param consumer
	 * @throws DataAccessException
	 */
	default  void doWithConnection(Consumer<Connection> consumer) throws DataAccessException {
		open();
		try {
			Connection connection = getConnection();
			consumer.accept(connection);
		} finally {
			close();
		}		
	}

	/**
	 * Performs an computation which needs a database connection and returns the result.
	 * @param <T> type of the result
	 * @param function the computation to perform.
	 * @return the computed result.
	 * @throws DataAccessException if technical problems occurred.
	 */
	default <T> T computeWithConnection(Function<Connection, T> function) throws DataAccessException {
		open();
		try {
			Connection connection = getConnection();
			return function.apply(connection);
		} finally {
			close();
		}
	}
}
