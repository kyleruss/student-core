
package engine.Models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class QueryBuilder 
{
    public static final String TABLE_KEY    =   "table";
    public static final String TYPE_KEY     =   "query_type";
    public static final String WHERE_KEY    =   "conditions";
    public static final String JOIN_KEY     =   "joins";
    public static final String SELECT_KEY   =   "filters";
    
    private Query query_contents;
    
    
    public QueryBuilder(String table)
    {
        query_contents  =   new Query();
        query_contents.addSelections(new String[] {"name", "email", "date"});
    }
    
    public QueryBuilder(Query query_contents)
    {
        this.query_contents =   query_contents;
    }
    
    /*public static QueryBuilder where(String column, String operator, String value)
    {
        
    }
    
    
    public static QueryBuilder table(String table)
    {
        
    } */
    
    public static void main(String[] args)
    {
        Gson gson   =   new GsonBuilder().setPrettyPrinting().create();
        String json =   gson.toJson(new QueryBuilder("test"));
        System.out.println(json);
        //JsonParser p    =   new JsonParser();
       // String table    =   p.parse(json).getAsJsonObject().get("query_contents").getAsString();
        Query q         =   gson.fromJson(json, Query.class);
        System.out.println("TABLE: " + q.getTable());//.getTable());
    }
}
