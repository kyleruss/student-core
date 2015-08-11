//####################################
//  KYLE RUSSELL
//  13831056
//  PDC Project
//####################################

package engine.core;

import engine.core.database.Query;
import engine.core.loggers.MainLogger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DataConnector implements AutoCloseable
{  
    
    private Connection conn;
    private DataConnection connection_conifg;
    private String connection_url;
    private ResultSet results;
    
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
    
    public void execute(String query) throws SQLException
    {
        this.execute(createStatement(query));
    }
    
    public void execute(PreparedStatement statement) throws SQLException
    {
        MainLogger.log(statement.toString(), MainLogger.DATA_LOGGER);
        ResultSet execStatus    =   statement.executeQuery();
        results                 =   execStatus;
    }
    
    public PreparedStatement createStatement(String query) throws SQLException
    {
        System.out.println(query);
        return conn.prepareStatement(query);
    }
    
    public boolean execute(Query query)
    {
        return true;
    }
    
    public boolean executeBatch(Query[] queries)
    {
        return true;
    }
    
    
    public Connection getConnection()
    {
        return conn;
    }
    
    public DataConnection getConnectionConfig()
    {
        return connection_conifg;
    }
    
    public ResultSet getResults()
    {
        return results;
    }

    @Override
    public void close()
    {
        try
        {
            conn.close();
        }
        
        catch(SQLException e)
        {
            System.out.println("[CONNECTION CLOSE EXCEPTION] " + e.getMessage());
        }
    }
}
