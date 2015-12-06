//====================================
//	Kyle Russell
//	jdamvc
//	DataConnector
//====================================

package engine.core;

import engine.config.AppConfig;
import engine.config.DatabaseConfig;
import engine.core.loggers.MainLogger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class DataConnector implements AutoCloseable
{
    //The connectors session query type
    //MUTATOR: includes all DML queries
    //ACCESSOR: non-DML select etc queries
    public enum QueryType
    {
        MUTATOR,
        ACCESSOR
    }
    
    private Connection conn; //Connection obj used in session
    private ConnectionParams connection_conifg; //parameters used to connect
    private String connectionURL; //the connection string used to connect
    private ResultSet results; //The results stored after a mutator query (null after close)
    private volatile PreparedStatement activeQuery; //the current query being operated on
    private QueryType queryType; //The connectors query type (mutator queries: MUTATOR, otherwise ACCESSOR)
    
    //Creates a DataConnector with default config
    public DataConnector()
    {
        this(new ConnectionParams());
    }
    
    //Creates a DataConnector with custom config
    public DataConnector(ConnectionParams config)
    {
        this.connection_conifg      =   config;
        this.connectionURL          =   config.getConnectionString();
        this.queryType              =   QueryType.ACCESSOR;
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
            //Register JDBC driver
            //Uses network driver
            DriverManager.registerDriver(new org.apache.derby.jdbc.ClientDriver());
            
            //Initializes the connection
            //connection_url is created from the active DataConnection
            conn    =   DriverManager.getConnection(connectionURL);
            
            //Sets the active schema
            conn.setSchema(DatabaseConfig.SCHEMA);
        }

        catch(SQLException e)
        {
            ExceptionOutput.output("Failed to connect to Database, application will now shutdown", ExceptionOutput.OutputType.MESSAGE);      
            close();
            
            if(!AppConfig.GUI_MODE)
            {
                try
                {
                    Thread.sleep(2000);
                }

                catch(InterruptedException ex) {}
            }
            
            System.exit(0);
        }                  
    }
    
    //Begins a transaction in the session
    //Upon completion use rollbackTransaction() or commitTransaction()
    public void startTransaction()
    {
        try { conn.setAutoCommit(false); } 
        catch (SQLException ex) {}
    }
    
    //Commits the transaction
    //Has no effect if there is no transaction
    public void commitTransaction()
    {
        try
        {
            if(!conn.isClosed())
                conn.commit(); 
        
        }
        catch(SQLException e)
        {
          ExceptionOutput.output("[SQL exception] Failed to commit transaction, error: " + e.getMessage(), ExceptionOutput.OutputType.DEBUG);
        }
    }
    
    //Rolls back a transaction
    //Any queries executed during the transaction state are undone
    //Useful in catch blocks to prevent inconsistencies
    public void rollbackTransaction()
    {
        try 
        { 
            if(conn != null && !conn.isClosed() && !conn.getAutoCommit()) 
                conn.rollback(); 
        }
        
        catch(SQLException e)
        {
            ExceptionOutput.output("[SQL exception] Failed to rollback transaction, error: " + e.getMessage(), ExceptionOutput.OutputType.DEBUG);
        }
    }
    
    //Sets the query type to ACCESSOR
    //Queries will now be handled as accessor queries
    public void setQueryAccessor()
    {
        queryType   =   QueryType.ACCESSOR;
    }
    
    //Sets teh query type to MUTATOR
    //Queries will be handled as mutator/DML queries
    public void setQueryMutator()
    {
        queryType   =   QueryType.MUTATOR;
    }
    
    //Returns the sessions query type
    //Default is ACCESSOR
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
            //Execute accessor query
            //Results are the fetched rows
            if(queryType == QueryType.ACCESSOR)
                results = activeQuery.executeQuery();      
            
            //Execute mutator/DML query
            //Results are the keys created by the query
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
        if(statement  == null) return false;
        
        activeQuery =   statement;
        return onExecute();
    }
    
    //Creates a statement from connection
    public PreparedStatement createStatement(String query)
    {
        try
        {    
            //Connection closed
            if(conn == null) return null;
            
            //Logs the attempted query 
            //Logging config is checked in log()
            MainLogger.log(query, MainLogger.DATA_LOGGER);
      
            if(AppConfig.DEBUG_MODE)
                ExceptionOutput.output(query, ExceptionOutput.OutputType.DEBUG);
            
            PreparedStatement statement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            return statement;
        }
        
        catch(SQLException e)
        {
            ExceptionOutput.output("[SQL exception] " + e.getMessage(), ExceptionOutput.OutputType.DEBUG);
            closeConnection();
            return null;
        }
    }
    
    //Returns the DataConnectors connection obj
    //Access at own risk and close where necessary
    public Connection getConnection()
    {
        return conn;
    }
    
    //Returns the DataConnectors connection config
    //Can be used to fetch properties of the current connection
    public ConnectionParams getConnectionConfig()
    {
        return connection_conifg;
    }
    
    //Returns the results of previously executed query
    //Handling of results must be used before closure
    public ResultSet getResults()
    {
        return results;
    }
    
    //Closes the DataConnector
    //Closes any opened Connections & ResultSets
    //Must be called on exceptions and when finished
    public void closeConnection()
    {
        try
        {
            rollbackTransaction();
            if(conn != null && !conn.isClosed()) conn.close();
            if(results != null && !results.isClosed()) results.close();
        }
        
        catch(SQLException e) {}
    }

    //Allow automatic closing feature of AutoClosable
    //Active Connections and ResultSets are closed and handled
    @Override
    public void close()
    {
        closeConnection();
    }
}
