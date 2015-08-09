//####################################
//  KYLE RUSSELL
//  13831056
//  PDC Project
//####################################

package engine.core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class DataConnectorTest 
{
    
  /*  @Test
    public void testGetConnection() {
        System.out.println("getConnection");
        DataConnector instance = new DataConnector();
        Connection expResult = null;
        Connection result = instance.getConnection();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }


    @Test
    public void testGetConnectionConfig() {
        System.out.println("getConnectionConfig");
        DataConnector instance = new DataConnector();
        DataConnection expResult = null;
        DataConnection result = instance.getConnectionConfig();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    } */
    
    @Test
    public void testConnect()
    {
        DataConnection conn_params       =   new DataConnection();
        String connection_url            =   conn_params.getConnectionString();
        try(Connection conn              =   DriverManager.getConnection(connection_url))
        {
            System.out.println("CONNECTED - default config");
            
        }
        
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
            fail("FAILED TO CONNECT - default config");
        }
    }
    
    @Test
    public void testInsert()
    {
        try(DataConnector conn  =   new DataConnector())
        {
            conn.insert("App.TestTable", new String[] {"1", "Kyle"});
        }
        
        catch(Exception e)
        {
            System.out.println("test exception - " + e.getMessage());
        }
    }
    
    
}
