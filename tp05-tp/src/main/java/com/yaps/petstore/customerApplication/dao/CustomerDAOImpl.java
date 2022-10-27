package com.yaps.petstore.customerApplication.dao;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.yaps.common.dao.AbstractDAO;
import com.yaps.common.dao.ConnectionManager;
import com.yaps.common.dao.exception.DataAccessException;
import com.yaps.common.dao.exception.DuplicateKeyException;
import com.yaps.common.dao.exception.ObjectNotFoundException;
import com.yaps.common.model.CheckException;
import com.yaps.petstore.customerApplication.domain.Customer;

@Component
public class CustomerDAOImpl extends AbstractDAO<Customer> {

    Logger logger = LogManager.getLogger(CustomerDAOImpl.class);

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

    public CustomerDAOImpl(ConnectionManager connectionManager) {
        super(connectionManager, TABLENAME, FIELDSNAMES);
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
                logger.info(pst);
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
            logger.info(pst);
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
            logger.info(pst);
            try (ResultSet resultSet = pst.executeQuery()) {
                if (resultSet.next()) {
                    logger.info("found customer %s".formatted(id));
                    return Optional.of(extractCustomer(resultSet));
                } else {
                    logger.info("customer not found %s".formatted(id));
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
            logger.info("found %d customers".formatted(result.size()));
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
