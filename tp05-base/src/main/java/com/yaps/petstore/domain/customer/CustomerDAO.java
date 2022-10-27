package com.yaps.petstore.domain.customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.yaps.utils.dao.AbstractDAO;
import com.yaps.utils.dao.exception.DataAccessException;
import com.yaps.utils.dao.exception.DuplicateKeyException;
import com.yaps.utils.dao.exception.ObjectNotFoundException;
import com.yaps.utils.model.CheckException;

public class CustomerDAO extends AbstractDAO<Customer> {

    private static final String TABLENAME = "customer";
    private static final String[] FIELDSNAMES = {
            "firstname",
            "lastname",
            "telephone",
            "street1",
            "street2",
            "city",
            "state",
            "zipcode",
            "country"
    };

    public CustomerDAO(Connection connection) {
        super(connection, TABLENAME, FIELDSNAMES);
    }

    @Override
    public void save(Customer customer) throws DuplicateKeyException, CheckException {
        if (findById(customer.getId()).isEmpty()) {

            customer.checkData();
            String sql = "insert into " + getTableName() + " (id, "
                    + String.join(", ", getFieldsNames())
                    + ") values (?, ?, ?, ?, ? , ?, ?, ?, ?, ?)";

            try (PreparedStatement pst = getConnection().prepareStatement(sql)) {
                int pos = 1;
                pst.setString(pos++, customer.getId());
                pst.setString(pos++, customer.getFirstname());
                pst.setString(pos++, customer.getLastname());
                pst.setString(pos++, customer.getTelephone());
                pst.setString(pos++, customer.getStreet1());
                pst.setString(pos++, customer.getStreet2());
                pst.setString(pos++, customer.getCity());
                pst.setString(pos++, customer.getState());
                pst.setString(pos++, customer.getZipcode());
                pst.setString(pos++, customer.getCountry());
                pst.executeUpdate();
            } catch (SQLException e) {
                throw new DataAccessException(e.getMessage(), e);
            }
        } else {
            throw new DuplicateKeyException();
        }
    }

    @Override
    public void update(Customer customer) throws ObjectNotFoundException, CheckException {
        customer.checkData();
        if (findById(customer.getId()).isEmpty()) {
            throw new ObjectNotFoundException();
        }
        String sql = "update " + getTableName() 
             + " set firstname = ?, lastname = ?, "
             + " telephone = ?,"
             + " street1 = ?, street2 = ?,"
             + " city = ?, state = ?, zipcode=?, country = ?"            
             + " where id = ?";
        try (PreparedStatement pst = getConnection().prepareStatement(sql)) {            
            int pos = 1;
            pst.setString(pos++, customer.getFirstname());
            pst.setString(pos++, customer.getLastname());
            pst.setString(pos++, customer.getTelephone());
            pst.setString(pos++, customer.getStreet1());
            pst.setString(pos++, customer.getStreet2());
            pst.setString(pos++, customer.getCity());
            pst.setString(pos++, customer.getState());
            pst.setString(pos++, customer.getZipcode());
            pst.setString(pos++, customer.getCountry());
            pst.setString(pos++, customer.getId());

            pst.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    @Override
    public Optional<Customer> findById(String id) {
        if (id == null)
            throw new NullPointerException("id should not be null");
        String sql = "select * from " + getTableName() + " where id = ?";
        try (PreparedStatement pst = getConnection().prepareStatement(sql)) {
            pst.setString(1, id);
            try (ResultSet resultSet = pst.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(extractCustomer(resultSet));
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    @Override
    public List<Customer> findAll() {
        String sql = "select * from " + getTableName();
        try (
                Statement st = getConnection().createStatement();
                ResultSet resultSet = st.executeQuery(sql);) {
            List<Customer> result = new ArrayList<>();
            while (resultSet.next()) {
                result.add(extractCustomer(resultSet));
            }
            return result;
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    
    private Customer extractCustomer(ResultSet res) throws SQLException {        
        return new Customer(res.getString("id"), 
            res.getString("firstname"), 
            res.getString("lastname"),
            res.getString("telephone"), res.getString("street1"), res.getString("street2"), 
            res.getString("city"), 
            res.getString("state"), 
            res.getString("zipcode"),
            res.getString("country"));
    }

}
