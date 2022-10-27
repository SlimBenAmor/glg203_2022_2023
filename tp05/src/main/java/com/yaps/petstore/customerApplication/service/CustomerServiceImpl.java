package com.yaps.petstore.customerApplication.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yaps.common.dao.ConnectionManager;
import com.yaps.common.dao.DAO;
import com.yaps.common.dao.IdSequenceDAO;
import com.yaps.common.dao.exception.DuplicateKeyException;
import com.yaps.common.dao.exception.ObjectNotFoundException;
import com.yaps.common.model.CheckException;
import com.yaps.petstore.customerApplication.domain.Customer;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    DAO<Customer> dao;

    @Autowired
    IdSequenceDAO idSequenceDAO;

    @Autowired
    ConnectionManager manager;

    @Override
    public Optional<Customer> findById(String id) {
        manager.open();
        try {
            return dao.findById(id);
        } finally {
            manager.close();
        }
    }

    @Override
    public void remove(String id) throws ObjectNotFoundException {
        manager.open();
        try {
            dao.remove(id);
        } finally {
            manager.close();
        }
    }

    @Override
    public List<Customer> findAll() {
        manager.open();
        try {
            return dao.findAll();
        } finally {
            manager.close();
        }
    }

    @Override
    public String save(Customer entity) throws DuplicateKeyException, CheckException {
        manager.open();
        try {
            int newId = idSequenceDAO.getCurrentMaxId("customer") + 1;
            idSequenceDAO.setCurrentMaxId("customer", newId);
            entity.setId(Integer.toString(newId));
            dao.save(entity);
            return Integer.toString(newId);
        } finally {
            manager.close();
        }
        
    }

    @Override
    public void update(Customer entity) throws ObjectNotFoundException, CheckException {
        manager.open();
        try {
            dao.update(entity);
        } finally {
            manager.close();
        }
    }

    @Override
    public boolean isNotEmpty() {
        manager.open();
        try {
            return ! dao.findAll().isEmpty();
        } finally {
            manager.close();
        }
    }

    @Override
    public void removeAll() {
        manager.open();
        try {
            dao.removeAll();            
        } finally {
            manager.close();
        }
    }

    
}
