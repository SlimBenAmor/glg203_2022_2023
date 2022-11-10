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
        throw new RuntimeException("Écrivez-moi !!!!!!!");
    }

}
