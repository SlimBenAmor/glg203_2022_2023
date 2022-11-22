package com.yaps.petstore.customerWebApplication.ui;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.yaps.common.model.CheckException;
import com.yaps.petstore.customerApplication.domain.Address;
import com.yaps.petstore.customerApplication.domain.Customer;
import com.yaps.petstore.customerApplication.service.CustomerService;

@WebServlet("/create")
public class CreateCustomerServlet extends HttpServlet {

    Logger logger = LogManager.getLogger(CreateCustomerServlet.class);

    @Autowired
    CustomerService customerService;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String firstname = req.getParameter("firstname");
        String lastname = req.getParameter("lastname");
        String telephone = req.getParameter("telephone");
        String street1 = req.getParameter("street1");
        String street2 = req.getParameter("street2");
        String city = req.getParameter("city");
        String zipcode = req.getParameter("zipcode");
        String state = req.getParameter("state");
        String country = req.getParameter("country");
        // Customer customer = new Customer("", firstname, lastname, telephone, street1, street2, city, state, zipcode, country);
        Address address = Address.builder().setStreet1(street1).setStreet2(street2).setCity(city).setState(state).setZipcode(zipcode).setCountry(country).build();
        Customer customer = new Customer("", firstname, lastname, telephone, address);
        PrintWriter writer = resp.getWriter();
        try{
            String id = customerService.save(customer);
            resp.sendRedirect(req.getContextPath() + "/view?id="+id);
        } catch (CheckException e){
            writer.write("<b>incorrect input for customer : first name and last names are mandatory</b>");
        }
    }

}
