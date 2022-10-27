package com.yaps.common.dao;

import java.util.List;
import java.util.Optional;

import com.yaps.common.dao.exception.DuplicateKeyException;
import com.yaps.common.dao.exception.ObjectNotFoundException;
import com.yaps.common.model.CheckException;

public interface DAO<T> {

	/**
     * Sauvegarde un objet en base.
     * 
     * @param obj
     */
	void save(T entity) throws DuplicateKeyException, CheckException;

	 /**
     * Met à jour un objet déjà connu.
     * 
     * @param obj
     */
	void update(T entity) throws ObjectNotFoundException, CheckException;

	 /**
     * Cherche un objet par identifiant.
     * 
     * @param obj
     */
	Optional<T> findById(String id);

	/**
     * Renvoie tous les objets.
     * 
     * @param obj
     */
	List<T> findAll();

	 /**
     * Détruit un objet dont on passe l'identifiant.
     * 
     * @param obj
     */
	void remove(String id) throws ObjectNotFoundException;

	/**
	 * Removes all entries of this type.
	 * Cette méthode n'est pas très habituelle dans une DAO.
	 * Nous l'avons ajoutée pour faciliter l'écriture des tests.
	 */
	void removeAll();

}