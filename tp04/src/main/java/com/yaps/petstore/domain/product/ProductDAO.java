package com.yaps.petstore.domain.product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.yaps.petstore.domain.category.Category;
import com.yaps.petstore.domain.category.CategoryDAO;
import com.yaps.utils.dao.AbstractDAO;
import com.yaps.utils.dao.exception.DataAccessException;
import com.yaps.utils.dao.exception.DuplicateKeyException;
import com.yaps.utils.dao.exception.ObjectNotFoundException;
import com.yaps.utils.model.CheckException;

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
    public Optional<Product> findById(String id) {
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
    public List<Product> findAll() {
        String sql = "select * from " + getTableName();
        try (
                Statement st = getConnection().createStatement();
                ResultSet resultSet = st.executeQuery(sql);) {
            List<Product> result = new ArrayList<>();
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
    public void save(Product product) throws DuplicateKeyException, CheckException {
        product.checkData();
        if (findById(product.getId()).isEmpty()) {
            String sql = "insert into " + getTableName()
                    + " ( id, " + String.join(", ", getFieldsNames()) + ")"
                    + " values (?, ?, ?, ?)";
            try (PreparedStatement pst = getConnection().prepareStatement(sql)) {
                pst.setString(1, product.getId());
                pst.setString(2, product.getName());
                pst.setString(3, product.getDescription());
                pst.setString(4, product.getCategory().getId());
                pst.executeUpdate();
            } catch (SQLException e) {
                throw new DuplicateKeyException();
            }
        } else {
            throw new DuplicateKeyException();
        }
    }

    @Override
    public void update(Product product) throws ObjectNotFoundException, CheckException {
        product.checkData();
        if (findById(product.getId()).isEmpty()) {
            throw new ObjectNotFoundException();
        }
        String sql = "update " + getTableName() + " set name = ?, description = ?, category_fk = ? where id = ?";
        try (PreparedStatement pst = getConnection().prepareStatement(sql)) {            
            pst.setString(1, product.getName());
            pst.setString(2, product.getDescription());
            pst.setString(3, product.getCategory().getId());
            pst.setString(4, product.getId());
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    private Product extractData(ResultSet res) throws SQLException, ObjectNotFoundException {
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
