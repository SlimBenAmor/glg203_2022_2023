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
import com.yaps.petstore.customer.config.TestDBConfig;
import com.yaps.testCommon.dao.AbstractDBUnitTestCase;

/**
 * Test categories.
 * Default database content is basically defined by :
 * 
 * <pre>
 *  <category id='a' name='b' description='c'/>
 *  <category id='d' name='e' description='f'/>
 * </pre>
 * 
 * The content of the database can be found in class
 * {@link com.yaps.petstore.domain.DefaultContent}
 * 
 * There is a method to start with empty content if needed.
 */
@SpringBootTest()
//@TestPropertySource(locations = "classpath:application-test.properties")
@ActiveProfiles("test") // active application-test.properties en PLUS de application.properties
public class CategoryDAOTest extends AbstractDBUnitTestCase {
   
    // ---------------------------------------------------------------------------------
    // - Configuration et préparation.
    // ---------------------------------------------------------------------------------

    @Configuration
    @Import({TestDBConfig.class})
    @ComponentScan(basePackageClasses = CategoryDAO.class)
    public static class InnerConfig {                
    }

    @Autowired
    DAO<Category> dao;

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
        List<Category> list = dao.findAll();
        assertTrue(list.isEmpty(), "With no category, findAll should return an empty list");
    }

    // findAll returns available categories when available
    @Test
    public void findAllSome() throws Exception {
        // test.
        List<Category> list = dao.findAll();
        // Note that we want to compare sets, not lists (the order is not relevant.)
        // we don't define equals for categories (because they are entities, more on
        // this later)
        // but we want to check if the list is the same as the expected one.
        // hence we use a trick : toString() depends only on content for our
        // categories...
        Set<String> expectedStrings = Arrays
                .stream(DefaultContent.DEFAULT_CATEGORIES)
                .map(c -> c.toString())
                .collect(Collectors.toSet());
        Set<String> computedStrings = list.stream()
                .map(c -> c.toString())
                .collect(Collectors.toSet());
        assertEquals(expectedStrings, computedStrings, "findAll should return all categories");
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
        Optional<Category> res = dao.findById("x");
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
        Optional<Category> res = dao.findById("");
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
        Category newCategory = new Category("00", "x1", "x2");
        dao.save(newCategory);
        // Done !
        // note : l'ordre est important, c'est celui des clefs...
        IDataSet expectedDataSet = buildIDataSet(
                "<category id='00' name='x1' description='x2'/>"
                        + getStartDatasetContent());
        IDataSet databaseDataSet = databaseTester.getConnection().createDataSet();
        ITable actualTable = databaseDataSet.getTable("category");
        ITable expectedTable = expectedDataSet.getTable("category");
        org.dbunit.Assertion.assertEquals(expectedTable, actualTable);
    }

    // correct multiple insert
    @Test
    public void multipleInsertTest() throws Exception {
        // Code to test..
        Category newCategory1 = new Category("00", "01", "02");
        dao.save(newCategory1);
        Category newCategory2 = new Category("x", "x1", "x2");
        dao.save(newCategory2);

        // Done !
        // note : l'ordre est important, c'est celui des clefs...
        IDataSet expectedDataSet = buildIDataSet(
                "<category id='00' name='01' description='02'/>"
                        + getStartDatasetContent()
                        + "<category id='x' name='x1' description='x2'/>");
        IDataSet databaseDataSet = databaseTester.getConnection().createDataSet();
        ITable actualTable = databaseDataSet.getTable("category");
        ITable expectedTable = expectedDataSet.getTable("category");
        org.dbunit.Assertion.assertEquals(expectedTable, actualTable);
    }

    // insert error if id is known
    @Test
    public void testSaveErrorIfDuplicate() throws Exception {
        assertThrows(DuplicateKeyException.class, () -> {
            dao.save(new Category("a", "aaaaaa", "bbbbbbb"));
        });
    }

    /**
     * test remove category
     * 
     * @throws Exception
     */
    @Test
    public void testRemoveCategory() throws Exception {
        dao.remove("b");
        IDataSet expectedDataSet = buildIDataSet(
                DefaultContent.CATEGORY_XML[0]);
        ITable expectedTable = expectedDataSet.getTable("category");
        IDataSet databaseDataSet = databaseTester.getConnection().createDataSet();
        ITable actualTable = databaseDataSet.getTable("category");
        org.dbunit.Assertion.assertEquals(expectedTable, actualTable);
    }


    // test remove absent category
    @Test
    public void testRemoveAbsentCategory() throws Exception {
        assertThrows(ObjectNotFoundException.class,
                () -> dao.remove("x"));
        checkNotModified();
    }

    /**
     * test save invalid category
     * 
     * @throws Exception
     */
    @Test
    public void testSaveInvalidCategory() throws Exception {
        assertThrows(CheckException.class,
                () -> dao.save(new Category("", "a", "b")), "category with empty id should not be saved");
        checkNotModified();
    }

    @Test
    public void testSaveInvalidCategory1() throws Exception {
        assertThrows(CheckException.class,
                () -> dao.save(new Category("x", "", "b")), "category with empty name should not be saved");
        checkNotModified();
    }

    @Test
    public void testSaveInvalidCategory2() throws Exception {
        assertThrows(CheckException.class,
                () -> dao.save(new Category("x", "a", "")), "category with empty description should not be saved");
        checkNotModified();
    }

    /**
     * test update category
     * 
     * @throws Exception
     */
    @Test
    public void testUpdateCategory() throws Exception {
        dao.update(new Category("b", "newName", "newDescription"));
        IDataSet expectedDataSet = buildIDataSet(
                DefaultContent.CATEGORY_XML[0]
                        + "<category id='b' name='newName' description='newDescription'/>");
        ITable expectedTable = expectedDataSet.getTable("category");
        IDataSet databaseDataSet = databaseTester.getConnection().createDataSet();
        ITable actualTable = databaseDataSet.getTable("category");
        org.dbunit.Assertion.assertEquals(expectedTable, actualTable);
    }

    // test update invalid category
    @Test
    public void testUpdateEmptyId() throws Exception {
        assertThrows(CheckException.class,
                () -> dao.update(new Category("", "aa", "bb")),
                "category with empty id should not be updated");
        checkNotModified();
    }

    @Test
    public void testUpdateEmptyDescription() throws Exception {
        assertThrows(CheckException.class,
                () -> dao.update(new Category("a", "aa", "")),
                "category with empty description should not be updated");
        checkNotModified();
    }

    @Test
    public void testUpdateEmptyName() throws Exception {
        assertThrows(CheckException.class,
                () -> dao.update(new Category("a", "", "bb")),
                "category with empty name should not be updated");
        checkNotModified();
    }

    @Test
    public void testUpdateNotFound() throws Exception {
        assertThrows(ObjectNotFoundException.class,
                () -> dao.update(new Category("someid", "aa", "bb")),
                "category with unknown id should not be updated");
        checkNotModified();
    }
    // ---------------------------------------------------------------------------------
    // - Setup and teardown code
    // ---------------------------------------------------------------------------------

    @Override
    protected String getStartDatasetContent() {
        return String.join("\n", DefaultContent.CATEGORY_XML);
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
        String initialContent = """
                <category/>
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
        assertCurrentTableContentCorrespondsFor(expectedDataSet, "category");
    }

}
