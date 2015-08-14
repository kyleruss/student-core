//####################################
//  KYLE RUSSELL
//  13831056
//  PDC Project
//####################################

package engine.core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import static org.junit.Assert.*;
import org.junit.Test;

public class DataConnectorTest 
{
    
    //Tests basic connectivity with raw Connection object
   @Test
    public void testConnect()
    {
        DataConnection conn_params       =   new DataConnection();
        String connection_url            =   conn_params.getConnectionString();
        try(Connection conn              =   DriverManager.getConnection(connection_url))
        {
            System.out.println("Successfully connected");
        }
        
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
            fail("FAILED TO CONNECT - default config");
        }
    } 
    
    
    //Connects to DB using DataConnector
   @Test
    public void testDataConnect()
    {
        try(DataConnector conn = new DataConnector()) {} 
    } 
    

    
    
}
