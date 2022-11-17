package com.yaps.petstore.customer.service;

import com.yaps.petstore.customer.domain.Address;
import com.yaps.petstore.customer.domain.Customer;
import com.yaps.petstore.customer.dto.CustomerDTO;

/**
 * Cette classe pourrait être extraite du service, ou ses méthodes pourraient être privées au
 * service. Nous pensons qu'en faire une classe interne rend le service plus lisible, en isolant 
 * cette partie du code, et que d'un autre côté, 
 * c'est le service et lui seul qui en a besoin... pour l'instant.
 */
public class CustomerDTOMapper {
    private CustomerDTOMapper() {}

    public static CustomerDTO toDTO(Customer customer) {
        CustomerDTO dto = new CustomerDTO(customer.getId(), customer.getFirstname(), customer.getLastname());
        dto.setEmail(customer.getEmail());
        dto.setTelephone(customer.getTelephone());
        dto.setStreet1(customer.getStreet1());
        dto.setStreet2(customer.getStreet2());
        dto.setZipcode(customer.getZipcode());
        dto.setCity(customer.getCity());
        dto.setState(customer.getState());
        dto.setCountry(customer.getCountry());
        // result.set
        return dto;            
    }

    public static Customer fromDTO(CustomerDTO customerDto) {
        Address address = Address.builder()
            // @formatter:off
            .setStreet1(customerDto.getStreet1())
            .setStreet2(customerDto.getStreet2())
            .setCity(customerDto.getCity())
            .setZipcode(customerDto.getZipcode())
            .setState(customerDto.getState())
            .setCountry(customerDto.getCountry())
            .build();
            // @formatter:on
        return new Customer(
                customerDto.getId(), 
                    customerDto.getFirstname(), customerDto.getLastname(),  
                     customerDto.getTelephone(), customerDto.getEmail(), address);
    }
}