package com.yaps.petstore.customerApplication.service;

import java.util.List;
import java.util.Optional;

import com.yaps.common.dao.ConnectionManager;
import com.yaps.common.dao.IdSequenceDAO;
import com.yaps.common.dao.exception.DuplicateKeyException;
import com.yaps.common.dao.exception.ObjectNotFoundException;
import com.yaps.common.model.CheckException;
import com.yaps.petstore.customerApplication.dao.CustomerDAOImpl;
import com.yaps.petstore.customerApplication.domain.Customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerServiceImpl implements CustomerService {
    
    @Autowired
    CustomerDAOImpl dao;

    @Autowired
    IdSequenceDAO idSequenceDAO;

    @Autowired
    ConnectionManager connectionManager;
    
    @Override
    public Optional<Customer> findById(String id) {
        connectionManager.open();
        try{
            return dao.findById(id);
        } finally {
            connectionManager.close();
        }
    }

    @Override
    public void remove(String id) throws ObjectNotFoundException {
        connectionManager.open();
        try{
            dao.remove(id);
        } finally {
            connectionManager.close();
        }
    }

    @Override
    public List<Customer> findAll() {
        connectionManager.open();
        try{
            return dao.findAll();
        } finally {
            connectionManager.close();
        }
    }

    @Override
    public String save(Customer customer) throws DuplicateKeyException, CheckException {
        connectionManager.open();
        try {
            int id = idSequenceDAO.getCurrentMaxId(dao.getTableName())+1;
            customer.setId(Integer.toString(id));
            dao.save(customer);
            idSequenceDAO.setCurrentMaxId(dao.getTableName(),id);
            return Integer.toString(id);
        } finally {
            connectionManager.close();
        }
    }

    @Override
    public void update(Customer customer) throws ObjectNotFoundException, CheckException {
        connectionManager.open();
        try{
            dao.update(customer);
        } finally {
            connectionManager.close();
        }
    }

    @Override
    public boolean isNotEmpty() {
        List<Customer> customers = findAll();
        return !customers.isEmpty();
    }

    @Override
    public void removeAll() {
        connectionManager.open();
        try{
            dao.removeAll();
        } finally {
            connectionManager.close();
        }
    }
    
}
