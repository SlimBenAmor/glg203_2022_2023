package com.yaps.petstore.customer.ui;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.yaps.petstore.catalog.service.CatalogService;
import com.yaps.petstore.config.LoggerConfig;
import com.yaps.petstore.customer.dto.CustomerDTO;
import com.yaps.petstore.customer.service.CustomerService;
/**
 * Test class for displaying customers.
 * 
 * 
 * This test class mocks the service. Hence, it doesn't need a database.
 * 
 */
@WebMvcTest
@Import(LoggerConfig.class) // Would not be injected by @WebMvcTest
public class DisplayCustomerServletTest extends AbstractCustomerServletTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    CustomerService customerService;

    @MockBean
    CatalogService catalogService;


    @BeforeEach
    public void beforeEach() {
       super.beforeEach();
    }


    @Test
    public void testSimple() throws Exception {
        CustomerDTO customer10 = map.get("10");
        // Configuration du pseudo service...
        when(customerService.findById("10")).thenReturn(Optional.of(customer10));
        checkDisplayForCustomer(customer10);       
    }

    @Test
    public void testRealistic() throws Exception {
        CustomerDTO customer = map.get("123");
        // Configuration du pseudo service...
        when(customerService.findById("123")).thenReturn(Optional.of(customer));
        checkDisplayForCustomer(customer);       
    }

    @Test
    public void testNotFound() throws Exception {
        String expectedContent = "Pas de client pour";
        mockMvc.perform(get("/customer/view").param("id", "10"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
            .andExpect(content().string(containsString(expectedContent)))
            .andExpect(content().string(containsString("id 10")))
            ;
    }

    private void checkDisplayForCustomer(CustomerDTO customer) throws Exception{
        mockMvc.perform(get("/customer/view").param("id", customer.getId()))
        .andDo(print())   
        .andExpectAll(
                status().isOk(),
                content().contentTypeCompatibleWith(MediaType.TEXT_HTML),
                content().string(containsString(customer.getId())),
                content().string(containsString(customer.getFirstname())),
                content().string(containsString(customer.getLastname())),
                content().string(containsString(customer.getEmail())),
                content().string(containsString(customer.getTelephone())),
                content().string(containsString(customer.getStreet1())),
                content().string(containsString(customer.getStreet2())),
                content().string(containsString(customer.getCity())),
                content().string(containsString(customer.getZipcode())),
                content().string(containsString(customer.getCountry())),
                content().string(containsString(customer.getState()))
            );
    }

}
