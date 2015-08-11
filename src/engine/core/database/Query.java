
package engine.core.database;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;


public class Query 
{

    public enum QueryType
    {
        INSERT, 
        DELETE, 
        UPDATE, 
        SELECT
    }
    
    @SerializedName("table_joins")
    private final List<Join> joins;
    
    @SerializedName("where")
    private final List<Conditional> conditionals;
    
    @SerializedName("select")
    private final List<Selection> selections;
    
    @SerializedName("extras")
    private final List<String> extras;
    
    @SerializedName("table_name")
    private final String table;
    
    private QueryType type;
    
    public Query(String table)
    {
        joins                =       new ArrayList<>();
        conditionals         =       new ArrayList<>();
        selections           =       new ArrayList<>();
        extras               =       new ArrayList<>();
        this.table           =       table;
        type                 =       QueryType.SELECT;
    }
    
    public List<Join> getJoins()
    {
        return joins;
    }
    
    public List<Conditional> getConditionals()
    {
        return conditionals;
    }
    
    public List<Selection> getSelections()
    {
        return selections;
    }
    
    public List<String> getExtras()
    {
        return extras;
    }
    
    public String formatSelections()
    {
        //Add default selection if no selections added
        if(selections.isEmpty()) 
            selections.add(new Selection()); 
        
        String formattedSelections   =   "";
        for(int selectionIndex = 0; selectionIndex < selections.size(); selectionIndex++)
            formattedSelections += selections.get(selectionIndex) + ((selectionIndex != selections.size() - 1)? ", " : "");
        
        return formattedSelections;
    }
    
    public String formatConditionals()
    {
        final String conjunction    =   " AND ";
        final String disjunction    =   " OR ";
        String formattedConditions =   "";
        
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
    
    public String formatExtras()
    {
        String formattedExtras  =   "";
        for(String extra : extras)
            formattedExtras += extra + " ";
        return formattedExtras;
    }
    
    public String getTable()
    {
        return table;
    }
    
    public void addConditional(Conditional condition)
    {
        conditionals.add(condition);
    }
    
    public void addJoin(Join join)
    {
        joins.add(join);
    }
    
    public void addSelection(Selection select)
    {
        selections.add(select);
    }
    
    public void addSelections(Selection[] selections)
    {
        for(Selection select : selections)
            this.selections.add(select);
    }
    
    public void addExtra(String extra)
    {
        extras.add(extra);
    }
    
    public QueryType getType()
    {
        return type;
    }
    
    public void setType(QueryType type)
    {
        this.type   =   type;
    }
    
    public String getJson()
    {
        Gson json   =   new GsonBuilder().setPrettyPrinting().create();
        return json.toJson(this);
    }
}
