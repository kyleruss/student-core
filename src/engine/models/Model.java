//####################################
//  KYLE RUSSELL
//  13831056
//  PDC Project
//####################################

package engine.models;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import engine.parsers.JsonParser;
import engine.config.DatabaseConfig;
import engine.core.Column;
import engine.core.DataConnector;
import engine.core.database.QueryBuilder;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
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
    protected final Map<String, Column> data;
    
    //Create a new model that does not identify a row
    //Data will be empty and records must be added to model before mutation
    public Model()
    {
        data    =   new LinkedHashMap<>();
        columns =   new ArrayList<>();
        initTable();
        initColumns();
    }
    
    //Create model that maps to an existing row
    //Pass the row primary key value
    //Row data fills the model's data
    public Model(String id)
    {
        this();
        fetchExisting(id);
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
            JsonArray colNames    =   metaData.get("columnNames").getAsJsonArray();
            
            initColumns(colNames);
        } 
        
        catch(SQLException e)
        {
            System.out.println("[SQL EXCEPTION] Failed to init columns - " + e.getMessage());
        } 
    }
    
    protected void initColumns(JsonArray resultColumns)
    {
        columns.clear();
        for(int colIndex = 0; colIndex < resultColumns.size(); colIndex++)
            columns.add(resultColumns.get(colIndex).getAsString());        
    }
    
    //Sets a models column value
    //Entries in Models table correspond to table columns
    public void set(String colName, Object value)
    {
        data.put(colName.toUpperCase(), new Column(colName.toUpperCase(), value));
    }
    
    public void set(String colName, Object value, String columnType)
    {
        data.put(colName.toUpperCase(), new Column(colName.toUpperCase(), value, columnType));
    }
    
    public Column get(String colName)
    {
        return data.get(colName);
    }
    
    //Returns the current columns in models data
    //Don't confuse with model's column names (meta)
    //This is the active Model instance data column names
    private String getDataColumns()
    {
        String columnNames          =   "";
        Iterator<String> nameIter   =   data.keySet().iterator();
        
        while(nameIter.hasNext())
            columnNames += nameIter.next() + ((nameIter.hasNext())? ", " : "");
        return columnNames;
    }
    
    //Returns the current Models column value in data
    private String getDataValues()
    {
        String columnValues         =   "";
        Iterator<Column> valIter    =   data.values().iterator();
        
        while(valIter.hasNext())
            columnValues += valIter.next().getColumnValue() + ((valIter.hasNext())? ", " : "");
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
            JsonArray results       =   JsonParser.resultsToJson(conn.getResults());
            JsonArray columnNames   =   results.get(0).getAsJsonObject().get("columnNames").getAsJsonArray();
            ResultSetMetaData meta  =   conn.getResults().getMetaData();
            
            System.out.println(results);
            initColumns(columnNames);
            if(results.size() > 0)
            {
                JsonObject entry            =   results.get(1).getAsJsonObject();
                
                for(int columnIndex = 0; columnIndex < columns.size(); columnIndex++)
                {
                    String column   =   columns.get(columnIndex);
                    String typeName =   meta.getColumnClassName(columnIndex + 1);
                    set(column, JsonParser.castElementToObj(entry.get(column), typeName));
                }

            }
            
            return results.toString();
            
        }
        
        catch(SQLException e)
        {
            System.out.println("[SQL EXCEPTION] Failed to find this model - " + e.getMessage());
            return null;
        }
    }
    
    public String buildUpdate()
    {
        String updateStr =   "";
        Iterator<Map.Entry<String, Column>> setData =  data.entrySet().iterator();     
        
        if(setData.hasNext()) updateStr += "SET ";
        else return updateStr;
        
        while(setData.hasNext())
        {
            Map.Entry<String, Column> column    =   setData.next();
            
            String colName         =   column.getKey();
            String colValue        =   column.getValue().getColumnValue().toString();
            
            if(!colName.equalsIgnoreCase(primaryKey))
                updateStr += MessageFormat.format("{0} = {1}{2} ", 
                             colName, colValue, (setData.hasNext())? "," : "");
        }
        
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
        String insertQuery      =   MessageFormat.format("INSERT INTO {0} ({1}) VALUES ({2})", table, columnNames, columnValues);
        
        System.out.println(insertQuery);
        try(DataConnector conn  =   new DataConnector())
        {
            conn.setQueryMutator();
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
        Column column       =   data.get(primaryKey.toUpperCase());
        String id           =   column.getColumnValue().toString();
       // id                  =   (column.isLiteral())? makeLiteral(id) : id;
        String updateQuery  =   MessageFormat.format("UPDATE {0} {1} WHERE {2} = {3}", table, changes, primaryKey, id);
        System.out.println(updateQuery);
        
        try(DataConnector conn   =   new DataConnector())
        {
            conn.setQueryMutator();
            conn.execute(updateQuery);
            return true;
        }
        
        catch(SQLException e)
        {
            System.out.println("[SQL EXCEPTION] Failed to update record - " + e.getMessage());
            return false;
        }
    }
    
    //Makes the value a literal
    //Literals in SQL must be enclosed in single quotes
    //Make sure to clean value before making literal
    public static String makeLiteral(String value)
    {
        String literalFormat    =   MessageFormat.format("''{0}''", value);
        return literalFormat;
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
