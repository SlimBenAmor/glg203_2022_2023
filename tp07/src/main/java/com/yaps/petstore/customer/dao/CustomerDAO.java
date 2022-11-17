package com.yaps.petstore.customer.dao;


import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.yaps.common.dao.AbstractDAO;
import com.yaps.common.dao.exception.YapsDataException;
import com.yaps.petstore.customer.domain.Customer;

@Component
public class CustomerDAO extends AbstractDAO<Customer> {
    
    private static final String TABLENAME = "customer";
    private static final String[] FIELDSNAMES = {
            "firstname",
            "lastname",
            "telephone",
            "email",
            "street1",
            "street2",
            "city",
            "state",
            "zipcode",
            "country"
    };

    public CustomerDAO(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate, TABLENAME, FIELDSNAMES);
    }


    @Override
    protected Object[] getFieldValues(Customer customer) {
       return new Object[] {
            customer.getFirstname(),
            customer.getLastname(),
            customer.getTelephone(),
            customer.getEmail(),
            customer.getStreet1(),
            customer.getStreet2(),
            customer.getCity(),
            customer.getState(),
            customer.getZipcode(),
            customer.getCountry()            
       };
    }


    @Override
    protected Customer extractEntity(ResultSet res) {
        try {
        return new Customer(
            extractString(res, "id"), 
            extractString(res,"firstname"), 
            extractString(res,"lastname"),
            extractString(res,"telephone"), 
            extractString(res,"email"),
            extractString(res,"street1"), 
            extractString(res,"street2"), 
            extractString(res,"city"), 
            extractString(res,"state"), 
            extractString(res,"zipcode"),
            extractString(res,"country"));
        } catch (SQLException e) {
            throw new YapsDataException("error in extraction", e);
        }
    }

}
