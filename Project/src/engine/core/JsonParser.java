//====================================
//	Kyle Russell
//	jdamvc
//	JsonParser
//====================================

package engine.core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;


//---------------------------------
//          JSON PARSER
//---------------------------------
//- Handles conversion to JSON of DB objects
//- Support conversion of ResultSet to JSON
//- utility parsePretty() converts obj to JSON in format


public class JsonParser 
{
    
    //Takes a ResultSet and parses it as a JSON array
    //Column data is stored in a JsonObject 
    //While the entire ResultSet is returned as JsonArray
    public static JsonArray resultsToJson(ResultSet results)
    {
        try
        {
            if(results == null) 
                return null;
            
            JsonArray data           =   new JsonArray();
            ResultSetMetaData meta   =   results.getMetaData();
            
            //Add result meta data to results
            //Meta data will be the first element in data
            //Results will follow after meta data
            JsonObject metaData     =   new JsonObject();
            JsonArray columnNames   =   new JsonArray();  
            JsonArray columnTypes   =   new JsonArray();
            
            for(int i = 1; i <= meta.getColumnCount(); i++)
            {
                columnTypes.add(new JsonPrimitive(meta.getColumnClassName(i)));
                columnNames.add(new JsonPrimitive(meta.getColumnName(i)));
            }
            
            metaData.add("columnNames", columnNames); //Column names
            metaData.addProperty("columnCount", meta.getColumnCount()); //Number of columns
            metaData.add("columnTypes", columnTypes);
            data.add(metaData); //Add meta data to front of returning JsonArray
            
            //Iterate through resulting rows
            while(results.next())
            {
                JsonObject currentRow    =   new JsonObject();
                
                //Add column data to json
                for(int columnIndex = 1; columnIndex <= meta.getColumnCount(); columnIndex++)
                    currentRow.addProperty(meta.getColumnName(columnIndex), "" + results.getObject(columnIndex));
                
                
                data.add(currentRow);
            }
           
            return data;
        }
        
        catch(SQLException e)
        {
            ExceptionOutput.output("[SQL EXCEPTION] Failed to parse result set - " + e.getMessage(), ExceptionOutput.OutputType.DEBUG);
            return null;
        }
    }
    
    public static Object castElementToObj(JsonElement element, String className)
    {
        switch(className)
        {
            case "java.lang.String": return element.getAsString();
            case "java.lang.Integer": return element.getAsBigInteger();
            case "java.lang.Double": return element.getAsDouble();
            case "java.lang.Boolean": return element.getAsBoolean();
            case "java.lang.Character": return element.getAsCharacter();
            case "java.sql.Date": return element.getAsString();
            default: return null;
        }
    }
    
    //Takes a object and parses it as JSON in clear format 
    public static String parsePretty(Object json)
    {
        Gson gson   =   new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(json);
    }
}
