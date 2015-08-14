//####################################
//  KYLE RUSSELL
//  13831056
//  PDC Project
//####################################

package engine.core;

import engine.config.DatabaseConfig;
import engine.core.database.Query;
import engine.core.loggers.MainLogger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DataConnector extends Thread implements AutoCloseable
{  
    
    private Connection conn;
    private DataConnection connection_conifg;
    private String connection_url;
    private ResultSet results;
    private volatile PreparedStatement activeQuery;
    
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
    
    
    //Thread is synced, waits for queries to be added before execution
    //Queries can be added by execute() calls that assign activeQuery and notify
    @Override
    public void run()
    {  
        try
        {
            synchronized(this)
            {
              
               //Wait for query to be added 
               while(activeQuery == null)
                   this.wait();
               
               //Execute the active query
                onExecute();
            }
        }
        
        catch(InterruptedException | SQLException e)
        {
            System.out.println("exception " + e.getMessage());
            close();
        } 
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
            //Register JDBC driver
            //Uses network driver
            DriverManager.registerDriver(new org.apache.derby.jdbc.ClientDriver());
            
            //Initializes the connection
            //connection_url is created from the active DataConnection
            conn    =   DriverManager.getConnection(connection_url);
            
            //Sets the active schema
            conn.setSchema((String) DatabaseConfig.config().get(DatabaseConfig.SCHEMA_KEY));
            
            System.out.println("Connected!");
            start();
        }

        catch(SQLException e)
        {

            System.out.println(e.getMessage());
            System.out.println("Failed to connect to database");
            close();
        }            
        
    }
    
    //Executes the active query
    //Resets activeQuery after
    private synchronized void onExecute() throws SQLException
    {
        results = activeQuery.executeQuery();
        activeQuery = null;
    }
    
    //Executes a query that is passed
    //Converted to PreparedStatement then executed
    public void execute(String query) throws SQLException
    {
        this.execute(createStatement(query), query);
    }
    
    //Executes and logs a passed query
    //Execution is handled by run and onExecute()
    public void execute(PreparedStatement statement, String query) throws SQLException
    {  
        //Logs the attempted query
        //Logging config is checked in log()
        MainLogger.log(query, MainLogger.DATA_LOGGER);
        synchronized(this)
        {
            activeQuery     =   statement; //Set active query to be next executed
            this.notify(); //continue - query executed in run by onExecuted()
        }
    }
    
    //Creates a statement from connection
    public PreparedStatement createStatement(String query) throws SQLException
    {
        return conn.prepareStatement(query);
    }
    
    //Executes a batch of queries
    public boolean executeBatch(Query[] queries)
    {
        return true;
    }
    
    //Returns the DataConnectors connection obj
    //Access at own risk and close where necessary
    public Connection getConnection()
    {
        return conn;
    }
    
    //Returns the DataConnectors connection config
    //Can be used to fetch properties of the current connection
    public DataConnection getConnectionConfig()
    {
        return connection_conifg;
    }
    
    //Returns the results of previously executed query
    //Handling of results must be used before closure
    public ResultSet getResults()
    {
        try
        {
            //Finish running queries/connections
            join();
            return results;
        }
        
        catch(InterruptedException e)
        {
            return null;
        }
    }

    //Closes the DataConnector
    //Interrupts the DataConnector thread
    //Closes any opened Connections & ResultSets
    //Must be called on exceptions and when finished
    @Override
    public void close()
    {
        try
        {
            interrupt();
            if(conn != null) conn.close();
            if(results != null) results.close();
        }
        
        catch(NullPointerException  | SQLException e)
        {
            System.out.println("[CONNECTION CLOSE EXCEPTION] " + e.getMessage());
        } 
    }
}
