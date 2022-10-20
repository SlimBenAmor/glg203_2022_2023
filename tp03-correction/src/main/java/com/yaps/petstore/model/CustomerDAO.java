package com.yaps.petstore.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.yaps.petstore.exceptions.ValidationException;
import com.yaps.petstore.exceptions.DuplicateKeyException;
import com.yaps.petstore.exceptions.NotFoundException;

/**
 * This class uses a HashTable to store customer objects in it and serializes the hashmap.
 */

// TODO one day : use persistent storage
public final class CustomerDAO {

    // ======================================
    // =             Attributes             =
    // ======================================
    private Map<String,Customer> map = new HashMap<>();
    private Random random = new Random();


    // ======================================
    // =            Constructors            =
    // ======================================
    public CustomerDAO() {
        // TODO one day : loads from persistent storage
    }
    
    // ======================================
    // =           Business methods         =
    // ======================================
    /**
     * This method gets a Customer object from the HashMap.
     *
     * @param id Customer identifier to be found in the hastable
     * @return Customer the customer object with all its attributs set
     * @throws NotFoundException is thrown if the customer id not found in the hastable
     */
    public Customer find(final String id) throws NotFoundException {
        // If the given id doesn't exist we throw a CustomerNotFoundException
        if (!map.containsKey(id)) {
            throw new NotFoundException();
        }

        return map.get(id);
    }

    /**
     * This method inserts a Customer object into the HashMap and serializes the Hastable on disk.
     *
     * @param customer Customer Object to be inserted into the hastable
     * @throws DuplicateKeyException is thrown when an identical object is already in the hastable
     * @throws ValidationException 
     */
    public void insert(final Customer customer) throws DuplicateKeyException, ValidationException {
        // If the given id already exists we cannot insert the new Customer
        if (map.containsKey(customer.getId())) {
            throw new DuplicateKeyException();
        }
        customer.checkData();
        // Puts the object into the hastable
        map.put(customer.getId(), customer);
    }

    /**
     * This method updates a Customer object of the HashMap and serializes the Hastable on disk.
     *
     * @param customer Customer to be updated from the hastable
     * @throws NotFoundException     is thrown if the customer id not found in the hastable
     * @throws ValidationException 
     */
	public void update(final Customer customer) throws NotFoundException, ValidationException {
		customer.checkData();
		if (!map.containsKey(customer.getId()))
			throw new NotFoundException();
		map.put(customer.getId(), customer);
	}

    /**
     * This method deletes a Customer object from the HashMap and serializes the Hastable on disk.
     *
     * @param id Customer identifier to be deleted from the hastable
     * @throws NotFoundException is thrown if there's a persistent problem
     */
    public void remove(final String id) throws NotFoundException {
        // If the given id does'nt exist we cannot remove the Customer from the hashmap
        if (!map.containsKey(id)) {
            throw new NotFoundException();
        }

        // The object is removed from the hastable
        map.remove(id);
    }
    
    // utilisé uniquement dans les tests... et pas très cohérent
    // avec le reste... à supprimer ???
    
    public int getUniqueId() {
    	int result = 0;
        boolean found = false;
        while (! found) {
            result = random.nextInt(Integer.MAX_VALUE);
            if (! map.containsKey(Integer.toString(result))) {
                found = true;
            }
        }
        return result;
    }


    // ======================================
    // =            Private methods         =
    // ======================================
    /**
     * This method checks that the identifier is valid.
     *
     * A SUPPRIMER
     * 
     * @param id identifier to check
     * @throws ValidationException if the id is invalid
     */
    private void checkId(final String id) throws ValidationException {
        if (id == null || "".equals(id))
            throw new ValidationException("Invalid customer id");
    }
}
