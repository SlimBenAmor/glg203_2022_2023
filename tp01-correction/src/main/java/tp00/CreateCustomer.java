package tp00;

/**
 * Un "test" simpliste de Customer.
 */
public class CreateCustomer {
	public static void main(String[] args) {
		Customer customer = new Customer("12345", "Bill", "Gates");
		if ( !customer.getLastname().equals("Gates") ) {
			System.out.println("*** The Customer constructor does not work!");
			System.exit(2);
		}
		else
			System.out.println("Customer " + customer.getFirstname() + " " + customer.getLastname() + " created!");
	}
}
