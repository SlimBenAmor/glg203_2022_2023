package com.yaps.petstore.catalog.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.yaps.common.dao.AbstractDAO;
import com.yaps.common.dao.DAO;
import com.yaps.common.dao.exception.YapsDataException;
import com.yaps.petstore.catalog.domain.Item;
import com.yaps.petstore.catalog.domain.Product;

/**
 * This class does all the database access for the class Item.
 *
 * @see Item
 */
@Component
public class ItemDAO extends AbstractDAO<Item> {

    

    private static final String TABLE = "item";
    private static final String[] FIELDS = { "name", "unit_cost", "image_path", "product_fk" };
    private DAO<Product> productDAO;

    public ItemDAO(JdbcTemplate jdbcTemplate, DAO<Product> productDAO) {
        super(jdbcTemplate, TABLE, FIELDS);
        this.productDAO = productDAO;
    }

    

    @Override
    protected Object[] getFieldValues(Item item) {
        // "name", "unit_cost", "product_fk"
        return new Object[] {
            item.getName(),
            item.getUnitCost(),
            item.getImagePath(),
            item.getProduct().getId()
        };
    }

    @Override
    protected Item extractEntity(ResultSet res) {
        try {
        String id = extractString(res, "id");
        String name = extractString(res, "name");
        double unitCost = res.getDouble("unit_cost");
        String productFK = extractString(res, "product_fk");
        String imagePath = extractString(res, "image_path");
        // Récupération du produit...
        Product product = productDAO.findById(productFK).orElseThrow();
        return new Item(id, name, unitCost, imagePath, product);
        } catch (SQLException e) {
            throw new YapsDataException("error in extraction", e);
        }
    }
}
