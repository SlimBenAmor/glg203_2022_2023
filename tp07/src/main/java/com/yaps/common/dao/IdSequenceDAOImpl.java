package com.yaps.common.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import com.yaps.common.dao.exception.YapsDataException;

public class IdSequenceDAOImpl implements IdSequenceDAO {

	private Logger logger = LoggerFactory.getLogger(IdSequenceDAOImpl.class);
	
	private JdbcTemplate jdbcTemplate;

	public IdSequenceDAOImpl(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	/**
	 * Returns the maximal sequence id stored for items in a certain table. Doesn't
	 * check if it is actually the maximal id.
	 * 
	 * @param tableName the name of the table (lowercase).
	 * @return the maximal id, as stored by the sequence.
	 */
	public int getCurrentMaxId(String tableName) {
		logger.debug("calling getCurrentMaxId for {}", tableName);
		String sql = "select max_id from sequence_id where table_name = ?";
		return jdbcTemplate.queryForObject(sql,
				Integer.class,
				tableName.toLowerCase());
	}

	public void setCurrentMaxId(String tableName, int newMaxId) {
		logger.debug("calling getCurrentMaxId for {} with value {}", tableName, newMaxId);
		if (newMaxId < 0)
			throw new YapsDataException("Invalid max id " + newMaxId);
		int currentMaxId = getCurrentMaxId(tableName);
		if (currentMaxId > newMaxId) {
			throw new YapsDataException("max id should increase : " + newMaxId + " < " + currentMaxId);
		}
		String sql = "update sequence_id set max_id = ? where table_name = ?";
		jdbcTemplate.update(sql, newMaxId, tableName);
	}
}
