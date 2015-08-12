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
    

    
    
}
