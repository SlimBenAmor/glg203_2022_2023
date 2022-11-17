package com.yaps.petstore.customerWebApplication.ui;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.owasp.encoder.Encode;
import org.springframework.beans.factory.annotation.Autowired;

import com.yaps.petstore.customerApplication.domain.Customer;
import com.yaps.petstore.customerApplication.service.CustomerService;

@WebServlet("/view")
public class DisplayCustomerServlet extends HttpServlet {

    Logger logger = LogManager.getLogger(DisplayCustomerServlet.class);

    @Autowired
    CustomerService customerService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            resp.setContentType("text/html");
            String id = req.getParameter("id");
            if (id == null || "".equals(id)) {
                resp.getWriter().write("<b>missing id, sorry.</b>");
            } else {
                PrintWriter writer = resp.getWriter();
                Optional<Customer> customer = customerService.findById(id);
                customer.ifPresentOrElse(c -> {
                    writeCustomer(writer, c);
                }, () -> {
                    writer.write("<b>sorry, no customer for id " + id + "</b>");
                });
            }
        } catch (Exception e) {
            // We are at top level.
            // if this servlet fails, the rest of the software might still be valid
            // (and anyway, it will run whatever we do)
            // errors should be listed somewhere (for developpers to see them)
            // but sending them to users might lead to valuable information leaking.
            // Hence the use of the logger.
            logger.error("Unexpected error", e);
        }

    }

    private void writeCustomer(PrintWriter writer, Customer c) {
        writer.write("<body>\n");
        writer.write("<ul>\n");
        String[] data = {
                "id", c.getId(),
                "first name", c.getFirstname(),
                "last name", c.getLastname(),
                "telephone", c.getTelephone(),
                "street 1", c.getStreet1(),
                "street 2", c.getStreet2(),
                "city", c.getCity(),
                "zip code", c.getZipcode(),
                "state", c.getState(),
                "country", c.getCountry()
        };
        for (int i = 0; i < data.length; i += 2) {
            writer.write("<li><b>");
            writer.write(Encode.forHtml(data[i]));
            writer.write("</b> : ");
            writer.write(Encode.forHtml(data[i + 1]));
            writer.write("</li>\n");
        }
    }
}
