//====================================
//  KYLE RUSSELL
//  13831056
//  PDC Project
//====================================

package engine.core;

import engine.config.DatabaseConfig;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import utilities.TestUtilities;

public class DataConnectorTest 
{
    private ConnectionParams testParams; //Uses the SchoolTestDB params 
    
    @Before
    public void setUp()
    {
        testParams  =   new ConnectionParams(DatabaseConfig.TEST_DATABASE);
    }
    
    //Tests connectivity with raw Connection object
    //Tests both invalid and valid connection params
   @Test
    public void testConnect()
    {
        //Valid connection params
        TestUtilities.formatSubHeader("Attempting connect with Connection");
        ConnectionParams connParams      =   new ConnectionParams();
        String connectionURL             =   connParams.getConnectionString();
        try(Connection conn              =   DriverManager.getConnection(connectionURL))
        {
            System.out.println("Connected");
        }
        
        catch(SQLException e)
        {
            System.out.println("Failed to connect");
            fail("FAILED TO CONNECT - default config");
        }
        
        //Invalid connection params
        TestUtilities.formatSubHeader("Attempting connect with Connection (INVALID URL)");
        String invalidConnURL   =   "invalid url";
        try(Connection conn =   DriverManager.getConnection(invalidConnURL))
        {
            System.out.println("Connected");
            fail();
        }
        
        catch(SQLException e)
        {
            System.out.println("Failed to connect");
        }
    } 
    
    
    //Tests connectivitiy using DataConnector
    //Tests both live and test DBs
   @Test
    public void testDataConnect()
    {
        TestUtilities.formatSubHeader("Attempting connect to live DB with DataConnector");
        try(DataConnector conn  =   new DataConnector())
        {
            System.out.println("Connected");
        } 
        
        TestUtilities.formatSubHeader("Attempting connect to test DB with DataConnector");
        try(DataConnector conn  =   new DataConnector(testParams))
        {
            System.out.println("Connected");
        }
    } 
    
    //Tests executing a query
    //Uses DataConnector and test db
    @Test
    public void testExecute()
    {
        String query    =   "SELECT * FROM Users";
        try(DataConnector conn  =   new DataConnector(testParams))
        {
            boolean result  =   conn.execute(query);
            assertTrue(result);
        }
    }
    
    //Tests executing a DML query 
    //Uses DataConnector and test db
    @Test
    public void testDMLExecute()
    {
        String query    =   "INSERT INTO Role (Name, Description, Permission_level) VALUES(\'test name\', \'test desc\', 2)";
        TestUtilities.formatSubHeader("DML query", query);
        try(DataConnector conn  =   new DataConnector(testParams))
        {
            conn.setQueryMutator();
            boolean result  =   conn.execute(query);
            assertTrue(result);
        }
    }
    
    //Tests using a transaction
    //valid query should insert and rollback after invalidQuery fails
    //Uses DataConnector and test db
    @Test
    public void testTransaction()
    {
        String validQuery       =   "INSERT INTO Role (Name, Description, Permission_level) VALUES(\'test name\', \'test desc\', 2)";
        String invalidQuery     =   "INSERT INTO invalidtable (Name, Description, Permission_level) VALUES(\'test name\', \'test desc\', 2)";
        try(DataConnector conn  =   new DataConnector(testParams))
        {
            conn.setQueryMutator();
            conn.startTransaction();
            
            boolean resultValid     =   conn.execute(validQuery);
            boolean resultInvalid   =   conn.execute(invalidQuery);
            
            if(!resultInvalid)
                conn.rollbackTransaction();
            else
                fail();
            
            assertTrue(resultValid);
        }
    }
    
    
    
    

    
    
}
