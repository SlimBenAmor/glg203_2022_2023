package com.yaps.petstore.domain.item;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;
import java.util.List;

import com.yaps.petstore.domain.product.Product;
import com.yaps.petstore.domain.product.ProductDAO;
import com.yaps.utils.dao.AbstractDAO;
import com.yaps.utils.dao.exception.ObjectNotFoundException;

/**
 * This class does all the database access for the class Item.
 *
 * @see Item
 */
public class ItemDAO extends AbstractDAO<Item> {
    public ItemDAO(Connection connection) {
        super(connection, "item", Item.class);
    }

    protected Item extractSpecificData(List<Object> argList) throws ObjectNotFoundException {
        ProductDAO productDAO = new ProductDAO(getConnection());
        Optional<Product> opt = productDAO.findById((String) argList.get(argList.size() - 1));
        if (opt.isPresent()) {
            argList.set(argList.size() - 1, opt.get());
            return new Item((String) argList.get(0), (String) argList.get(1), (double) Double.parseDouble((String)argList.get(2)), (Product) argList.get(3));
        }
        else {
            throw new ObjectNotFoundException();
        }
    }

}
