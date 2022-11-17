package com.yaps.petstore.customer.ui;


import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.opentest4j.MultipleFailuresError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import com.yaps.common.model.CheckException;
import com.yaps.petstore.catalog.service.CatalogService;
import com.yaps.petstore.config.LoggerConfig;
import com.yaps.petstore.customer.domain.Customer;
import com.yaps.petstore.customer.dto.CustomerDTO;
import com.yaps.petstore.customer.service.CustomerDTOMapper;
import com.yaps.petstore.customer.service.CustomerService;



import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
/**
 * Tests for Create Customer Servlet.
 * 
 * <p>
 * We had a problem with @MockBean : save() is supposed to throw an error
 * which is in fact thrown by customer.checkData(). This contract does not
 * respect encapsulation rules, and it shows here.
 * 
 * <p>
 * Actual integration tests (with the full database) might be somehow easier to
 * perform (the problem being : how to set the whole application in a known
 * state ?).
 * 
 * <p>
 * In this exercice, we keep those tests from the previous stage.
 * They are in a way more correct than the integration tests used elsewhere in
 * this code.
 * 
 * <p>
 * However, it's quite likely that, for the sake of uniformity, we move
 * to a general use of integration tests (and use of the test database).
 * 
 * We could also use MockMVC instead of WebClient.
 * 
 * This test class mocks the service. Hence, it doesn't need a database.
 */
@WebMvcTest
@Import(LoggerConfig.class)
public class CreateCustomerServletTest extends AbstractCustomerServletTest {

    
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    CustomerService customerService;

    @MockBean
    CatalogService catalogService;


    /**
     * Default Parameters to send in a request.
     * If changes are needed, please clone this list...
     */

    String[] parametersArray = new String[] {
            "firstname", "",
            "lastname", "",
            "telephone", "",
            "email", "",
            "street1", "",
            "street2", "",
            "city", "",
            "zipcode", "",
            "state", "",
            "country", "",
    };

    @BeforeEach
    public void beforeEach() {
        super.beforeEach();
    }


    @Test
    public void testJustOk() throws Exception {
        // The new id which will be given...
        final String newId = "23";
        CustomerDTO adaCreated = new CustomerDTO(newId, "Ada", "Lovelace");
        // Configure the service to call checkData on customer and return an id.
        Mockito.when(customerService.save(any(CustomerDTO.class))).thenAnswer(
                invocation -> {
                    CustomerDTO c = invocation.getArgument(0, CustomerDTO.class);
                    Customer c1 = CustomerDTOMapper.fromDTO(c); // somehow clumsy.
                    c1.setId(newId);
                    c1.checkData();
                    return newId;
                });
        when(customerService.findById(newId)).thenReturn(Optional.of(adaCreated));

        // Deactivate JS errors (which don't correspond to reality)
        // webClient.getOptions().setThrowExceptionOnScriptError(false);

        String params[] = parametersArray.clone();
        params[1] = "Ada";
        params[3] = "Lovelace";
        ResultActions pageResult = sendCreateRequest(params);
        
        
        pageResult
            .andDo(print())
            .andExpect(status().is3xxRedirection())
            .andExpectAll(redirectedUrl("view?id=23"))
            ;
    }

    /**
     * Check if when submitting a new customer info, the correct service method is
     * called.
     * 
     * @throws IOException
     * @throws MalformedURLException
     * @throws CheckException
     */
    @Test
    public void testFullOk() throws Exception {
        // see https://www.baeldung.com/mockito-verify
        // Check that customerService.save will be called with all values from
        // the form...
        String[] parametersArray = new String[] {
                "firstname", "p1",
                "lastname", "p2",
                "telephone", "p3",
                "email", "p4",
                "street1", "a1",
                "street2", "a2",
                "city", "a3",
                "zipcode", "a4",
                "state", "a5",
                "country", "a6"
        };

        ArgumentCaptor<CustomerDTO> customerCaptor = ArgumentCaptor.forClass(CustomerDTO.class);
        sendCreateRequest(parametersArray);
        verify(customerService).save(customerCaptor.capture());
        CustomerDTO capturedCustomer = customerCaptor.getValue();
        assertAll(
                () -> assertEquals("p1", capturedCustomer.getFirstname()),
                () -> assertEquals("p2", capturedCustomer.getLastname()),
                () -> assertEquals("p3", capturedCustomer.getTelephone()),
                () -> assertEquals("p4", capturedCustomer.getEmail()),
                () -> assertEquals("a1", capturedCustomer.getStreet1()),
                () -> assertEquals("a2", capturedCustomer.getStreet2()),
                () -> assertEquals("a3", capturedCustomer.getCity()),
                () -> assertEquals("a4", capturedCustomer.getZipcode()),
                () -> assertEquals("a5", capturedCustomer.getState()),
                () -> assertEquals("a6", capturedCustomer.getCountry()));
    }

    @Test
    public void testBadFirstName() throws Exception{
        String[] parametersArray = new String[] {
                "firstname", "",
                "lastname", "a",
                "telephone", "a",
                "email", "a",
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
    public void testBadLastName() throws Exception{
        String[] parametersArray = new String[] {
                "firstname", "a",
                "lastname", "",
                "telephone", "a",
                "email", "a",
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
     * @throws MultipleFailuresError
     * @throws Exception
     */
    private void checkBadData(String[] parametersArray)
            throws MultipleFailuresError, Exception {
        // Configure the service to call checkData on customer.
        Mockito.when(customerService.save(any(CustomerDTO.class))).thenAnswer(
                invocation -> {
                    CustomerDTO c = invocation.getArgument(0, CustomerDTO.class);
                    Customer customer = CustomerDTOMapper.fromDTO(c);
                    customer.setId("23"); // Id given by service
                    customer.checkData();
                    throw new RuntimeException("A check exception should have been thrown");
                });
        ResultActions pageResult = sendCreateRequest(parametersArray);
        
        String expected = "Données incorrected : les champs nom et prénom sont obligatoires";

        pageResult
            .andExpect(status().isOk())
            .andDo(print())
            .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
            .andExpect(content().string(containsString(expected)));
    }

    private ResultActions sendCreateRequest(String[] parametersArray) throws Exception {
        MockHttpServletRequestBuilder request = post("/customer/create");
        for (int i = 0; i < parametersArray.length; i+= 2) {
            request = request.param(parametersArray[i], parametersArray[i+1]);
        }
        return mockMvc.perform(
            request
        );
    }

}
