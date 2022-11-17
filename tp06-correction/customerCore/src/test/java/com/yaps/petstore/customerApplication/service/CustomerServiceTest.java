package com.yaps.petstore.customerApplication.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

import com.yaps.common.dao.ConnectionManager;
import com.yaps.common.dao.DAO;
import com.yaps.common.dao.exception.DuplicateKeyException;
import com.yaps.common.model.CheckException;
import com.yaps.petstore.config.TestDBConfig;
import com.yaps.petstore.customerApplication.domain.Customer;


@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class CustomerServiceTest {

    @Configuration
    @Import({TestDBConfig.class})
    @ComponentScan("com.yaps.petstore.customerApplication.service")
    @ComponentScan("com.yaps.petstore.customerApplication.dao")
    public static class MyConfig {

    }

    @Autowired
    CustomerService service;

    @Autowired
    DAO<Customer> dao;

    @Autowired
    ConnectionManager connectionManager ;
    

    @Test
    public void testFindAllSimple() {
        List<Customer> customers = service.findAll();
        assertNotNull(customers);
    }

    @Test
    public void testRemoveAll() {
        service.removeAll();
        List<Customer> customers = service.findAll();
        assertEquals(Collections.emptyList(), customers);
    }

    @Test
    public void testSave() throws DuplicateKeyException, CheckException {        
        service.removeAll();
        Customer c0 = new Customer(null, "a", "b");
        String res = service.save(c0);
        assertNotNull(res);
        // res représente un entier :
        assertDoesNotThrow(
            () -> Integer.parseInt(res)
        );
        connectionManager.open();
        try {
            Customer checkCustomer = dao.findById(res).get();
            assertEquals("a", checkCustomer.getFirstname());
            assertEquals("b", checkCustomer.getLastname());
        } finally {
            connectionManager.close();
        }
    }

    @Test
    public void testTwoSaves() throws DuplicateKeyException, CheckException {
        service.removeAll();
        Customer c0 = new Customer(null, "a", "b");
        String res1 = service.save(c0);
        Customer c1 = new Customer(null, "c", "d");
        String res2 = service.save(c1);
        assertTrue(res1.compareTo(res2) < 0, "Ids should increase");
    }

    @Test
    public void testFindyId() throws DuplicateKeyException, CheckException {
        service.removeAll();
        Customer c0 = new Customer(null, "a", "b");
        String res1 = service.save(c0);
        Customer c1 = new Customer(null, "c", "d");
        String res2 = service.save(c1);
        Optional<Customer> c3 = service.findById(res1);
        assertFalse(c3.isEmpty());
        assertEquals("a", c3.get().getFirstname());
        assertTrue(res1.compareTo(res2) < 0, "Ids should increase");
    }


    @Test
    public void testFindNone() throws DuplicateKeyException, CheckException {
        service.removeAll();
        Customer c0 = new Customer(null, "a", "b");
        String res1 = service.save(c0);        
        Optional<Customer> c3 = service.findById(res1 + "0");
        assertTrue(c3.isEmpty(), "Unknown elements should not be found");        
    }



    
}
