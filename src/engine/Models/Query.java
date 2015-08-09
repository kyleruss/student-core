
package engine.Models;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

public class Query 
{
    
    @SerializedName("table_joins")
    private final List<Join> joins;
    
    @SerializedName("where")
    private final List<Conditional> conditionals;
    
    @SerializedName("select")
    private final List<String> selections;
    
    @SerializedName("table_name")
    private final String table;
    
    public Query()
    {
        joins           =       new ArrayList<>();
        conditionals    =       new ArrayList<>();
        selections      =       new ArrayList<>();
        table           =       "test"; 
    }
    
    public List<Join> getJoins()
    {
        return joins;
    }
    
    public List<Conditional> getConditionals()
    {
        return conditionals;
    }
    
    public List<String> getSelections()
    {
        return selections;
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
    
    public void addSelection(String select)
    {
        selections.add(select);
    }
    
    public void addSelections(String[] selections)
    {
        for(String select : selections)
            this.selections.add(select);
    }
}
