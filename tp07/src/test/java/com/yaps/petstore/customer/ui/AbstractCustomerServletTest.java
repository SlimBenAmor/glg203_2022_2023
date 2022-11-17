package com.yaps.petstore.customer.ui;

import java.util.HashMap;

import com.yaps.petstore.customer.domain.Address;
import com.yaps.petstore.customer.domain.Customer;
import com.yaps.petstore.customer.dto.CustomerDTO;
import com.yaps.petstore.customer.service.CustomerDTOMapper;

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
     * A map from customers ids to customers.
     */
    protected HashMap<String, CustomerDTO> map = new HashMap<>();

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
        Customer c1 = new Customer("10", "firstnamexa", "lastnamexa", "101010", "c1@cnam.fr", fullAddress);
        Customer c2 = new Customer("123", "Pascal", "Graffion", "06020305", "c2@cnam.fr", cnamAddress);
        // CustomerDTOMapper should probably not be used here. But, well, it just works.

        map.put(c1.getId(), CustomerDTOMapper.toDTO(c1));
        map.put(c2.getId(), CustomerDTOMapper.toDTO(c2));
    }


}
