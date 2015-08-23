//####################################
//  KYLE RUSSELL
//  13831056
//  PDC Project
//####################################

package engine.core;

import com.google.gson.JsonArray;
import engine.config.DatabaseConfig;
import engine.core.database.Query;
import engine.core.loggers.MainLogger;
import engine.models.EmergencyContactModel;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class DataConnector implements AutoCloseable//extends Thread implements AutoCloseable
{  
    public enum QueryType
    {
        MUTATOR,
        ACCESSOR
    }
    
    private Connection conn;
    private DataConnection connection_conifg;
    private String connection_url;
    private ResultSet results;
    private volatile PreparedStatement activeQuery;
    private volatile boolean running = true;
    private volatile boolean fetchingResults = false;
    private QueryType queryType;
    private int i = 0;
    
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
        this.queryType              =   QueryType.ACCESSOR;
        connect();
        
    }
    
    
    //Thread is synced, waits for queries to be added before execution
    //Queries can be added by execute() calls that assign activeQuery and notify
    /*@Override
    public void run()
    {  
        synchronized(this)
        {
          try
          {
              while(running)
              {

                //Execute the active query
                 if(activeQuery != null) onExecute();
                 else if(fetchingResults != true) wait();
             }
        }

        catch(InterruptedException e)
        {
            e.printStackTrace();
            System.out.println("exception " + e.getMessage());
            
           // closeConnection();
        } 
    }
        
    }*/
    
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
            
           // start();
        }

        catch(SQLException e)
        {
            System.out.println("Failed to connect to server, please try again\nError: " + e.getMessage());      
            close();
        }                 
    }
    
    public void startTransaction()
    {
        try { conn.setAutoCommit(false); } 
        catch (SQLException ex) 
        {
          //  System.out.println("[SQL exception] Failed to start transaction, error: " + ex.getMessage());
        }
    }
    
    public void commitTransaction()
    {
        try
        {
            if(!conn.isClosed())
                conn.commit(); 
        
        }
        catch(SQLException e)
        {
          //  System.out.println("[SQL exception] Failed to commit transaction, error: " + e.getMessage());
        }
    }
    
    public void rollbackTransaction()
    {
        try 
        { 
            if(!conn.isClosed() && !conn.getAutoCommit()) 
                conn.rollback(); 
        }
        
        catch(SQLException e)
        {
            //System.out.println("[SQL exception] Failed to rollback transaction, error: " + e.getMessage());
        }
    }
    
    public void finish()
    {
        synchronized(this)
        {
            running = false;
            notify();
        }
    }
    
    public void fetchResults()
    {
        synchronized(this)
        {
            fetchingResults = true;
            notify();
        }
    }
    
    public void setQueryAccessor()
    {
        queryType   =   QueryType.ACCESSOR;
    }
    
    public void setQueryMutator()
    {
        queryType   =   QueryType.MUTATOR;
    }
    
    public QueryType getQueryType()
    {
        return queryType;
    }
    
    //Executes the active query
    //Resets activeQuery after
    private synchronized boolean onExecute()
    {
        if(activeQuery == null) return false;
        
        try
        {
            if(queryType == QueryType.ACCESSOR)
            
                results = activeQuery.executeQuery();      
            
            else
            {
                activeQuery.executeUpdate();
                results =   activeQuery.getGeneratedKeys();
            }

            activeQuery = null;
            return true;
        }
        
        catch(SQLException e)
        {
          //  System.out.println("SQL EXCEPTION: " + e.getMessage());
            closeConnection();
            return false;
        }
    }
    
    //Executes a query that is passed
    //Converted to PreparedStatement then executed
    public boolean execute(String query)
    {
        return execute(createStatement(query));
    }
    
    //Executes and logs a passed query
    //Execution is handled by run and onExecute()
    public boolean execute(PreparedStatement statement)
    {  
       /* synchronized(this)
        {
            activeQuery     =   statement; //Set active query to be next executed
            notify(); //continue - query executed in run by onExecuted()
        }*/
        
        if(statement  == null) return false;
        
        activeQuery =   statement;
        return onExecute();
    }
    
    //Creates a statement from connection
    public PreparedStatement createStatement(String query)
    {
        try
        {
     //       System.out.println(query);
            
            //Logs the attempted query 
            //Logging config is checked in log()
            MainLogger.log(query, MainLogger.DATA_LOGGER);
      
            PreparedStatement statement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            return statement;
        }
        
        catch(SQLException e)
        {
          //  e.printStackTrace();
         //   System.out.println("[SQL Exception] Error creating query - " + e.getMessage());
            closeConnection();
            return null;
        }
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
        //Finish running queries/connections
        //join();
        return results;
    }
    
    public void closeConnection()
    {
        try
        {
            rollbackTransaction();
            if(conn != null && !conn.isClosed()) conn.close();
            if(results != null && !results.isClosed()) results.close();
        }
        
        catch(NullPointerException | SQLException e)
        {
     //       System.out.println("[CONNECTION CLOSE EXCEPTION] " + e.getMessage());
        } 
    }

    //Closes the DataConnector
    //Interrupts the DataConnector thread
    //Closes any opened Connections & ResultSets
    //Must be called on exceptions and when finished
    @Override
    public void close()
    {
        closeConnection();
    }
    
    public static void main(String[] args)
    {
        
       /* EmergencyContactModel emergencyContact  =   new EmergencyContactModel();
            emergencyContact.set("firstname", "test");
            emergencyContact.set("lastname", "test2");
            emergencyContact.set("contact_ph", "asdasdrw43434");
            emergencyContact.set("contact_email", "dasdasd@mgial.com");
            emergencyContact.set("relationship", "test"); */
        
        try(DataConnector conn  =   new DataConnector())
        {
            conn.setQueryMutator();
            conn.execute("INSERT INTO medical (DESCRIPTION, CONTACT_ID) VALUES ('dsfsdfye', 1)");
            ResultSet rs = conn.getResults();
            System.out.println("Next: " + rs.next());
            System.out.println(rs.getInt(1));
            //conn.finish();
            conn.execute("INSERT INTO medical (DESCRIPTION, CONTACT_ID) VALUES ('TEST', 1)");
        }
        
        catch(SQLException e)
       {
     //       e.printStackTrace();
        }
    }
}
