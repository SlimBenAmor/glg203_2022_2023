package com.yaps.petstore.domain.item;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import com.yaps.petstore.domain.product.Product;
import com.yaps.petstore.domain.product.ProductDAO;
import com.yaps.utils.dao.AbstractDAO;
import com.yaps.utils.dao.exception.DataAccessException;
import com.yaps.utils.dao.exception.DuplicateKeyException;
import com.yaps.utils.dao.exception.ObjectNotFoundException;
import com.yaps.utils.model.CheckException;

/**
 * This class does all the database access for the class Item.
 *
 * @see Item
 */
public class ItemDAO extends AbstractDAO<Item> {
    private static final String COLUMNS[] = { "name", "unit_cost", "product_fk" };

    public ItemDAO(Connection connection) {
        super(connection, "item", COLUMNS);
    }

    @Override
    protected void fillPreparedStatement(PreparedStatement pst, Item item, int[] fieldsOrder) throws SQLException {
        pst.setString(fieldsOrder[0], item.getId());
        pst.setString(fieldsOrder[1], item.getName());
        pst.setDouble(fieldsOrder[2], item.getUnitCost());
        pst.setString(fieldsOrder[3], item.getProduct().getId());
    }

    protected Item extractData(ResultSet res) throws SQLException, ObjectNotFoundException {
        String id = res.getString("id");
        String name = res.getString("name");
        double unitCost = res.getDouble("unit_cost");
        String productId = res.getString("product_fk");
        ProductDAO productDAO = new ProductDAO(getConnection());
        Optional<Product> opt = productDAO.findById(productId);
        if (opt.isPresent()){
            return new Item(id, name, unitCost, opt.get());
        }
        else {
            throw new ObjectNotFoundException();
        }
    }

}
