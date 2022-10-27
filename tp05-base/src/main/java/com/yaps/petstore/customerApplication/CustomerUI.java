package com.yaps.petstore.customerApplication;

import java.sql.Connection;

import com.yaps.petstore.domain.customer.Customer;
import com.yaps.petstore.domain.customer.CustomerDAO;
import com.yaps.utils.crudUI.AbstractEntityManagementUI;
import com.yaps.utils.textUi.Menu;
import com.yaps.utils.textUi.SimpleIO;

public class CustomerUI extends AbstractEntityManagementUI<Customer, CustomerDAO> {

    protected CustomerUI(SimpleIO io) {
        super(io, "Customer");
    }

    @Override
    protected CustomerDAO createDAO(Connection connection) {
        return new CustomerDAO(connection);
    }

    @Override
    protected Menu createEditMenu() {
        return new Menu(io)
                .add("n1", "firstname")
                .add("n2", "lastname")
                .add("t", "telephone")
                .add("s1", "street1")
                .add("s2", "street2")
                .add("c1", "city")
                .add("s3", "state")
                .add("z", "zipcode")
                .add("c2", "country");
    }

    @Override
    protected void processEditing(Connection connection, String fieldCode, Customer customer) {
        switch (fieldCode) {
            case "n1": {
                String name = io.askForString("firstname");
                customer.setFirstname(name);
                break;
            }
            case "n2": {
                String name = io.askForString("lastname");
                customer.setLastname(name);
                break;
            }
            case "t": {
                String s = io.askForString("telephone");
                customer.setTelephone(s);
                break;
            }
            case "s1": {
                String s = io.askForString("street1");
                customer.setStreet1(s);
                break;
            }case "s2": {
                String s = io.askForString("street2");
                customer.setStreet2(s);
                break;
            }case "c1": {
                String s = io.askForString("city");
                customer.setCity(s);
                break;
            }case "s3": {
                String s = io.askForString("state");
                customer.setState(s);
                break;
            }case "z": {
                String s = io.askForString("zipcode");
                customer.setZipcode(s);
                break;
            }case "c2": {
                String s = io.askForString("country");
                customer.setCountry(s);
                break;
            }
        }
    }

    @Override
    protected Customer askForEntityData(Connection connection) {
        String firstname = io.askForString("firstname");
        String lastname = io.askForString("lastname");
        String telephone = io.askForString("telephone");
        String street1 = io.askForString("street1");
        String street2 = io.askForString("street2");
        String city = io.askForString("city");
        String state = io.askForString("state");
        String zipcode = io.askForString("zipcode");
        String country = io.askForString("country");
        // updating max id...
        // Should probably move just before the part where we actually save the element ?
        int id = createDAO(connection).getCurrentMaxId() + 1;
        createDAO(connection).setCurrentMaxId(id);
        return new Customer(Integer.toString(id), firstname, lastname, telephone, street1, street2, city, state, zipcode, country);
    }

}
