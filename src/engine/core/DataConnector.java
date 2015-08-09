//####################################
//  KYLE RUSSELL
//  13831056
//  PDC Project
//####################################

package engine.core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DataConnector implements AutoCloseable
{  
    
    private Connection conn;
    private DataConnection connection_conifg;
    private String connection_url;
    
    //Creates a DataConnector with default config
    public DataConnector()
    {
        this(new DataConnection());
    }
    
    //Creates a DataConnector with custom config
    public DataConnector(DataConnection db_config)
    {
        this.connection_conifg      =   db_config;
        this.connection_url         =   db_config.getConnectionString();
        connect();
    }
    
    
    //-----------------------------
    //       CONNECT TO DB
    //-----------------------------
    //Returns a Connection obj 
    //- connect_string: pass the appropriate connection string
    //- Sql exception is thrown off to handler
    private void connect()
    {
        try
        {
            DriverManager.registerDriver(new org.apache.derby.jdbc.ClientDriver());
            conn    =   DriverManager.getConnection(connection_url);          
            System.out.println("Connected!");
        }
        
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to connect to DB, conn string: " + connection_url);
        }
    }
    
    public void insert(String table, String[] data)
    {
        try
        {
            PreparedStatement statement =  conn.prepareStatement("INSERT INTO " + table + " VALUES (?, ?)");
            statement.setInt(1, Integer.parseInt(data[0]));
            statement.setString(2, data[1]);
            statement.executeUpdate();
        }
        
        catch(SQLException e)
        {
            System.out.println("sql exception: " + e.getMessage());
        }
    }
    
    public Connection getConnection()
    {
        return conn;
    }
    
    public DataConnection getConnectionConfig()
    {
        return connection_conifg;
    }

    @Override
    public void close() throws Exception 
    {
        conn.close();
    }
}
