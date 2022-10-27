package com.yaps.petstore.customerApplication.service;


import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.yaps.common.dao.exception.DuplicateKeyException;
import com.yaps.common.dao.exception.ObjectNotFoundException;
import com.yaps.common.model.CheckException;
import com.yaps.common.service.EntityService;
import com.yaps.petstore.customerApplication.domain.Customer;

@Service
public interface CustomerService extends EntityService<Customer> {

    /**
     * Cherche un customer, connaissant son id.
     */
	Optional<Customer> findById(String id);

    /**
     * Supprime un customer, connaissant sont id.
     */
	void remove(String id) throws ObjectNotFoundException;

    /**
     * Renvoie tous les customers.
     */
	List<Customer> findAll();

	/**
	 * Sauvegarde un customer.
     * La méthode lui attribue un nouvel id.
     * Les ids attribués doivent être strictement croissants dans la base.
	 * @param customer
	 * @return
	 * @throws DuplicateKeyException
	 * @throws CheckException
	 */
	String save(Customer customer) throws DuplicateKeyException, CheckException;

    /**
     * Met à jour un customer.
     */
	void update(Customer customer) throws ObjectNotFoundException, CheckException;

    /**
     * Vérifie qu'il y a au moins un customer.
     */
	boolean isNotEmpty();

	/**
	 * Clear all entries for this entity.
	 * (to ease testing...)
	 * Relatively dangerous method.
	 */
	void removeAll();
}
