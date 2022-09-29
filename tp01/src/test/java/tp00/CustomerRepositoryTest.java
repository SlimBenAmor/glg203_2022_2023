package tp00;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CustomerRepositoryTest {

    private CustomerRepository repository;

    @BeforeEach
    public void setUp() {
        repository = new CustomerRepository();
    }

    /**
     * This method ensures that creating an object works. It first finds the object,
     * makes sure it doesn't exist, creates it and checks it then exists.
     */
    @Test
    public void testCreateCustomer() {
        final int id = 102;
        Customer customer = findCustomer(id);

        // Ensures that the object doesn't exist
        if (customer != null)
            fail("Object has not been created yet it shouldn't be found");

        // Creates an object
        createCustomer(id);

        // Ensures that the object exists
        customer = findCustomer(id);
        if (customer == null)
            fail("Object has been created it should be found");

        // Checks that it's the right object
        checkCustomer(customer, id);
    }

    @Test
    public void testRemoveCustomer() {
        createCustomer(6);
        removeCustomer(6);
        Customer c = findCustomer(6);
        if (c != null)
            fail("Object has been deleted it shouldn't be found");
    }

    /**
     * This test tries to find an object with an invalid identifier.
     */
    @Test
    public void testFindCustomerWithInvalidValues() {
        // Finds an object with an empty identifier
        if (repository.find(new String()) != null)
            fail("Object with empty id should not be found");

        // Finds an object with a null identifier
        if (repository.find(null) != null)
            fail("Object with null id should not be found");
    }

    @Test
    public void testFindWithUnknownId() {
        createCustomer(100);
        createCustomer(200);
        // Finds an object with an unknown identifier
        final int id = 300;
        Customer customer = findCustomer(id);
        if (customer != null)
            fail("Object with unknonw id should not be found");
    }

    /**
     * Check that we can't create two customers with the same id in the repository.
     */
    @Test
    public void testCantCreateWithSameId() {
        Customer c1 = new Customer("1", "a", "a");
        Customer c2 = new Customer("1", "b", "b");
        boolean ok1 = repository.insert(c1);
        assertTrue(ok1, "first insert ok");
        boolean ok2 = repository.insert(c2);
        assertFalse(ok2, "second insert with same id should fail");
        Customer c = repository.find("1");
        assertEquals("a", c.getFirstname(), "customer should be first inserted");
    }

    @Test
    public void testRemoveReturn() {
        createCustomer(101);
        boolean ok1 = removeCustomer(101);
        assertTrue(ok1, "successful remove should return true");
        boolean ok2 = removeCustomer(101);
        assertFalse(ok2, "further remove should return false");
    }

    @Test
    public void testRemoveReturnsFalse() {
        createCustomer(101);
        createCustomer(102);
        boolean ok1 = removeCustomer(51);
        assertFalse(ok1, "Removing non existant element should return false");
    }

    /**
     * Ensure we can create at least two different customers !
     */
    @Test
    public void testCreateMultipleCustomers() {
        createCustomer(2);
        createCustomer(100);
        Customer c1 = findCustomer(2);
        Customer c2 = findCustomer(100);
        assertEquals("custo2", c1.getId());
        assertEquals("custo100", c2.getId());

        Customer c3 = findCustomer(3);
        assertNull(c3, "Customer should not be found for id 3");
    }

    /**
     * This test ensures that the system cannont remove an unknown object
     */
    @Test
    public void testDeleteUnknownCustomer() {
        // Removes an unknown object
        int id = 1000;
        String sid = "custo" + id;
        if (repository.remove(sid))
            fail("Deleting an unknown object should break");
    }

    // ==================================
    // = Private Methods =
    // ==================================
    private Customer findCustomer(final int id) {
        final Customer customer = repository.find("custo" + id);
        return customer;
    }

    private boolean createCustomer(final int id) {
        final Customer customer = new Customer("custo" + id, "firstname" + id, "lastname" + id);
        customer.setCity("city" + id);
        customer.setCountry("cnty" + id);
        customer.setState("state" + id);
        customer.setStreet1("street1" + id);
        customer.setStreet2("street2" + id);
        customer.setTelephone("phone" + id);
        customer.setZipcode("zip" + id);
        return repository.insert(customer);
    }

    private boolean removeCustomer(final int id) {
        final String sid = "custo" + id;
        return repository.remove(sid);
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
