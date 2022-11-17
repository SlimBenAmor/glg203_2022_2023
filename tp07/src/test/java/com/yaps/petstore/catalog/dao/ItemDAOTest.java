package com.yaps.petstore.catalog.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.dbunit.DatabaseUnitException;
import org.dbunit.IDatabaseTester;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.operation.DatabaseOperation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import com.yaps.common.dao.DAO;
import com.yaps.common.dao.exception.DuplicateKeyException;
import com.yaps.common.dao.exception.ObjectNotFoundException;
import com.yaps.common.model.CheckException;
import com.yaps.petstore.catalog.domain.Category;
import com.yaps.petstore.catalog.domain.Item;
import com.yaps.petstore.catalog.domain.Product;
import com.yaps.petstore.customer.config.TestDBConfig;
import com.yaps.testCommon.dao.AbstractDBUnitTestCase;

import static com.yaps.petstore.catalog.dao.DefaultContent.*;

@SpringBootTest()
//@TestPropertySource(locations = "classpath:application-test.properties")
@ActiveProfiles("test") // active application-test.properties en PLUS de application.properties
public class ItemDAOTest extends AbstractDBUnitTestCase {

    // ---------------------------------------------------------------------------------
    // - Configuration et préparation.
    // ---------------------------------------------------------------------------------

    @Configuration
    @Import({TestDBConfig.class})
    @ComponentScan(basePackageClasses = ItemDAO.class)
    public static class InnerConfig {                
    }

    @Autowired
    DAO<Item> dao;

    @Autowired
    DAO<Category> categoryDao;

    @Autowired
    DAO<Product> productDao;

    @Autowired
    IDatabaseTester databaseTester;

    @Autowired
    DataSource dataSource;

    @Override
    protected IDatabaseTester getDatabaseTester() {
        return databaseTester;
    }
    
    
    @Override
    protected DataSource getDataSource() {
        return dataSource;
    }

    // ---------------------------------------------------------------------------------
    // - Actual code.
    // ---------------------------------------------------------------------------------

    /**
     * tests if findAll returns empty when no category
     * 
     * @throws Exception
     */
    @Test
    public void findAllEmpty() throws Exception {
        setupEmptyContent();        
        List<Item> list = dao.findAll();
        assertTrue(list.isEmpty(), "With no Item, findAll should return an empty list");
    }

    // findAll returns available categories when available
    @Test
    public void findAllSome() throws Exception {
        // test.
        List<Item> list = dao.findAll();
        // Note that we want to compare sets, not lists (the order is not relevant.)
        // we don't define equals for categories (because they are entities, more on
        // this later)
        // but we want to check if the list is the same as the expected one.
        // hence we use a trick : toString() depends only on content for our
        // products...

        Set<String> expectedStrings = Arrays.stream(DEFAULT_ITEMS)
                .map(p -> p.toString())
                .collect(Collectors.toSet());

        Set<String> computedStrings = list.stream().map(c -> c.toString()).collect(Collectors.toSet());
        assertEquals(expectedStrings, computedStrings,
                "findAll should return all items");
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
        Optional<Item> res = dao.findById("x");
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
        Optional<Item> res = dao.findById("");
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
        // Code to test..
        Item newItem = new Item("00i", "name of 00i", 1000, "image1", DEFAULT_PRODUCTS[2]);
        dao.save(newItem);
        // Done !
        // note : l'ordre est important, c'est celui des clefs...
        IDataSet expectedDataSet = buildIDataSet(
                "<item id='00i' name='name of 00i' unit_cost='1000' product_fk='pc' image_path='image1'/>"
                        + getStartDatasetContent());
        assertCurrentTableContentCorrespondsFor(expectedDataSet, "item");
    }

    // correct multiple insert
    @Test
    public void multipleInsertTest() throws Exception {
        // Code to test..
        Item newItem1 = new Item("00i", "name of 00i", 1000, "image1", DEFAULT_PRODUCTS[2]);
        dao.save(newItem1);
        Item newItem2 = new Item("x00", "name of 00i", 1000, "image2", DEFAULT_PRODUCTS[2]);
        dao.save(newItem2);

        // Done !
        // note : l'ordre est important, c'est celui des clefs...
        IDataSet expectedDataSet = buildIDataSet(
                "<item id='00i' name='name of 00i' unit_cost='1000' product_fk='pc' image_path='image1'/>"
                        + getStartDatasetContent()
                        + "<item id='x00' name='name of 00i' unit_cost='1000' product_fk='pc' image_path='image2'/>");
        IDataSet databaseDataSet = databaseTester.getConnection().createDataSet();
        ITable actualTable = databaseDataSet.getTable("item");
        ITable expectedTable = expectedDataSet.getTable("item");
        org.dbunit.Assertion.assertEquals(expectedTable, actualTable);
    }

    // insert error if id is known
    @Test
    public void testSaveErrorIfDuplicate() throws Exception {
        assertThrows(DuplicateKeyException.class, () -> {
            dao.save(new Item("ia", "aaaaaa", 10, "image", DEFAULT_PRODUCTS[0]));
        });
    }

    /**
     * test remove Product
     * 
     * @throws Exception
     */
    @Test
    public void testRemoveProduct() throws Exception {
        dao.remove("ic");
        IDataSet expectedDataSet = buildIDataSet(
                ITEM_XML[0] + ITEM_XML[1]);
        ITable expectedTable = expectedDataSet.getTable("item");
        IDataSet databaseDataSet = databaseTester.getConnection().createDataSet();
        ITable actualTable = databaseDataSet.getTable("item");
        org.dbunit.Assertion.assertEquals(expectedTable, actualTable);
    }

    // test remove absent Product
    @Test
    public void testRemoveAbsentProduct() throws Exception {
        assertThrows(ObjectNotFoundException.class,
                () -> dao.remove("x"));
        checkNotModified();
    }

    /**
     * test save invalid Product
     * 
     * @throws Exception
     */
    @Test
    public void testSaveInvalidProduct() throws Exception {
        assertThrows(CheckException.class,
                () -> dao.save(new Item("", "a", 10, "image1", DEFAULT_PRODUCTS[0])),
                "items with empty id should not be saved");
        checkNotModified();
    }

    @Test
    public void testSaveInvalidProduct1() throws Exception {
        assertThrows(CheckException.class,
                () -> dao.save(new Item("dsfds", "", 10, "image1", DEFAULT_PRODUCTS[0])),
                "Product with empty name should not be saved");
        checkNotModified();
    }

    /**
     * test update Product
     * 
     * @throws Exception
     */
    @Test
    public void testUpdateProduct() throws Exception {
        dao.update(new Item("ib", "newName", 300, "image1", DEFAULT_PRODUCTS[2]));
        IDataSet expectedDataSet = buildIDataSet(
                ITEM_XML[0]
                        + "<item id='ib' name ='newName' unit_cost='300' product_fk='pc' image_path='image1'/>"
                        + ITEM_XML[2]);
        ITable expectedTable = expectedDataSet.getTable("item");
        IDataSet databaseDataSet = databaseTester.getConnection().createDataSet();
        ITable actualTable = databaseDataSet.getTable("item");
        org.dbunit.Assertion.assertEquals(expectedTable, actualTable);
    }

    // test update invalid Product
    @Test
    public void testUpdateEmptyId() throws Exception {
        assertThrows(CheckException.class,
                () -> dao.update(new Item("", "newName", 300, "image1", DEFAULT_PRODUCTS[2])),
                "items with empty id should not be updated");
        checkNotModified();
    }

    @Test
    public void testUpdateEmptyDescription() throws Exception {
        assertThrows(CheckException.class,
                () -> dao.update(new Item("ib", "", 300, "image1", DEFAULT_PRODUCTS[2])),
                "Items with empty names should not be updated");
        checkNotModified();
    }

    @Test
    public void testUpdateNotFound() throws Exception {
        assertThrows(ObjectNotFoundException.class,
                () -> dao.update(new Item("ibbdbbb", "newName", 300,"image1", DEFAULT_PRODUCTS[2])),
                "Product with unknown id should not be updated");
        checkNotModified();
    }


    /**
     * Test cascade deletion (made by MySQL, not by us).
     * This code must be in ItemDAOTest, because it's the only class where
     * the whole data is configured.
     * @throws Exception
     */
    @Test
    public void testCascadeRemove() throws Exception {
        // As the SQL states it, removing a category will remove the products and
        // the items which use it. We test this.
        // Category 1 is used by product 1 which is used by item 0.
        categoryDao.remove(DEFAULT_CATEGORIES[1].getId());
        assertTrue(productDao.findById(
            DEFAULT_PRODUCTS[1].getId()).isEmpty());
        assertTrue(dao.findById(
                DEFAULT_ITEMS[0].getId()).isEmpty());
    }
    // ---------------------------------------------------------------------------------
    // - Setup and teardown code
    // ---------------------------------------------------------------------------------
    @Override
    protected String getStartDatasetContent() {
        return String.join(" ", CATEGORY_XML) +
                String.join(" ", PRODUCT_XML) +
                String.join(" ", ITEM_XML);
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
     * Sets the content for this DAO type to EMPTY.
     * 
     * @throws DatabaseUnitException
     * @throws SQLException
     * @throws Exception
     */
    public void setupEmptyContent() throws DatabaseUnitException, SQLException, Exception {
        // Fix the database content for this test.
        String initialContent = "<item/>";
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
        // check on category is not very useful, as there is no actual
        // reason for categories to be modified by items.
        assertCurrentTableContentCorrespondsFor(expectedDataSet, "category");
        assertCurrentTableContentCorrespondsFor(expectedDataSet, "product");
        assertCurrentTableContentCorrespondsFor(expectedDataSet, "item");
    }

}
