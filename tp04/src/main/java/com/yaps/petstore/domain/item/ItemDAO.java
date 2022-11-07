package com.yaps.petstore.domain.item;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
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
    public Optional<Item> findById(String id) {
        if (id == null)
            throw new NullPointerException("id should not be null");
        String sql = "select * from " + getTableName() + " where id = ?";
        try (PreparedStatement pst = getConnection().prepareStatement(sql)) {
            pst.setString(1, id);
            try (ResultSet resultSet = pst.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(extractData(resultSet));
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage(), e);
        } catch (ObjectNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Item> findAll() {
        String sql = "select * from " + getTableName();
        try (
                Statement st = getConnection().createStatement();
                ResultSet resultSet = st.executeQuery(sql);) {
            List<Item> result = new ArrayList<>();
            while (resultSet.next()) {
                result.add(extractData(resultSet));
            }
            return result;
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage(), e);
        } catch (ObjectNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(Item item) throws DuplicateKeyException, CheckException {
        item.checkData();
        if (findById(item.getId()).isEmpty()) {
            String sql = "insert into " + getTableName()
                    + " ( id, " + String.join(", ", getFieldsNames()) + ")"
                    + " values (?, ?, ?, ?)";
            try (PreparedStatement pst = getConnection().prepareStatement(sql)) {
                pst.setString(1, item.getId());
                pst.setString(2, item.getName());
                pst.setDouble(3, item.getUnitCost());
                pst.setString(4, item.getProduct().getId());
                pst.executeUpdate();
            } catch (SQLException e) {
                throw new DataAccessException(e.getMessage(), e);
            }
        } else {
            throw new DuplicateKeyException();
        }
    }

    @Override
    public void update(Item item) throws ObjectNotFoundException, CheckException {
        item.checkData();
        if (findById(item.getId()).isEmpty()) {
            throw new ObjectNotFoundException();
        }
        String sql = "update " + getTableName() + " set name = ?, unit_cost = ?, product_fk = ? where id = ?";
        try (PreparedStatement pst = getConnection().prepareStatement(sql)) {            
            pst.setString(1, item.getName());
            pst.setDouble(2, item.getUnitCost());
            pst.setString(3, item.getProduct().getId());
            pst.setString(4, item.getId());
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    private Item extractData(ResultSet res) throws SQLException, ObjectNotFoundException {
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
