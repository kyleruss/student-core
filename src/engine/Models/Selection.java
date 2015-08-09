
package engine.Models;


public class Selection 
{
    private String column_name;
    
    private String column_alias;
    
    public Selection(String column_name)
    {
        this(column_name, null);
    }
    
    public Selection(String column_name, String column_alias)
    {
        this.column_name    =   column_name;
        this.column_alias   =   column_alias;
    }
    
    public String getName()
    {
        return column_name;
    }
    
    public String getAlias()
    {
        return column_alias;
    }
   
}
