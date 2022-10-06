package tp00;

import java.util.HashMap;
import java.util.Map;

public class CustomerRepository {

    private Map<String, Customer> map = new HashMap<>();

    public Customer find(String id) {
        return map.get(id);
    }

    public boolean insert(Customer c) {
        if (!map.containsKey(c.getId())) {
            map.put(c.getId(), c);
            return true;
        } else {
            return false;
        }
    }

    public boolean remove(String id) {
        if (map.containsKey(id)) {
            map.remove(id);
            return true;
        } else {
            return false;
        }
    }

}
