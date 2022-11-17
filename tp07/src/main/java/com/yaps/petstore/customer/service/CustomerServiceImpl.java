package com.yaps.petstore.customer.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yaps.common.dao.DAO;
import com.yaps.common.dao.IdSequenceDAO;
import com.yaps.common.dao.exception.YapsDataException;
import com.yaps.common.dao.exception.DuplicateKeyException;
import com.yaps.common.dao.exception.ObjectNotFoundException;
import com.yaps.common.model.CheckException;
import com.yaps.petstore.customer.domain.Customer;
import com.yaps.petstore.customer.dto.CustomerDTO;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    DAO<Customer> dao;

    @Autowired
    IdSequenceDAO idSequenceDAO;


    @Override
    public Optional<CustomerDTO> findById(String id) {
        // Remarquer l'usage de map sur un optional :
        // - si celui-ci est vide, renvoie un optional vide
        // - sinon applique la fonction pour créer un optional "plein", ici un Optional<CustomerDTO>
        return dao.findById(id).map(customer -> CustomerDTOMapper.toDTO(customer));
    }

    @Override
    public void remove(String id) throws ObjectNotFoundException {
        dao.remove(id);
    }

    @Override
    public List<CustomerDTO> findAll() {
        return dao.findAll().stream().map(CustomerDTOMapper::toDTO).toList();
    }

    @Override
    public String save(CustomerDTO customerDTO) throws CheckException {
        try {
            int newId = idSequenceDAO.getCurrentMaxId("customer") + 1;
            idSequenceDAO.setCurrentMaxId("customer", newId);
            customerDTO.setId(Integer.toString(newId));
            dao.save(CustomerDTOMapper.fromDTO(customerDTO));
            return Integer.toString(newId);
        } catch (DuplicateKeyException e) {
            // should not happen ?
            throw new YapsDataException("unexpected error", e);
        }
    }

    @Override
    public void update(CustomerDTO customerDTO) throws ObjectNotFoundException, CheckException {
        dao.update(CustomerDTOMapper.fromDTO(customerDTO));
    }

    @Override
    public boolean isNotEmpty() {
        return !dao.findAll().isEmpty();
    }
}
