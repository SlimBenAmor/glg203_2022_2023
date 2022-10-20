package com.yaps.petstore;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

import com.yaps.petstore.exceptions.ValidationException;
import com.yaps.petstore.model.Customer;

/**
 * This class tests the Customer class
 */
public final class CustomerTest  {

	/**
	 * This test tries to create an object with valid values.
	 */
	@Test
	public void testCreateValidCustomer() {
		// Creates a valid customer
		try {
			final Customer customer = new Customer("bill000", "Bill", "Gates");
			assertEquals("Bill", customer.getFirstname());
			assertEquals("Gates", customer.getLastname());
			customer.checkData();
		} catch (ValidationException e) {
			fail("Custumer data is OK!");
		}
	}

	/**
	 * This test tries to create an object with invalid values.
	 * TODO : one test by case !!!
	 */
	@Test
	public void testCreateCustomerWithInvalidValues() throws Exception {
		// Creates objects with empty values
		try {
			final Customer customer = new Customer("1234", "", "Gates");
			customer.checkData();
			fail("Object with empty values should not be created");
		} catch (ValidationException e) {
			assertEquals("Invalid customer first name", e.getMessage());
		}
		try {
			final Customer customer = new Customer("1234", "Bill", "");
			customer.checkData();
			fail("Object with empty values should not be created");
		} catch (ValidationException e) {
			assertEquals("Invalid customer last name", e.getMessage());
		}

		// Creates objects with null values
		try {
			final Customer customer = new Customer("1234", null, "Gates");
			customer.checkData();
			fail("Object with null values should not be created");
		} catch (ValidationException e) {
			assertEquals("Invalid customer first name", e.getMessage());
		}
		try {
			final Customer customer = new Customer("1234", "Bill", null);
			customer.checkData();
			fail("Object with null values should not be created");
		} catch (ValidationException e) {
			assertEquals("Invalid customer last name", e.getMessage());
		}
	}

}
