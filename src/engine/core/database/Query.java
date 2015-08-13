//####################################
//  KYLE RUSSELL
//  13831056
//  PDC Project
//####################################

package engine.core.database;

import com.google.gson.annotations.SerializedName;
import engine.Parsers.JsonParser;
import java.util.ArrayList;
import java.util.List;

//--------------------------------
//              QUERY
//--------------------------------
//- aux ds for raw SQL queries
//- Represents a database query
//- Supports building of non-DML queries


public class Query 
{
    public enum QueryType
    {
        INSERT, 
        DELETE, 
        UPDATE, 
        SELECT
    }
    
    //Query joins
    //Include inner, self, left, outer joins
    @SerializedName("table_joins")
    private final List<Join> joins;
    
    //Query conditionals
    //Where clauses 
    @SerializedName("where")
    private final List<Conditional> conditionals;
    
    //Query selections
    //Select Column, function names
    @SerializedName("select")
    private final List<Selection> selections;
    
    //Query extras
    //Includes limits, offsets, orderby, groupby
    @SerializedName("extras")
    private final List<String> extras;
    
    //Query mapping table
    @SerializedName("table_name")
    private final String table;
    
    //Query underlying operation type
    private QueryType type;
    
    //Creates a query that maps to table
    //Defaults type to select
    //query components default empty, add by builder or directly
    public Query(String table)
    {
        joins                =       new ArrayList<>();
        conditionals         =       new ArrayList<>();
        selections           =       new ArrayList<>();
        extras               =       new ArrayList<>();
        this.table           =       table;
        type                 =       QueryType.SELECT;
    }
    
    //Returns the queries joins
    public List<Join> getJoins()
    {
        return joins;
    }
    
    //Returns the queries conditionals
    public List<Conditional> getConditionals()
    {
        return conditionals;
    }
    
    //Returns the queries selections
    public List<Selection> getSelections()
    {
        return selections;
    }
    
    //Returns the queries extras
    public List<String> getExtras()
    {
        return extras;
    }
    
    //Formats the queries selections into a select statement
    //If empty selects all (*) => Selection default
    //For formatting of individual selections see Selections
    public String formatSelections()
    {
        //Add default selection if no selections added
        //Defaults to selecting all columns (*)
        if(selections.isEmpty()) 
            selections.add(new Selection()); 
        
        //Format: SELECT column1, column2,..columnN
        String formattedSelections   =   "";
        for(int selectionIndex = 0; selectionIndex < selections.size(); selectionIndex++)
            formattedSelections += selections.get(selectionIndex) + ((selectionIndex != selections.size() - 1)? ", " : "");
        
        return formattedSelections;
    }
    
    //Formats the queries conditionals
    //Connects multiple conditionals
    //by their respective connective type
    
    public String formatConditionals()
    {
        final String conjunction    =   " AND "; // isConjunction
        final String disjunction    =   " OR "; // !isConjunction
        String formattedConditions =   "";
        
        //Format: WHERE column operator value [AND/OR] .. WHERE column operator value..
        for(int condIndex = 0; condIndex < conditionals.size(); condIndex++)
        {
            Conditional condition =   conditionals.get(condIndex);
            formattedConditions += condition;
            
            //Add conjunctions/disjunctions to conditionals if more than
            //one where clause in the query
            if(conditionals.size() > 1 && condIndex != conditionals.size() - 1)
                formattedConditions += (condition.isConjunction())? conjunction : disjunction;
        }
        
        return formattedConditions;
    }
    
    //Format the queries extra statements
    //Extras are built last
    public String formatExtras()
    {
        String formattedExtras  =   "";
        for(String extra : extras)
            formattedExtras += extra + " ";
        return formattedExtras;
    }
    
    //Returns the queries mapping table
    public String getTable()
    {
        return table;
    }
    
    //Add a new conditional to the query
    public void addConditional(Conditional condition)
    {
        conditionals.add(condition);
    }
    
    //Add a new join to the query
    public void addJoin(Join join)
    {
        joins.add(join);
    }
    
    //Add a new selection to the query
    public void addSelection(Selection select)
    {
        selections.add(select);
    }
    
    //Add multiple selections to the query
    public void addSelections(Selection[] selections)
    {
        for(Selection select : selections)
            this.selections.add(select);
    }
    
    //Add an extra to the query
    //These statements are not necessarily supported
    public void addExtra(String extra)
    {
        extras.add(extra);
    }
    
    //Returns the query operation type
    public QueryType getType()
    {
        return type;
    }
    
    //Set the query operation type
    public void setType(QueryType type)
    {
        this.type   =   type;
    }
    
    //Returns a JSON representation of the query
    //For keys see serialized name tags above
    public String getJson()
    {
        return JsonParser.parsePretty(this);
    }
}
