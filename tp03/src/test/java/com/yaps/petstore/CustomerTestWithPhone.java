package com.yaps.petstore;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

import com.yaps.petstore.exceptions.ValidationException;
import com.yaps.petstore.model.Customer;

public class CustomerTestWithPhone {

    @Test
    public void testWithPhoneOk() throws ValidationException {
        final Customer customer = new Customer("1234", "Niklaus", "Wirth");
        customer.setTelephone("863434");
        customer.checkData();
    }

    @Test
    public void testWithPhoneNotOk() {
        try {

            final Customer customer = new Customer("1234", "Niklaus", "Wirth");
            customer.setTelephone("456a");
            customer.checkData();
            fail("Customer with non numeric phones not be created");
        } catch (ValidationException e) {
            assertEquals("telephone should only contain digits", e.getMessage());
        }
    }

    public void testWithPhoneEmpty() throws ValidationException {
        final Customer customer = new Customer("1234", "Niklaus", "Wirth");
        customer.setTelephone("");
        customer.checkData();
    }
}
