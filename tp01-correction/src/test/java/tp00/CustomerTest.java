package tp00;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * This class tests the (Big)Customer class
 */
public final class CustomerTest {
	
	private Customer validCustomer;

	@BeforeEach
	public void setUp() {
		// Creates a valid customer
		validCustomer = new Customer("bill000", "Bill", "Gates", "tel", "street1", "street2", "city", "state",
				"zipcode", "country", "bilou@microsoft.fr");
	}

	// ==================================
	// = Test cases =
	// ==================================

	/**
	 * This test tries to create an object with valid values.
	 */
	@Test
	public void testCreateValidCustomer() {
		assertEquals("Bill", validCustomer.getFirstname());
		assertEquals("Gates", validCustomer.getLastname());
		boolean b = validCustomer.checkId();
		assertTrue(b, "Customer id is OK!");
		b = validCustomer.checkData();
		assertTrue(b, "Customer data is OK!");
	}

	@Test
	public void testCreateWithEmptyLastName() {
		Customer customer = new Customer("bill000", "Bill", null);
		boolean b = customer.checkData();
		assertFalse(b, "Customer last name should not be null!");
	}

	@Test
	public void createWithEmptyId() {
		Customer customer = new Customer("", "Bill", "Gates");
		boolean b = customer.checkData();
		assertFalse(b, "Customer id should not be empty!");
	}

	@Test
	public void createWithNullId() {
		Customer customer = new Customer(null, "Bill", "Gates");
		boolean b = customer.checkData();
		assertFalse(b, "Customer id should not be null!");		
	}

	@Test
	public void createWithEmptyFirstName() {
		Customer customer = new Customer("bill000", "", "Gates");
		boolean b = customer.checkData();
		assertFalse(b, "Customer first name should not be empty!");				
	}

	@Test
	public void messageForEmptyId() {
		Customer customer = new Customer("", "Bill", "Gates");
		String s = customer.getCheckDataError();
		assertEquals("Invalid id", s);
	}

	@Test
	public void messageForNullId() {
		Customer customer = new Customer(null, "Bill", "Gates");
		String s = customer.getCheckDataError();
		assertEquals("Invalid id", s);
	}

	@Test
	public void messageForEmptyFirstName() {
		Customer customer = new Customer("bill000", "", "Gates");
		String s = customer.getCheckDataError();
		assertEquals("Invalid first name", s);
	}

	@Test
	public void messageForEmptyLastName() {
		Customer customer = new Customer("bill000", "Bill", "");
		String s = customer.getCheckDataError();
		assertEquals("Invalid last name", s);
	}


	/**
	 * This test use each getter
	 * (we should in theory write one test for each getter)
	 */
	@Test
	public void testAllGetters() {
		assertEquals("bill000", validCustomer.getId());
		assertEquals("Bill", validCustomer.getFirstname());
		assertEquals("Gates", validCustomer.getLastname());
		assertEquals("tel", validCustomer.getTelephone());
		assertEquals("street1", validCustomer.getStreet1());
		assertEquals("street2", validCustomer.getStreet2());
		assertEquals("city", validCustomer.getCity());
		assertEquals("state", validCustomer.getState());
		assertEquals("zipcode", validCustomer.getZipcode());
		assertEquals("country", validCustomer.getCountry());
		assertEquals("bilou@microsoft.fr", validCustomer.getMail());
	}

	/**
	 * This test use each setter.
	 * (we should in theory write one test for each setter)
	 */
	@Test
	public void testAllSetters() {
		validCustomer.setFirstname("Bill2");
		validCustomer.setLastname("Gates2");
		validCustomer.setTelephone("tel2");
		validCustomer.setStreet1("street12");
		validCustomer.setStreet2("street22");
		validCustomer.setCity("city2");
		validCustomer.setState("state2");
		validCustomer.setZipcode("zipcode2");
		validCustomer.setCountry("country2");
		validCustomer.setMail("anotherMail");
		assertEquals("Bill2", validCustomer.getFirstname());
		assertEquals("Gates2", validCustomer.getLastname());
		assertEquals("tel2", validCustomer.getTelephone());
		assertEquals("street12", validCustomer.getStreet1());
		assertEquals("street22", validCustomer.getStreet2());
		assertEquals("city2", validCustomer.getCity());
		assertEquals("state2", validCustomer.getState());
		assertEquals("zipcode2", validCustomer.getZipcode());
		assertEquals("country2", validCustomer.getCountry());
		assertEquals("anotherMail", validCustomer.getMail());
	}

	/**
	 * The following tests mail contents
	 */
	@Test
	public void testCheckMailWithValidLength() {
		Customer customer = new Customer("6", "aaa", "bbb");
		customer.setMail("toto@gmail.com");
		boolean b = customer.checkMail();
		assertEquals(true, b, customer.getMail() + " est une adresse de longueur valide");
	}

	@Test
	public void testCheckMailWithEmptyTo() {
		Customer customer = validCustomer;
		customer.setMail("@x.fr");
		boolean b = customer.checkMail();
		assertEquals(false, b, "le mail devrait avoir un destinataire => Echec");
	}

	@Test
	public void testCheckMailWithArrobas() {
		Customer customer = validCustomer;
		boolean b = customer.checkMail();
		assertEquals(true, b);
	}

	@Test
	public void testCheckMailWithoutArrobas() {
		Customer customer = validCustomer;
		customer.setMail("nobody.nowhere");
		boolean b = customer.checkMail();
		assertEquals(false, b, "Adresse sans @ => Echec");
	}

	@Test
	public void testCheckMailsWithValidDomain() {
		Customer customer = validCustomer;
		boolean b = customer.checkMail();
		assertEquals(true, b, "Adresse should be OK!");
		customer.setMail("anyone@cnam.fr");
		b = customer.checkMail();
		assertEquals(true, b, "Adresse should be OK!");
	}

	@Test
	public void testCheckMailsWithInvalidDomain() {
		Customer customer = validCustomer;
		customer.setMail("nobody@g");
		boolean b = customer.checkMail();
		assertFalse(b, "domain should contain a . !");		
	}

}
