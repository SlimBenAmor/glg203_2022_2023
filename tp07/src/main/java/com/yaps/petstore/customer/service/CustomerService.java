package com.yaps.petstore.customer.service;


import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.yaps.common.dao.exception.ObjectNotFoundException;
import com.yaps.common.model.CheckException;
import com.yaps.petstore.customer.dto.CustomerDTO;

@Service
public interface CustomerService {

    /**
     * Cherche un customer, connaissant son id.
     */
	Optional<CustomerDTO> findById(String id);

    /**
     * Supprime un customer, connaissant sont id.
     */
	void remove(String id) throws ObjectNotFoundException;

    /**
     * Renvoie tous les customers.
     */
	List<CustomerDTO> findAll();

	/**
	 * Sauvegarde un customer.
     * La méthode lui attribue un nouvel id.
     * Les ids attribués doivent être strictement croissants dans la base.
	 * @param customer
	 * @return	 
	 * @throws CheckException si les données du customer sont incorrectes.
	 */
	String save(CustomerDTO customer) throws CheckException;

    /**
     * Met à jour un customer.
     */
	void update(CustomerDTO customer) throws ObjectNotFoundException, CheckException;

    /**
     * Vérifie qu'il y a au moins un customer.
     */
	boolean isNotEmpty();
}
