//####################################
//  KYLE RUSSELL
//  13831056
//  PDC Project
//####################################

package engine.Models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    
 /*(   public boolean insert(List<String> values)
    {
        
    }
    
    public boolean insert(List<String> columns, List<String> values)
    {
        
    }
    
    public boolean update(Map<String, String> changes)
    {
        
    }
    
    public boolean delete(Map<String, String> conditions)
    {
        
    }
    
    public Model search(String id)
    {
        
    }
    
    public String colVal(String column)
    {
        
    }
    
    public QueryBuilder where()
    {
        
    }
    
    public QueryBuilder builder()
    {
        return 
    }
    
    public QueryBuilder get()
    {
        return get(columns);
    }
    
    public QueryBuilder get(List<String> columns)
    {
        
    } */
}
