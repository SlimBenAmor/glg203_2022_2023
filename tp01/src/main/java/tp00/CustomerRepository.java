package tp00;
import java.util.ArrayList;

public class CustomerRepository {
    private ArrayList <Customer> customersList = new ArrayList <Customer >();

    public Customer find(String id) {
        for (Customer customer : customersList) {
            if (customer.getId().equals(id)) {
                return customer;
            }
        }
        return null;
    }

    public boolean remove(String id) {
        Customer customer = this.find(id);
        return customersList.remove(customer);
    }

    public boolean insert(Customer customer) {
        Customer foundCustomer = this.find(customer.getId());
        if(foundCustomer==null)
            return customersList.add(customer);
        return false;
    }
}
