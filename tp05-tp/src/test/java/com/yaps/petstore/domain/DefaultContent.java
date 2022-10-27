package com.yaps.petstore.domain;

import com.yaps.petstore.customerApplication.domain.Customer;

/**
 * Utility class for holding the content of the default test DB for DBUnit.
 */
public class DefaultContent {

    private DefaultContent() {
    }

    /**
     * The categories found in the default test setting.
     */
    public static final Customer[] DEFAULT_CUSTOMER = {
            new Customer("1", "fn1", "ln1",
                    "1", "s11", "s21", "c1",
                    "s1", "z1", "c1"),
            new Customer("2", "fn2", "ln2",
                    "2", "s12", "s22", "c2",
                    "s2", "z2", "c2")
    };

    /**
     * XML Representation of customers for DB Unit
     */

    public static final String CUSTOMER_XML[] = {
        "<customer id='1' firstname='fn1' lastname='ln1' telephone='1' street1='s11' street2='s21' city='c1' state='s1' zipcode='z1' country='c1'/>",
        "<customer id='2' firstname='fn2' lastname='ln2' telephone='2' street1='s12' street2='s22' city='c2' state='s2' zipcode='z2' country='c2'/>"        
    }; 

    
}
