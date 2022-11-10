package com.yaps.petstore.customerWebApplication.ui;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.yaps.petstore.customerApplication.domain.Address;
import com.yaps.petstore.customerApplication.domain.Customer;
import com.yaps.petstore.customerApplication.service.CustomerService;
import com.yaps.petstore.customerWebApplication.AbstractCustomerServletTest;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class DisplayCustomerServletTest extends AbstractCustomerServletTest {

    @LocalServerPort
    String port;


    @MockBean
    CustomerService customerService;

    @Override
    protected String getPort() {
        return port;
    }
    
    @BeforeEach
    public void beforeEach() {
       super.beforeEach();
    }

    @Test
    public void checkConfig() {
        assertNotNull(port);
    }

    @Test
    public void testSimple() throws Exception {
        Customer customer10 = map.get("10");
        // Configuration du pseudo service...
        when(customerService.findById("10")).thenReturn(Optional.of(customer10));
        checkDisplayForCustomer(customer10);       
    }

    @Test
    public void testRealistic() throws Exception {
        Customer customer = map.get("123");
        // Configuration du pseudo service...
        when(customerService.findById("123")).thenReturn(Optional.of(customer));
        checkDisplayForCustomer(customer);       
    }

    @Test
    public void testNotFound() throws FailingHttpStatusCodeException, MalformedURLException, IOException {
        HtmlPage page = webClient.getPage(getBaseUrl() + "/view?id=" + "10");
        assertAll(
                () -> assertEquals(200, page.getWebResponse().getStatusCode(), "status code should be ok"),
                () -> assertTrue(page.isHtmlPage(), "page should be html"),
                () -> checkContent(page, "no customer for id 10", "error message")
        );
    }

    private void checkDisplayForCustomer(Customer customer) throws FailingHttpStatusCodeException, MalformedURLException, IOException {
        HtmlPage page = webClient.getPage(getBaseUrl() + "/view?id=" + customer.getId());
        assertAll(
                () -> assertEquals(200, page.getWebResponse().getStatusCode(), "status code should be ok"),
                () -> assertTrue(page.isHtmlPage(), "page should be html"),
                () -> checkContent(page, customer.getId(), "id"),
                () -> checkContent(page, customer.getFirstname(), "first name"),
                () -> checkContent(page, customer.getLastname(), "last name"),
                () -> checkContent(page, customer.getTelephone(), "telephone"),
                () -> checkContent(page, customer.getStreet1(), "street 1"),
                () -> checkContent(page, customer.getStreet2(), "street 2"),
                () -> checkContent(page, customer.getCity(), "city"),
                () -> checkContent(page, customer.getZipcode(), "zip code"),
                () -> checkContent(page, customer.getState(), "state"),
                () -> checkContent(page, customer.getState(), "country")
        );
    }

    private void checkContent(HtmlPage page, String expected, String attrName) {
        assertTrue(page.getBody().getTextContent().contains(expected),
                "%s should be displayed as %s".formatted(attrName, expected));
    }

}
