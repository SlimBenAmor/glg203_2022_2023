package com.yaps.petstore.customer.ui;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.yaps.common.model.CheckException;
import com.yaps.petstore.customer.dto.CustomerDTO;
import com.yaps.petstore.customer.service.CustomerService;

/**
 * This servlet creates a customer.
 */
@Controller
@RequestMapping("/customer")
public class CreateCustomerController {

    @Autowired
    Logger logger;

    @Autowired
    private CustomerService customerService;

    @GetMapping("/create")
    public String customerForm(Model model) {
        logDebugEnter("customerForm");
        model.addAttribute("customerDTO", new CustomerDTO());
        return "customer/createForm";
    }

    @PostMapping("/create")
    public ModelAndView createCustomerProcess(@ModelAttribute CustomerDTO customerDTO) {
        logDebugEnter("createCustomerProcess");
        try {
            String newId = customerService.save(customerDTO);
            return new ModelAndView("redirect:view", "id", newId);
        } catch (CheckException e) {
            return new ModelAndView("/customer/missingNames");
        }
    }

    /**
     * Log the call of a method - we might like to know if controllers are actually
     * called.
     * 
     * @param methodName
     */
    private void logDebugEnter(String methodName) {
        logger.debug("entering {}", methodName);
    }
}