package com.yaps.petstore.domain.product;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;
import java.util.List;

import com.yaps.petstore.domain.category.Category;
import com.yaps.petstore.domain.category.CategoryDAO;
import com.yaps.utils.dao.AbstractDAO;
import com.yaps.utils.dao.exception.ObjectNotFoundException;

/**
 * This class does all the database access for the class Product.
 *
 * @see Product
 */
public class ProductDAO extends AbstractDAO<Product> {
    public ProductDAO(Connection connection) {
        super(connection, "product", Product.class);
    }

    protected Product extractSpecificData(List<Object> argList) throws ObjectNotFoundException {
        CategoryDAO categoryDAO = new CategoryDAO(getConnection());
        Optional<Category> opt = categoryDAO.findById((String) argList.get(argList.size() - 1));
        if (opt.isPresent()) {
            argList.set(argList.size() - 1, opt.get());
            return new Product((String) argList.get(0), (String) argList.get(1), (String) argList.get(2), (Category) argList.get(3));
        }
        else {
            throw new ObjectNotFoundException();
        }
    }

}
