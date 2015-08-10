//####################################
//  KYLE RUSSELL
//  13831056
//  PDC Project
//####################################

package engine.Models;

import engine.core.database.QueryBuilder;
import java.util.List;


public abstract class Model 
{
    public static final String DEFAULT_PK_NAME  =   "id";
    
    protected String table;
    
    protected String primary_key;
    
    protected List<String> columns;
    
    public Model()
    {
        
    }
    
    public Model(String id)
    {
        
    }
      
    protected void initColumns()
    {
        
    }
    
    public String getTableName()
    {
        return table;
    }
    
    public String getPrimaryKey()
    {
        return primary_key;
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
