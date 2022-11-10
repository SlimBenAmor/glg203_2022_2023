package com.yaps.common.service;

import java.util.List;
import java.util.Optional;

import com.yaps.common.dao.exception.DuplicateKeyException;
import com.yaps.common.dao.exception.ObjectNotFoundException;
import com.yaps.common.model.CheckException;

/**
 * Interface for services related to a given entity.
 * @author rosmord
 *
 * @param <E> the entity type (Customer, for instance).
 */
public interface EntityService<E> {

	Optional<E> findById(String id);

	void remove(String id) throws ObjectNotFoundException;

	List<E> findAll();

	/**
	 * Saves a entity and returns its id.
	 * @param entity
	 * @return
	 * @throws DuplicateKeyException
	 * @throws CheckException
	 */
	String save(E entity) throws DuplicateKeyException, CheckException;

	void update(E entity) throws ObjectNotFoundException, CheckException;

	boolean isNotEmpty();

	/**
	 * Clear all entries for this entity.
	 * (to ease testing...)
	 * Relatively dangerous method.
	 */
	void removeAll();

}
