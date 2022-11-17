package com.yaps.petstore.catalog.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.yaps.common.dao.AbstractDAO;
import com.yaps.common.dao.DAO;
import com.yaps.common.dao.exception.YapsDataException;
import com.yaps.petstore.catalog.domain.Category;
import com.yaps.petstore.catalog.domain.Product;

/**
 * This class does all the database access for the class Product.
 *
 * @see Product
 */
@Component
public class ProductDAO extends AbstractDAO<Product> {

    private static final String TABLE = "product";
    private static final String[] FIELDS = { "name", "description", "category_fk" };

    private DAO<Category> categoryDAO;

    protected ProductDAO(JdbcTemplate jdbcTemplate, DAO<Category> categoryDao) {
        super(jdbcTemplate, TABLE, FIELDS);
        this.categoryDAO = categoryDao;
    }

    @Override
    protected Object[] getFieldValues(Product entity) {
        return new Object[] {
                entity.getName(),
                entity.getDescription(),
                entity.getCategory().getId()
        };
    }

    @Override
    protected Product extractEntity(ResultSet res) {
        try {
            String id = extractString(res, "id");
            String name = extractString(res, "name");
            String description = extractString(res, "description");
            String categoryFK = extractString(res, "category_fk");
            Category cat = categoryDAO.findById(categoryFK).orElseThrow();
            return new Product(id, name, description, cat);
        } catch (SQLException e) {
            throw new YapsDataException("error in extraction", e);
        }
    }
}
