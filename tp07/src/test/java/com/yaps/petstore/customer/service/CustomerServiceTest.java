package com.yaps.petstore.customer.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import com.yaps.common.dao.DAO;
import com.yaps.common.dao.exception.DuplicateKeyException;
import com.yaps.common.model.CheckException;
import com.yaps.petstore.customer.config.TestDBConfig;
import com.yaps.petstore.customer.domain.Customer;
import com.yaps.petstore.customer.dto.CustomerDTO;


@SpringBootTest
// active application-test.properties en PLUS de application.properties
@ActiveProfiles("test")
// Sur un jeu de tests, @Transactional déclenche un rollback après le test, quel que soit
// sont résultat.
// Note : ça ne fonctionne pas bien sur les tests webclient, parce que la transaction est en dehors 
// du test lui-même.
@Transactional
// Remarque générale sur l'utilisation de @Sql dans ces tests :
// @Sql posé sur une classe de tests est joué UNE SEULE FOIS avant tous les tests.
// Il n'est pas très intéressant, donc.
// @Sql posé sur une méthode de tests est joué avant le test 
// (on peut aussi le faire jouer après le test, en passant des arguments)
// En revanche, il n'y a pas moyen de mettre en place un système pour fixer un code SQL
// global, qui serait joué avant chacun des tests (comme @BeforeEach pour le code java)
public class CustomerServiceTest {

    CustomerDTO c1, c2;

    @Configuration
    @Import({ TestDBConfig.class })
    @ComponentScan("com.yaps.petstore.customer.service")
    @ComponentScan("com.yaps.petstore.customer.dao")
    public static class MyConfig {

    }

   
    @BeforeEach
    public void init() {
        c1 = createCustomer("1");
        c2 = createCustomer("2");
        
    }

    private CustomerDTO createCustomer(String id) {
        CustomerDTO c = new CustomerDTO(id, "firstname"+ id, "lastname"+ id);
        c.setEmail("email"+id);
        c.setTelephone("telephone"+id);
        c.setStreet1("street1_"+id);
        c.setStreet2("street2_"+id);
        c.setCity("city"+id);
        c.setZipcode("zipcode"+id);
        c.setState("state"+id);
        c.setCountry("country"+id);
        return c;
    }

    @Autowired
    CustomerService service;

    @Autowired
    DAO<Customer> dao;

    @Test
    @Sql({
        "/testsql/customer/empty.sql"
    })
    public void testFindAllEmpty() {
        List<CustomerDTO> customers = service.findAll();
        assertNotNull(customers);
        assertEquals(0, customers.size());
    }

    @Test
    @Sql({
       //  "/testsql/01_cleanup.sql",
        "/testsql/customer/two.sql"
    })
    public void testFindAllTwo() {
        Set<String> expected = Set.of(c1.toString(), c2.toString());
        List<String> customers = service.findAll().stream().map(c -> c.toString()).toList();
        HashSet<String> computed = new HashSet<>(customers);
        assertEquals(expected, computed);
    }

    @Test
    @Sql("/testsql/customer/two.sql")
    public void testFindById() {
        Optional<CustomerDTO> optC = service.findById("2");
        assertAll(
            () -> optC.isPresent(),
            () -> optC.get().equals(c2)
        );
    }
    
    

    

    @Test
    @Sql("/testsql/customer/empty.sql")
    public void testSave() throws DuplicateKeyException, CheckException {
        CustomerDTO c0 = new CustomerDTO(null, "a", "b");
        String res = service.save(c0);
        assertNotNull(res);
        // res représente un entier :
        assertDoesNotThrow(
                () -> Integer.parseInt(res));
        Customer checkCustomer = dao.findById(res).get();
        assertEquals("a", checkCustomer.getFirstname());
        assertEquals("b", checkCustomer.getLastname());

    }

    @Test
    @Sql("/testsql/customer/empty.sql")
    public void testTwoSaves() throws DuplicateKeyException, CheckException {
        CustomerDTO c0 = new CustomerDTO(null, "a", "b");
        String res1 = service.save(c0);
        CustomerDTO c1 = new CustomerDTO(null, "c", "d");
        String res2 = service.save(c1);
        assertTrue(res1.compareTo(res2) < 0, "Ids should increase");
    }

    @Test
    @Sql("/testsql/customer/empty.sql")
    public void testFindyId() throws DuplicateKeyException, CheckException {
        CustomerDTO c0 = new CustomerDTO(null, "a", "b");
        String res1 = service.save(c0);
        CustomerDTO c1 = new CustomerDTO(null, "c", "d");
        String res2 = service.save(c1);
        Optional<CustomerDTO> c3 = service.findById(res1);
        assertFalse(c3.isEmpty());
        assertEquals("a", c3.get().getFirstname());
        assertTrue(res1.compareTo(res2) < 0, "Ids should increase");
    }

    @Test
    @Sql("/testsql/customer/two.sql")
    public void testFindNone() throws DuplicateKeyException, CheckException {
        Optional<CustomerDTO> c3 = service.findById("nope");
        assertTrue(c3.isEmpty(), "Unknown elements should not be found");
    }

}
