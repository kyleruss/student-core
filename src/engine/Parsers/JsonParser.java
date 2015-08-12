

package engine.Parsers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class JsonParser 
{
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
    
    public static String parsePretty(JsonElement json)
    {
        Gson gson   =   new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(json);
    }
}
