package com.yaps.petstore.domain.customer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.dbunit.DatabaseUnitException;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.operation.DatabaseOperation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.yaps.petstore.AbstractDBUnitTestCase;
import com.yaps.petstore.domain.DefaultContent;
import com.yaps.utils.dao.exception.DuplicateKeyException;
import com.yaps.utils.dao.exception.ObjectNotFoundException;
import com.yaps.utils.model.CheckException;

public class CustomerDAOTest extends AbstractDBUnitTestCase {

    // ---------------------------------------------------------------------------------
    // - Actual code.
    // ---------------------------------------------------------------------------------

    /**
     * tests if findAll returns empty when no Customer
     * 
     * @throws Exception
     */
    @Test
    public void findAllEmpty() throws Exception {
        setupEmptyContent();
        CustomerDAO dao = buildDAO();
        List<Customer> list = dao.findAll();
        assertTrue(list.isEmpty(), "With no Customer, findAll should return an empty list");
    }

    // findAll returns available categories when available
    @Test
    public void findAllSome() throws Exception {
        // test.
        List<Customer> list = buildDAO().findAll();
        // Note that we want to compare sets, not lists (the order is not relevant.)
        // we don't define equals for categories (because they are entities, more on
        // this later)
        // but we want to check if the list is the same as the expected one.
        // hence we use a trick : toString() depends only on content for our
        // categories...
        Set<String> expectedStrings = Arrays
                .stream(DefaultContent.DEFAULT_CUSTOMER)
                .map(c -> c.toString())
                .collect(Collectors.toSet());
        Set<String> computedStrings = list.stream()
                .map(c -> c.toString())
                .collect(Collectors.toSet());
        assertEquals(expectedStrings, computedStrings, "findAll should return all customers");
    }

    /**
     * find object with unknown id fails
     * 
     * @throws Exception
     * @throws SQLException
     * @throws DatabaseUnitException
     * @throws DataSetException
     */
    @Test
    public void testFindUnknowIdFail() throws DataSetException, DatabaseUnitException, SQLException, Exception {
        Optional<Customer> res = buildDAO().findById("x");
        assertTrue(res.isEmpty(), "unknown elements should not be found");
    }

    /**
     * find object with with empty id fails
     * 
     * @throws Exception
     * @throws SQLException
     * @throws DatabaseUnitException
     * @throws DataSetException
     */
    @Test
    public void testFindEmptyIdFail() throws DataSetException, DatabaseUnitException, SQLException, Exception {
        Optional<Customer> res = buildDAO().findById("");
        assertTrue(res.isEmpty(), "empty id elements should not be found");
    }

    /**
     * find object with with null id should throw NPE.
     * (they are most likely a bug in the software)
     * 
     * @throws Exception
     * @throws SQLException
     * @throws DatabaseUnitException
     * @throws DataSetException
     */
    @Test
    public void testFindNullIdFail() throws DataSetException, DatabaseUnitException, SQLException, Exception {
        CustomerDAO dao = buildDAO();
        assertThrows(NullPointerException.class,
                () -> dao.findById(null),
                "null id should lead to NPE.");
    }

    /**
     * Simple insert (with everything ok).
     * 
     * Could be replaced by save() and findById() test...
     * 
     * @throws Exception
     */
    @Test
    public void simpleInsertTest() throws Exception {
        CustomerDAO dao = buildDAO();
        // Code to test..
        Customer newCustomer = new Customer("0", "a", "b",
                "c", "d", "e", "f", "g", "h", "i");
        dao.save(newCustomer);
        // Done !
        // note : l'ordre est important, c'est celui des clefs...
        IDataSet expectedDataSet = buildIDataSet(
                "<customer id='0' firstname='a' lastname='b' telephone='c' street1='d' street2='e' city='f' state='g' zipcode='h' country='i'/>"
                        + getStartDatasetContent());
        IDataSet databaseDataSet = databaseTester.getConnection().createDataSet();
        ITable actualTable = databaseDataSet.getTable("customer");
        ITable expectedTable = expectedDataSet.getTable("customer");
        org.dbunit.Assertion.assertEquals(expectedTable, actualTable);
    }

    // correct multiple insert
    @Test
    public void multipleInsertTest() throws Exception {
        CustomerDAO dao = buildDAO();
        // Code to test..
        Customer newCustomer1 = new Customer("0", "n0", "n0", "", "", "", "", "", "", "");
        dao.save(newCustomer1);
        Customer newCustomer2 = new Customer("x", "nx", "n0", "", "", "", "", "", "", "");
        dao.save(newCustomer2);

        // Done !
        // note : l'ordre est important, c'est celui des clefs...
        IDataSet expectedDataSet = buildIDataSet(
                "<customer id='0' firstname='n0' lastname='n0' telephone='' street1='' street2='' city='' state='' zipcode='' country=''/>"
                        + getStartDatasetContent()
                        + "<customer id='x' firstname='nx' lastname='n0' telephone='' street1='' street2='' city='' state='' zipcode='' country=''/>");
        IDataSet databaseDataSet = databaseTester.getConnection().createDataSet();
        ITable actualTable = databaseDataSet.getTable("customer");
        ITable expectedTable = expectedDataSet.getTable("customer");
        org.dbunit.Assertion.assertEquals(expectedTable, actualTable);
    }

    // insert error if id is known
    @Test
    public void testSaveErrorIfDuplicate() throws Exception {
        CustomerDAO dao = buildDAO();
        assertThrows(DuplicateKeyException.class, () -> {
            dao.save(new Customer("1", "aaaaaa", "bbbbbbb"));
        });
    }

    /**
     * test remove Customer
     * 
     * @throws Exception
     */
    @Test
    public void testRemoveCustomer() throws Exception {
        CustomerDAO dao = buildDAO();
        dao.remove("2");
        IDataSet expectedDataSet = buildIDataSet(
                DefaultContent.CUSTOMER_XML[0]);
        ITable expectedTable = expectedDataSet.getTable("customer");
        IDataSet databaseDataSet = databaseTester.getConnection().createDataSet();
        ITable actualTable = databaseDataSet.getTable("customer");
        org.dbunit.Assertion.assertEquals(expectedTable, actualTable);
    }

    // test remove absent Customer
    @Test
    public void testRemoveAbsentCustomer() throws Exception {
        CustomerDAO dao = buildDAO();
        assertThrows(ObjectNotFoundException.class,
                () -> dao.remove("x"));
        checkNotModified();
    }

    /**
     * test save invalid Customer
     * 
     * @throws Exception
     */
    @Test
    public void testSaveInvalidCustomer() throws Exception {
        CustomerDAO dao = buildDAO();
        assertThrows(CheckException.class,
                () -> dao.save(new Customer("", "a", "b")), "Customer with empty id should not be saved");
        checkNotModified();
    }

    @Test
    public void testSaveInvalidCustomer1() throws Exception {
        CustomerDAO dao = buildDAO();
        assertThrows(CheckException.class,
                () -> dao.save(new Customer("x", "", "b")), "Customer with empty name should not be saved");
        checkNotModified();
    }

    @Test
    public void testSaveInvalidCustomer2() throws Exception {
        CustomerDAO dao = buildDAO();
        assertThrows(CheckException.class,
                () -> dao.save(new Customer("x", "a", "")), "Customer with empty description should not be saved");
        checkNotModified();
    }

    /**
     * test update Customer
     * 
     * @throws Exception
     */
    @Test
    public void testUpdateCustomer() throws Exception {
        CustomerDAO dao = buildDAO();
        dao.update(
                new Customer("2", "m", "n",
                        "o", "p", "q", "r",
                        "s", "t", "u"));
        IDataSet expectedDataSet = buildIDataSet(
                DefaultContent.CUSTOMER_XML[0]
                        +
                        "<customer id='2' firstname='m' lastname='n' telephone='o' street1='p' street2='q' city='r' state='s' zipcode='t' country='u'/>");
        ITable expectedTable = expectedDataSet.getTable("customer");
        IDataSet databaseDataSet = databaseTester.getConnection().createDataSet();
        ITable actualTable = databaseDataSet.getTable("customer");
        org.dbunit.Assertion.assertEquals(expectedTable, actualTable);
    }

    // test update invalid Customer
    @Test
    public void testUpdateEmptyId() throws Exception {
        CustomerDAO dao = buildDAO();
        assertThrows(CheckException.class,
                () -> dao.update(new Customer("", "aa", "bb")),
                "Customer with empty id should not be updated");
        checkNotModified();
    }

    @Test
    public void testUpdateEmptyDescription() throws Exception {
        CustomerDAO dao = buildDAO();
        assertThrows(CheckException.class,
                () -> dao.update(new Customer("a", "aa", "")),
                "Customer with empty description should not be updated");
        checkNotModified();
    }

    @Test
    public void testUpdateEmptyName() throws Exception {
        CustomerDAO dao = buildDAO();
        assertThrows(CheckException.class,
                () -> dao.update(new Customer("a", "", "bb")),
                "Customer with empty name should not be updated");
        checkNotModified();
    }

    @Test
    public void testUpdateNotFound() throws Exception {
        CustomerDAO dao = buildDAO();
        assertThrows(ObjectNotFoundException.class,
                () -> dao.update(new Customer("someid", "aa", "bb")),
                "Customer with unknown id should not be updated");
        checkNotModified();
    }
    // ---------------------------------------------------------------------------------
    // - Setup and teardown code
    // ---------------------------------------------------------------------------------

    @Override
    protected String getStartDatasetContent() {
        return String.join("\n", DefaultContent.CUSTOMER_XML);
    }

    @BeforeEach
    @Override
    protected void setup() throws Exception {
        super.setup();
    }

    @AfterEach
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    // ---------------------------------------------------------------------------------
    // - Utility functions
    // ---------------------------------------------------------------------------------

    /**
     * Creates a DAO.
     * 
     * @return
     * @throws Exception
     */
    private CustomerDAO buildDAO() throws Exception {
        return new CustomerDAO(connection());
    }

    /**
     * Sets the content for this DAO type to EMPTY.
     * 
     * @throws DatabaseUnitException
     * @throws SQLException
     * @throws Exception
     */
    public void setupEmptyContent() throws DatabaseUnitException, SQLException, Exception {
        // Fix the database content for this test.
        String initialContent = """
                <customer/>
                """;
        IDataSet someDataSet = buildIDataSet(initialContent);
        DatabaseOperation.CLEAN_INSERT.execute(databaseTester.getConnection(), someDataSet);
    }

    /**
     * Checks if the current table has still its original content.
     * 
     * @throws Exception
     * @throws SQLException
     */
    private void checkNotModified() throws SQLException, Exception {
        IDataSet expectedDataSet = buildIDataSet(
                getStartDatasetContent());
        assertCurrentTableContentCorrespondsFor(expectedDataSet, "customer");
    }

}
