//####################################
//  KYLE RUSSELL
//  13831056
//  PDC Project
//####################################

package engine.Parsers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
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
            JsonArray data           =   new JsonArray();
            ResultSetMetaData meta   =   results.getMetaData();
            
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
            System.out.println("[SQL EXCEPTION] Failed to parse result set - " + e.getMessage());
            return null;
        }
    }
    
    //Takes a object and parses it as JSON in clear format 
    public static String parsePretty(Object json)
    {
        Gson gson   =   new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(json);
    }
}
