
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
    
    @SerializedName("table_name")
    private final String table;
    
    private QueryType type;
    
    public Query(String table)
    {
        joins                =       new ArrayList<>();
        conditionals         =       new ArrayList<>();
        selections           =       new ArrayList<>();
        this.table           =       "test";
        type                 =       null;
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
    
    public String formatSelections()
    {
        String formatted_selections   =   "";
        for(Selection selection : selections)
            formatted_selections += selection + ", ";
        
        return formatted_selections;
    }
    
    public String formatConditionals()
    {
        final String conjunction    =   " AND ";
        final String disjunction    =   " OR ";
        String formatted_conditions =   "";
        
        for(Conditional condition : conditionals)
        {
            formatted_conditions += condition;
            
            //Add conjunctions/disjunctions to conditionals if more than
            //one where clause in the query
            if(conditionals.size() > 1)
                formatted_conditions += (condition.isConjunction())? conjunction : disjunction;
        }
        
        return formatted_conditions;
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
