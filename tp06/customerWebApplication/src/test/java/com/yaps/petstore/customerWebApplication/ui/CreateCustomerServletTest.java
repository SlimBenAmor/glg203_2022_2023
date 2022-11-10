package com.yaps.petstore.customerWebApplication.ui;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.opentest4j.MultipleFailuresError;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.yaps.common.model.CheckException;
import com.yaps.petstore.customerApplication.domain.Customer;
import com.yaps.petstore.customerApplication.service.CustomerService;
import com.yaps.petstore.customerWebApplication.AbstractCustomerServletTest;

/**
 * Tests for Create Customer Servlet.
 * 
 * <p>
 * We had a problem with @MockBean : save() is supposed to throw an error
 * which is in fact thrown by customer.checkData(). This contract does not
 * respect encapsulation rules, and it shows here.
 * <p>
 * Actual integration tests (with the full database) might be somehow easier to
 * perform (the problem being : how to set the whole application in a known
 * state ?).
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class CreateCustomerServletTest extends AbstractCustomerServletTest {

    @LocalServerPort
    String port;

    @MockBean
    CustomerService customerService;

    /**
     * Default Parameters to send in a request.
     * If changes are needed, please clone this list...
     */

    String[] parametersArray = new String[] {
            "firstname", "",
            "lastname", "",
            "telephone", "",
            "street1", "",
            "street2", "",
            "city", "",
            "zipcode", "",
            "state", "",
            "country", "",
    };

    private List<NameValuePair> buildParameterList(String[] rowList) {
        List<NameValuePair> result = new ArrayList<>();
        for (int i = 0; i < rowList.length; i += 2) {
            result.add(new NameValuePair(rowList[i], rowList[i + 1]));
        }
        return result;
    }

    @Override
    protected String getPort() {
        return port;
    }

    @BeforeEach
    public void beforeEach() {
        super.beforeEach();
    }

    @Test
    public void testJustOk() throws CheckException, MalformedURLException, IOException {
        Customer adaCreated = new Customer("23", "Ada", "Lovelace");
        // Configure the service to call checkData on customer and return an id.
        Mockito.when(customerService.save(any(Customer.class))).thenAnswer(
                invocation -> {
                    Customer c = invocation.getArgument(0, Customer.class);
                    c.setId("23"); // Id given by service
                    c.checkData();
                    return "23";
                });
        when(customerService.findById("23")).thenReturn(Optional.of(adaCreated));

        String params[] = parametersArray.clone();
        params[1] = "Ada";
        params[3] = "Lovelace";
        HtmlPage resultPage = sendCreateRequest(params);
        // Note that webclient will traverse redirection and display the final page.
        assertAll(
            () ->  assertEquals(200, resultPage.getWebResponse().getStatusCode()),
            () -> assertEquals(
                "/view",
                resultPage.getUrl().getPath(),
                "Create show redirect to a page displaying the new customer"
            ),
            () -> assertEquals(
                "id=23",
                resultPage.getUrl().getQuery(),
                "Create show redirect to a page displaying the new customer"
            )
        );
        
    }

    /**
     * Check if when submitting a new customer info, the correct service method is called.
     * @throws IOException
     * @throws MalformedURLException
     * @throws CheckException
     */
    @Test
    public void testFullOk() throws MalformedURLException, IOException, CheckException {
        // see https://www.baeldung.com/mockito-verify
        // Check that customerService.save will be called with all values from 
        // the form...
        String[] parametersArray = new String[] {
                "firstname", "p1",
                "lastname", "p2",
                "telephone", "p3",
                "street1", "a1",
                "street2", "a2",
                "city", "a3",
                "zipcode", "a4",
                "state", "a5",
                "country", "a6"
        };

        ArgumentCaptor<Customer> customerCaptor = ArgumentCaptor.forClass(Customer.class);
        sendCreateRequest(parametersArray);
        verify(customerService).save(customerCaptor.capture());
        Customer capturedCustomer = customerCaptor.getValue();
        assertAll(
            () -> assertEquals("p1", capturedCustomer.getFirstname()),
            () -> assertEquals("p2", capturedCustomer.getLastname()),
            () -> assertEquals("p3", capturedCustomer.getTelephone()),
            () -> assertEquals("a1", capturedCustomer.getStreet1()),
            () -> assertEquals("a2", capturedCustomer.getStreet2()),
            () -> assertEquals("a3", capturedCustomer.getCity()),
            () -> assertEquals("a4", capturedCustomer.getZipcode()),
            () -> assertEquals("a5", capturedCustomer.getState()),
            () -> assertEquals("a6", capturedCustomer.getCountry())
        );
    }

    @Test
    public void testBadFirstName() throws FailingHttpStatusCodeException, IOException, CheckException {
        String[] parametersArray = new String[] {
                "firstname", "",
                "lastname", "a",
                "telephone", "a",
                "street1", "a",
                "street2", "a",
                "city", "a",
                "zipcode", "a",
                "state", "a",
                "country", "a"
        };
        checkBadData(parametersArray);
    }

    @Test
    public void testBadLastName() throws MalformedURLException, MultipleFailuresError, CheckException, IOException {
        String[] parametersArray = new String[] {
                "firstname", "a",
                "lastname", "",
                "telephone", "a",
                "street1", "a",
                "street2", "a",
                "city", "a",
                "zipcode", "a",
                "state", "a",
                "country", "a"
        };
        checkBadData(parametersArray);
    }

    /**
     * Takes bad entry and check it's actually bad.
     * 
     * @param parametersArray
     * @throws CheckException
     * @throws MalformedURLException
     * @throws IOException
     * @throws MultipleFailuresError
     */
    private void checkBadData(String[] parametersArray)
            throws CheckException, MalformedURLException, IOException, MultipleFailuresError {
        // Configure the service to call checkData on customer.
        Mockito.when(customerService.save(any(Customer.class))).thenAnswer(
                invocation -> {
                    Customer c = invocation.getArgument(0, Customer.class);
                    c.checkData();
                    return null;
                });
        HtmlPage page = sendCreateRequest(parametersArray);

        String expected = "incorrect input for customer : first name and last names are mandatory";
        assertAll(
                () -> assertEquals(200, page.getWebResponse().getStatusCode(), "status code should be ok"),
                () -> assertTrue(page.isHtmlPage(), "page should be html"));
        String content = page.getBody().getTextContent();
        assertTrue(content.contains(expected),
                "error message expected; actual text is : " + content);
    }

    private HtmlPage sendCreateRequest(String[] parametersArray) throws MalformedURLException, IOException {
        URL url = new URL(getBaseUrl() + "/create");
        WebRequest request = new WebRequest(url, HttpMethod.POST);
        request.setRequestParameters(buildParameterList(parametersArray));
        HtmlPage page = webClient.getPage(request);
        return page;
    }

}
