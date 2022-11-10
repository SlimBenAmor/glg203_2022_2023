package com.yaps.petstore.dao;

import java.io.StringReader;
import java.sql.SQLException;

import org.dbunit.IDatabaseTester;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;

import com.yaps.common.dao.ConnectionManager;


/**
 * Base class for the tests.
 * <p> Note that when we will move to the frameworks specific to Spring and databases,
 * things will be different.
 * <p> the protected getDatabaseTester() field gives access to a IDatabaseTester instance.
 * <p> Gives methods to set up and clear up the DB Unit tests.
 * <p> The connection to the database can be easily accessed ;
 * <p> two methods, {@link #setup()} and {@link #tearDown()} <b>must</b>
 * be called by the actual test classes, respectively as <code>@BeforeEach</code>
 * and <code>@AfterEach</code> code.
 */
public abstract class AbstractDBUnitTestCase {

    protected abstract IDatabaseTester getDatabaseTester();

    protected abstract ConnectionManager getConnectionManager();

    
    /**
     * Call this method in your test in a method annotated with @BeforeEach.
     * @throws Exception
     */
    protected void setup() throws Exception {
        getDatabaseTester().setSetUpOperation(DatabaseOperation.CLEAN_INSERT);       
        getDatabaseTester().setDataSet(buildIDataSet(getStartDatasetContent()));
        getDatabaseTester().onSetup();
        getConnectionManager().open();
    }

    /**
     * Call this method in your test in a method annotated with @AfterEach.
     * @throws Exception
     */
    protected void tearDown() throws Exception {
        getDatabaseTester().onTearDown();
        getConnectionManager().close();
    }

    /**
     * Asserts that the current content of a table is the same as in a user-built dataset.
     * @param expectedDataSet expected values
     * @param tableName the table to compare.
     * @throws Exception
     * @throws SQLException
     */
    protected void assertCurrentTableContentCorrespondsFor(
        IDataSet expectedDataSet,
         String tableName
         ) throws SQLException, Exception {
        ITable expectedTable = expectedDataSet.getTable(tableName);
        IDataSet databaseDataSet = getDatabaseTester().getConnection().createDataSet();
        ITable actualTable = databaseDataSet.getTable(tableName);
        org.dbunit.Assertion.assertEquals(expectedTable, actualTable);    
    }
    /**
     * Returns a String describing the <em>content</em> of the initial database.
     * The String should be something like :
     * <pre>
     *   <person id="3" name="Babbage"/>
     * </pre>
     * if some data is there, or 
     * <pre>
     *   <person/>
     * </pre>
     * if for instance table person is empty.
     * @return
     */
    protected abstract String getStartDatasetContent();

    /**
     * Builds a dataset using an XML description of its content.
     * The string should be something like :
     * <pre>
     *   <person id="3" name="Babbage"/>
     * </pre>
     * if some data is there, or 
     * <pre>
     *   <person/>
     * </pre>
     * @param innerXML
     * @return
     * @throws DataSetException
     */
    protected static IDataSet buildIDataSet(String innerXML) throws DataSetException {
        StringBuilder builder = new StringBuilder();
        builder.append("<?xml version='1.0' encoding='UTF-8'?>\n");
        builder.append("<dataset>\n");
        builder.append(innerXML);
        builder.append("\n</dataset>\n");
        String xml =  builder.toString();
        IDataSet dataset = new FlatXmlDataSetBuilder().build(new StringReader(xml));     
        return dataset;  
    }

    
}
