package com.yaps.petstore.domain.product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

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
    private static final String COLUMNS[] = { "name", "description", "category_fk" };

    public ProductDAO(Connection connection) {
        super(connection, "product", COLUMNS);
    }


    @Override
    protected void fillPreparedStatement(PreparedStatement pst, Product product, int[] fieldsOrder) throws SQLException {
        pst.setObject(fieldsOrder[0], product.getId());
        pst.setObject(fieldsOrder[1], product.getName());
        pst.setObject(fieldsOrder[2], product.getDescription());
        pst.setObject(fieldsOrder[3], product.getCategory().getId());
    }

    protected Product extractData(ResultSet res) throws SQLException, ObjectNotFoundException {
        String id = res.getString("id");
        String name = res.getString("name");
        String description = res.getString("description");
        String categoryId = res.getString("category_fk");
        CategoryDAO categoryDAO = new CategoryDAO(getConnection());
        Optional<Category> opt = categoryDAO.findById(categoryId);
        if (opt.isPresent()){
            return new Product(id, name, description, opt.get());
        }
        else {
            throw new ObjectNotFoundException();
            // return new Product(id, name, description);
        }
    }

}
