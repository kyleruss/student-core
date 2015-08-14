//####################################
//  KYLE RUSSELL
//  13831056
//  PDC Project
//####################################

package engine.Models;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import engine.Parsers.JsonParser;
import engine.config.DatabaseConfig;
import engine.core.DataConnector;
import engine.core.database.Conditional;
import engine.core.database.QueryBuilder;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


//------------------------------------------
//                  MODEL
//------------------------------------------
//- Model object class 
//- Maps database tables to objects
//- Model entity represent tables in the database
//- Model objects represent rows/records
//- Model behavior coresponds to it's table counterpart
public abstract class Model 
{
    //The models mapping table
    protected String table;
    
    //The table's primary key column name
    //For default see DatabaseConfig - typically "ID" column
    protected String primaryKey = (String) DatabaseConfig.config().get("DEFAULT_PRIMARY");
    
    //The columns in the table
    //Columns are fetched on initialization by initColumns()
    protected List<String> columns;
    
    //The table's row data
    //Allows for mutation such as insertion, deletion and editing
    private final Map<String, Object> data;
    
    //Create a new model that does not identify a row
    //Data will be empty and records must be added to model before mutation
    public Model()
    {
        data    =   new HashMap<>();
        columns =   new ArrayList<>();
        initTable();
    }
    
    //Create model that maps to an existing row
    //Pass the row primary key value
    //Row data fills the model's data
    public Model(String id)
    {
        data    =   new HashMap<>();
        columns =   new ArrayList<>();
        initTable();
        initColumns();
    }
    
    //Initializes the tables mapping properties
    //Implementation should initialize table name
    //and optionally override table properties here
    protected abstract void initTable();
            
      
    //Initializes the model's mapping to column names
    //Fetches and fills the model's columns collection
    //with all column names of the respective mapping table
    protected void initColumns()
    {    
        try
        {
            JsonObject metaData   =   builder().first().execute().get(0).getAsJsonObject();
            JsonArray colNames    =   metaData.get("columNames").getAsJsonArray();
            int numColumns        =   metaData.get("columnCount").getAsInt();
            
            columns.clear();
            for(int colIndex = 1; colIndex <= numColumns; colIndex++)
                columns.add(colNames.get(colIndex).getAsString());
        } 
        
        catch(SQLException e)
        {
            System.out.println("[SQL EXCEPTION] Failed to init columns - " + e.getMessage());
        } 
    }
    
    //Sets a models column value
    //Entries in Models table correspond to table columns
    public void set(String colName, Object value)
    {
        data.put(table, value);
    }
    
    //Returns the current columns in models data
    //Don't confuse with model's column names (meta)
    //This is the active Model instance data column names
    private String getDataColumns()
    {
        String columnNames          =   "";
        Iterator<String> nameIter   =   data.keySet().iterator();
        
        while(nameIter.hasNext())
            columnNames += nameIter.next() + ", ";
        return columnNames;
    }
    
    //Returns the current Models column value in data
    private String getDataValues()
    {
        String columnValues         =   "";
        Iterator<Object> valIter    =   data.values().iterator();
        
        while(valIter.hasNext())
            columnValues += valIter.next() + ", ";
        return columnValues;
    }
    
    
    //Finds an existing record in the mapping table
    //If a record is found, adds entries to models data
    //Is called if init by ID (find)
    public String fetchExisting(String id)
    {
        QueryBuilder qBuilder   =   builder().where(primaryKey, "=", id);
        String query            =    qBuilder.build();
        
        try(DataConnector conn =   new DataConnector())
        {
            conn.execute(query);    
            JsonArray results = JsonParser.resultsToJson(conn.getResults());
            
            if(results.size() > 0)
            {
                JsonObject entry            =   results.get(0).getAsJsonObject();
                
                Iterator<String> colIter    =   columns.iterator();
                while(colIter.hasNext())
                {
                    String column   =   colIter.next().toUpperCase();
                    data.put(column, entry.get(column).getAsString());
                }
            }
            
            return results.toString();
            
        }
        
        catch(SQLException e)
        {
            System.out.println("[SQL EXCEPTION] Failed to select first record - " + e.getMessage());
            return null;
        }
    }
    
    public String buildUpdate()
    {
        String updateStr =   "";
        Iterator<Map.Entry<String, Object>> setData =  data.entrySet().iterator();
        
        if(setData.hasNext()) updateStr += "SET ";
        else return "";
        
        while(setData.hasNext())
        {
            Map.Entry<String, Object> column    =   setData.next();
            
            boolean isLiteral                   =   column.getValue() instanceof String;
            String colName                      =   column.getKey();
            String colValue                     =   (isLiteral)? (String) column.getValue() : column.getValue().toString();
            
            updateStr += MessageFormat.format("SET {0} = {1}", colName, (isLiteral)? Conditional.makeLiteral(colValue) : colValue);
        }
        
        System.out.println(updateStr);
        return updateStr;
    }
    
    
    //Saves the current Model
    //Inserts the Model data values corresponding to data column names
    //Into the Model mapping table
    //Returns true if the insertion was successful
    public boolean save()
    {
        String columnNames      =   getDataColumns();
        String columnValues     =   getDataValues();
        String insertQuery      =   MessageFormat.format("INSERT INTO {0} ({1}) VALUES ({2});", table, columnNames, columnValues);
        
        try(DataConnector conn  =   new DataConnector())
        {
            conn.execute(insertQuery);
            return true;
        }
        
        catch(SQLException e)
        {
            System.out.println("[SQL EXCEPTION] Failed to save record - " + e.getMessage());
            return false;
        }
    }
    
    public boolean update()
    {
        String changes      =   buildUpdate();
        String id           =   data.get(primaryKey).toString();
        String updateQuery  =   MessageFormat.format("UPDATE {0} {1} WHERE {2} = {3}", table, changes, primaryKey, id);
        
        try(DataConnector conn   =   new DataConnector())
        {
            conn.execute(updateQuery);
            return true;
        }
        
        catch(SQLException e)
        {
            System.out.println("[SQL EXCEPTION] Failed to update record - " + e.getMessage());
            return false;
        }
    }
    
    
    //Returns the Model's mapping table name
    public String getTableName()
    {
        return table;
    }
    
    //Returns the Model's table primary key
    public String getPrimaryKey()
    {
        return primaryKey;
    }

    //Returns a QueryBuilder instance for the Model
    public QueryBuilder builder()
    {
        return new QueryBuilder(table);
    }
}
