package com.yaps.petstore.customerApplication;

import java.sql.Connection;

import com.yaps.petstore.domain.customer.Customer;
import com.yaps.petstore.domain.customer.CustomerDAO;
import com.yaps.utils.crudUI.AbstractEntityManagementUI;
import com.yaps.utils.textUi.Menu;
import com.yaps.utils.textUi.SimpleIO;

public class CustomerApplication  {

    private SimpleIO simpleIO = new SimpleIO();
    
    public void run() {
        new CustomerUI(simpleIO).run();
    }
}