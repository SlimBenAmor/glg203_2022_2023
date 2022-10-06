package com.yaps.petstore;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * This class tests the CustomerDAO class
 */
public final class CustomerDAOTest {

    /**
     * The customer repository.
     * Will be recreated before each test to ensure test independance.
     */
    private CustomerDAO dao;

    @BeforeEach
    public void initDAO() {
        this.dao = new CustomerDAO();        
    }

    // ==================================
    // = Test cases =
    // ==================================
    /**
     * This test tries to find an object with a invalid identifier.
     * TODO: one test / test method
     */
    @Test
    public void testFindCustomerWithInvalidValues() throws Exception {

        // Finds an object with a unknown identifier
        final int id = 1; // pas encore utilisé !

        assertThrows(CustomerNotFoundException.class,
                () -> findCustomer(id),
                "Object with unknonw id should not be found");

        assertThrows(CustomerNotFoundException.class,
                () -> dao.find(new String()),
                "Object with empty id should not be found");

        // Finds an object with a null identifier

        assertThrows(CustomerNotFoundException.class,
                () -> dao.find(null),
                "Object with null id should not be found");
    }

    /**
     * This method ensures that creating an object works. It first finds the object,
     * makes sure it doesn't exist, creates it and checks it then exists.
     */
    @Test
    public void testCreateCustomer() throws Exception {
        final int id = 1;
        Customer customer = null;

        // Ensures that the object doesn't exist
        try {
            customer = findCustomer(id);
            fail("Object has not been created yet it shouldn't be found");
        } catch (CustomerNotFoundException e) {
        }

        // Creates an object
        createCustomer(id);

        // Ensures that the object exists
        try {
            customer = findCustomer(id);
        } catch (CustomerNotFoundException e) {
            fail("Object has been created it should be found");
        }

        // Checks that it's the right object
        checkCustomer(customer, id);

        // Creates an object with the same identifier. An exception has to be thrown
        try {
            createCustomer(id);
            fail("An object with the same id has already been created");
        } catch (CustomerDuplicateKeyException e) {
        }

        // Cleans the test environment
        removeCustomer(id);

        try {
            findCustomer(id);
            fail("Object has been deleted it shouldn't be found");
        } catch (CustomerNotFoundException e) {
        }
    }

    /**
     * This test tries to update an unknown object.
     */
    @Test
    public void testUpdateUnknownCustomer() throws Exception {
        // Updates an unknown object
        try {
            dao.update(new Customer());
            fail("Updating a none existing object should break");
        } catch (CustomerCheckException e) {
        }
    }

    /**
     * This test tries to update an object with a invalid values.
     */
    @Test
    public void testUpdateCustomerWithInvalidValues() throws Exception {

        // Creates an object
        final int id = 100;
        createCustomer(id);

        // Ensures that the object exists
        Customer customer = null;
        try {
            customer = findCustomer(id);
        } catch (CustomerNotFoundException e) {
            fail("Object has been created it should be found");
        }

        // Updates the object with empty values
        try {
            customer.setFirstname(new String());
            customer.setLastname(new String());
            dao.update(customer);
            fail("Updating an object with empty values should break");
        } catch (CustomerCheckException e) {
        }

        // Updates the object with null values
        try {
            customer.setFirstname(null);
            customer.setLastname(null);
            dao.update(customer);
            fail("Updating an object with null values should break");
        } catch (CustomerCheckException e) {
        }

        // Ensures that the object still exists
        try {
            customer = findCustomer(id);
        } catch (CustomerNotFoundException e) {
            fail("Object should be found");
        }

        // Cleans the test environment
        removeCustomer(id);

        try {
            findCustomer(id);
            fail("Object has been deleted it shouldn't be found");
        } catch (CustomerNotFoundException e) {
        }
    }

    /**
     * This test make sure that updating an object success
     */
    @Test
    public void testUpdateCustomer() throws Exception {
        final int id = 1;

        // Creates an object
        createCustomer(id);

        // Ensures that the object exists
        Customer customer = null;
        try {
            customer = findCustomer(id);
        } catch (CustomerNotFoundException e) {
            fail("Object has been created it should be found");
        }

        // Checks that it's the right object
        checkCustomer(customer, id);

        // Updates the object with new values
        updateCustomer(customer, id + 1);

        // Ensures that the object still exists
        Customer customerUpdated = null;
        try {
            customerUpdated = findCustomer(id);
        } catch (CustomerNotFoundException e) {
            fail("Object should be found");
        }

        // Checks that the object values have been updated
        checkCustomer(customerUpdated, id + 1);

        // Cleans the test environment
        removeCustomer(id);

        try {
            findCustomer(id);
            fail("Object has been deleted it shouldn't be found");
        } catch (CustomerNotFoundException e) {
        }
    }

    /**
     * This test ensures that the system cannont remove an unknown object
     */
    @Test
    public void testDeleteUnknownCustomer() throws Exception {
        // Removes an unknown object
        try {
            int id = 1;
            String sid = "custo" + id;
            dao.remove(sid);
            fail("Deleting an unknown object should break");
        } catch (CustomerNotFoundException e) {
        }
    }

    // ==================================
    // = Private Methods =
    // ==================================
    private Customer findCustomer(final int id) throws CustomerFinderException, CustomerCheckException {
        final Customer customer = dao.find("custo" + id);
        return customer;
    }

    private void createCustomer(final int id) throws CustomerCreateException, CustomerCheckException {
        final Customer customer = new Customer("custo" + id, "firstname" + id, "lastname" + id);
        customer.setCity("city" + id);
        customer.setCountry("cnty" + id);
        customer.setState("state" + id);
        customer.setStreet1("street1" + id);
        customer.setStreet2("street2" + id);
        customer.setTelephone("phone" + id);
        customer.setZipcode("zip" + id);
        dao.insert(customer);
    }

    private void updateCustomer(final Customer customer, final int id)
            throws CustomerNotFoundException, CustomerDuplicateKeyException, CustomerCheckException {
        customer.setFirstname("firstname" + id);
        customer.setLastname("lastname" + id);
        customer.setCity("city" + id);
        customer.setCountry("cnty" + id);
        customer.setState("state" + id);
        customer.setStreet1("street1" + id);
        customer.setStreet2("street2" + id);
        customer.setTelephone("phone" + id);
        customer.setZipcode("zip" + id);
        dao.update(customer);
    }

    private void removeCustomer(final int id) throws CustomerNotFoundException {
        final String sid = "custo" + id;
        dao.remove(sid);
    }

    private void checkCustomer(final Customer customer, final int id) {
        assertEquals("firstname" + id, customer.getFirstname(), "firstname");
        assertEquals("lastname" + id, customer.getLastname(), "lastname");
        assertEquals("city" + id, customer.getCity(), "city");
        assertEquals("cnty" + id, customer.getCountry(), "country");
        assertEquals("state" + id, customer.getState(), "state");
        assertEquals("street1" + id, customer.getStreet1(), "street1");
        assertEquals("street2" + id, customer.getStreet2(), "street2");
        assertEquals("phone" + id, customer.getTelephone(), "telephone");
        assertEquals("zip" + id, customer.getZipcode(), "zipcode");
    }

}
