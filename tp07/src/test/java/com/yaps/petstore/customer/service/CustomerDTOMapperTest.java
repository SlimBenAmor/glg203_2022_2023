package com.yaps.petstore.customer.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.yaps.petstore.customer.domain.Address;
import com.yaps.petstore.customer.domain.Customer;
import com.yaps.petstore.customer.dto.CustomerDTO;

public class CustomerDTOMapperTest {
    @Test
    void testFromDTO() {
        CustomerDTO customerDTO = new CustomerDTO("id2", "firstname2", "lastname2");
        customerDTO.setTelephone("telephone2");
        customerDTO.setEmail("email2");
        customerDTO.setStreet1("street1");
        customerDTO.setStreet2("street2");
        customerDTO.setCity("city2");
        customerDTO.setZipcode("zipcode2");
        customerDTO.setState("state2");
        customerDTO.setCountry("country2");
        Customer customer = CustomerDTOMapper.fromDTO(customerDTO);
        assertAll(
        () -> assertEquals("id2", customer.getId()),
        () -> assertEquals("firstname2", customer.getFirstname()),
        () -> assertEquals("lastname2", customer.getLastname()),
        () -> assertEquals("email2", customer.getEmail()),
        () -> assertEquals("telephone2", customer.getTelephone()),
        () -> assertEquals(
            // @formatter:off
            Address.builder()
                .setStreet1("street1")
                .setStreet2("street2")
                .setCity("city2")
                .setZipcode("zipcode2")
                .setState("state2")
                .setCountry("country2")
                .build()
            // @formatter:on
        , customer.getAddress())
        );
    }

    @Test
    void testToDTO() {
        // @formatter:off
        Address address = Address.builder()
            .setStreet1("street1").setStreet2("street2")
            .setCity("city1").setState("state1")
            .setZipcode("zipcode1").setCountry("country1").build();
        // @formatter:on

        Customer customer = new Customer("id1", "firstname1", "lastname1", "telephone1",
                "email1", address);

        CustomerDTO customerDTO = CustomerDTOMapper.toDTO(customer);
        assertEquals("id1", customerDTO.getId());
        assertEquals("firstname1", customer.getFirstname());
        assertEquals("lastname1", customer.getLastname());
        assertEquals("telephone1", customer.getTelephone());
        assertEquals("email1", customer.getEmail());
        assertEquals("street1", customer.getStreet1());
        assertEquals("street2", customer.getStreet2());
        assertEquals("city1", customer.getCity());
        assertEquals("zipcode1", customer.getZipcode());
        assertEquals("state1", customer.getState());
        assertEquals("country1", customer.getCountry());
    }
}
