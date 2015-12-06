//====================================
//	Kyle Russell
//	jdamvc
//	Model
//====================================

package engine.models;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import engine.core.JsonParser;
import engine.config.DatabaseConfig;
import engine.core.database.Column;
import engine.core.DataConnector;
import engine.core.ExceptionOutput;
import engine.core.database.QueryBuilder;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.Iterator;
import java.util.LinkedHashMap;
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
    protected String primaryKey = DatabaseConfig.DEFAULT_KEY;
    
    //The columns in the table
    //Columns are fetched on initialization by initColumns()
    protected Map<String, Column> columns;
    
    //The table's row data
    //Allows for mutation such as insertion, deletion and editing
    protected final Map<String, Column> data;
    
    protected DataConnector activeConnection;
    
    //Create a new model that does not identify a row
    //Data will be empty and records must be added to model before mutation
    public Model()
    {
        data    =   new LinkedHashMap<>();
        columns =   new LinkedHashMap<>();
        initTable();
        initColumns();
    }
    
    //Create model that maps to an existing row
    //Pass the row primary key value
    //Row data fills the model's data
    public Model(Object id)
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
            JsonArray metaDataTemp   =   builder().first().execute();
            if(metaDataTemp == null) return;
            
            JsonObject metaData     =   metaDataTemp.get(0).getAsJsonObject();
            JsonArray colNames      =   metaData.get("columnNames").getAsJsonArray();
            JsonArray colTypes      =   metaData.get("columnTypes").getAsJsonArray();
            
            initColumns(colNames, colTypes);
        } 
        
        catch(SQLException e)
        {
            ExceptionOutput.output("[SQL EXCEPTION] Failed to init columns - " + e.getMessage(), ExceptionOutput.OutputType.DEBUG);
        } 
    }
    
    protected void initColumns(JsonArray resultColumns, JsonArray colTypes)
    {
        columns.clear();
        for(int colIndex = 0; colIndex < resultColumns.size(); colIndex++)
        {
            String colName  =   resultColumns.get(colIndex).getAsString();
            String colType  =   colTypes.get(colIndex).getAsString();
            columns.put(colName, new Column(colName, colType));      
        }
    }
    
    public Map<String, Column> getColumns()
    {
        return columns;
    }
    
    public Map<String, Column> getData()
    {
        return data;
    }
    
    public void setActiveConnection(DataConnector liveConnection)
    {
        this.activeConnection   =   liveConnection;
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
        return data.get(colName.toUpperCase());
    }
    
    public Column getColumn(String colName)
    {
        return columns.get(colName.toUpperCase());
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
        {
            Column current  =   valIter.next();
            String colName  =   current.getColumnName();
            
            current.setLiteral(columns.get(colName).isLiteral());
            columnValues += current.getColumnValue() + ((valIter.hasNext())? ", " : "");
        }
        return columnValues;
    }
    
    public boolean hasColumn(String columnName)
    {
        return columns.containsKey(columnName.toUpperCase());
    }
    
    
    //Finds an existing record in the mapping table
    //If a record is found, adds entries to models data
    //Is called if init by ID (find)
    public String fetchExisting(Object id)
    {
        String idLiteral        =   (getColumn(primaryKey).isLiteral())? makeLiteral(id.toString()) : id.toString();
        QueryBuilder qBuilder   =   builder().where(primaryKey, "=", idLiteral);
        String query            =    qBuilder.build();
        
        try(DataConnector conn =   new DataConnector())
        {
            conn.execute(query);    
            JsonArray results       =   JsonParser.resultsToJson(conn.getResults());
            if(results == null) return null;
            
            JsonObject colMeta      =   results.get(0).getAsJsonObject();
            if(colMeta == null) return null;
            
            JsonArray columnNames   =   colMeta.get("columnNames").getAsJsonArray();
            ResultSetMetaData meta  =   conn.getResults().getMetaData();
              
            if(results.size() > 1)
            {
                JsonObject entry            =   results.get(1).getAsJsonObject();
                
                for(int columnIndex = 0; columnIndex < columns.size(); columnIndex++)
                {
                    
                    String columnName   =   columnNames.get(columnIndex).getAsString();
                    String typeName     =   meta.getColumnClassName(columnIndex + 1);

                    if(entry.get(columnName) == null || entry.get(columnName).getAsString().equals("null")) continue;
                    else set(columnName, JsonParser.castElementToObj(entry.get(columnName), typeName));
                }

            }
            
            return results.toString();
            
        }
        
        catch(SQLException e)
        {
            ExceptionOutput.output("[SQL EXCEPTION] Failed to find this model - " + e.getMessage(), ExceptionOutput.OutputType.DEBUG);
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
            Column current         =   column.getValue();
            String colName         =   column.getKey();
            
            current.setLiteral(columns.get(colName).isLiteral());
            Object colValue        =   current.getColumnValue();
            colValue               =   (colValue == null)? "null" : colValue.toString();
            
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
        
        if(activeConnection == null)
        {
            try(DataConnector conn  =   new DataConnector())
            {
                conn.setQueryMutator();
                return conn.execute(insertQuery);
            }
        }

        else
        {
            activeConnection.setQueryMutator();
            return activeConnection.execute(insertQuery);
        }
    }
    
    public boolean update()
    {
        String changes      =   buildUpdate();
        Column column       =   data.get(primaryKey.toUpperCase());
        if(column == null) return false;
        
        String id           =   column.getColumnValue().toString();
        String updateQuery  =   MessageFormat.format("UPDATE {0} {1} WHERE {2} = {3}", table, changes, primaryKey, id);
        
        try(DataConnector conn   =   new DataConnector())
        {
            conn.setQueryMutator();
            conn.execute(updateQuery);
            return true;
        }
    }
    
    public boolean delete()
    {
        Column idCol   =   data.get(primaryKey.toUpperCase());
        if(idCol == null) return false;
        else
        {
            String id           =   idCol.getColumnValue().toString();
            String deleteQuery  =   MessageFormat.format("DELETE FROM {0} WHERE {1} = {2}", table, primaryKey, id);
            
            try(DataConnector conn  =   new DataConnector())
            {
                conn.setQueryMutator();
                conn.execute(deleteQuery);
                return true;
            }
        }
    }
    
    public boolean exists()
    {
        return data.get(primaryKey.toUpperCase()) != null;
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
