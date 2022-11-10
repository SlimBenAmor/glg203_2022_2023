package com.yaps.petstore.customerWebApplication;

import java.util.HashMap;

import org.junit.jupiter.api.BeforeEach;

import com.gargoylesoftware.htmlunit.WebClient;
import com.yaps.petstore.customerApplication.domain.Address;
import com.yaps.petstore.customerApplication.domain.Customer;
import com.yaps.petstore.customerWebApplication.ui.DisplayCustomerServletTest;

/**
 * Abstract base for tests.
 * Note that the fact that annotations are not inherited
 * precludes their use here.
 * 
 * Predefines a map with customers in it.
 * <p>
 * The map is protected, and can freely be accessed by tests.
 */
public abstract class AbstractCustomerServletTest {

    /**
     * The web client to use for the tests.
     */
    protected WebClient webClient;

    /**
     * A map from customers ids to customers.
     */
    protected HashMap<String, Customer> map = new HashMap<>();

    private String baseUrl;

    /**
     * An address to use.
     */
    protected Address cnamAddress = Address.builder()
            .setStreet1("292 Rue Saint-Martin")
            .setZipcode("75003")
            .setCity("Paris")
            .setCountry("France").build();

    /**
     * An address with no empty field
     */
    protected Address fullAddress = Address.builder()
            .setStreet1("street1xa")
            .setStreet2("street2xa")
            .setCity("cityxa")
            .setState("statexa")
            .setZipcode("zipcodexa")
            .setCountry("countryxa").build();

    /**
     * A method to be called before each test.
     * 
     * Please call it from the actual test. See {@link DisplayCustomerServletTest#beforeEach()}.
     */
    public void beforeEach() {
        Customer c1 = new Customer("10", "firstnamexa", "lastnamexa", "101010", fullAddress);
        Customer c2 = new Customer("123", "Pascal", "Graffion", "06020305", cnamAddress);
        map.put(c1.getId(), c1);
        map.put(c2.getId(), c2);

        webClient = new WebClient();
        baseUrl = "http://localhost:" + getPort() + "/";
    }

    /**
     * @return the baseUrl
     */
    protected String getBaseUrl() {
        return baseUrl;
    }

    /**
     * Returns the port used by this test for http access.
     * Note that this will be typically injected. See
     * {@link DisplayCustomerServletTest} for an example.
     * 
     * @return
     */
    protected abstract String getPort();

}
