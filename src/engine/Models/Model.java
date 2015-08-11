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
import engine.core.database.QueryBuilder;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public abstract class Model 
{
    
    protected String table;
    
    protected String primaryKey = (String) DatabaseConfig.config().get("DEFAULT_PRIMARY");
    
    protected List<String> columns;
    
    private final Map<String, Object> data;
    
    //Create new model
    public Model()
    {
        data    =   new HashMap<>();
        columns =   new ArrayList<>();
        initTable();
        initColumns();
    }
    
    //Create model with id (find)
    public Model(String id)
    {
        data    =   new HashMap<>();
        columns =   new ArrayList<>();
        initTable();
        initColumns();
    }
    
    protected abstract void initTable();
            
      
    protected void initColumns()
    {    
        try
        {
            ResultSet results   =   builder().first().execute();

            columns.clear();
            ResultSetMetaData meta  =   results.getMetaData();
            int columnCount         =   meta.getColumnCount();

            for(int colIndex = 1; colIndex <= columnCount; colIndex++)
                columns.add(meta.getColumnName(colIndex));

        } 
        
        catch(SQLException e)
        {
            System.out.println("[SQL EXCEPTION] Failed to init columns - " + e.getMessage());
        }
    }
    
    public void set(String colName, Object value)
    {
        data.put(table, value);
    }
    
    private String getDataColumns()
    {
        String columnNames          =   "";
        Iterator<String> nameIter   =   data.keySet().iterator();
        
        while(nameIter.hasNext())
            columnNames += nameIter.next() + ", ";
        return columnNames;
    }
    
    
    private String getDataValues()
    {
        String columnValues         =   "";
        Iterator<Object> valIter    =   data.values().iterator();
        
        while(valIter.hasNext())
            columnValues += valIter.next() + ", ";
        return columnValues;
    }
    
    public String fetchExisting(String id)
    {
        QueryBuilder qBuilder   =   builder(table).where(primaryKey, "=", id);
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
                    /*String column   =   */ System.out.println("COL: " + colIter.next().toUpperCase());
                   // data.put(column, entry.get(column).getAsString());
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
        return true;
    }
    
    public String getTableName()
    {
        return table;
    }
    
    public String getPrimaryKey()
    {
        return primaryKey;
    }

    public QueryBuilder builder()
    {
        return new QueryBuilder(table);
    }
    
    public static QueryBuilder builder(String table_name)
    {
        return new QueryBuilder(table_name);
    }
}
