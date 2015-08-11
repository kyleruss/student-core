
package engine.core.database;

import java.text.MessageFormat;

public class Selection 
{
    private String column_name;
    
    private String column_alias;
    
    public static final String DEFAULT_SELECT   =   "*";
    
    //Default selects all columns
    public Selection()
    {
        this(DEFAULT_SELECT);
    }
    
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
    
    @Override
    public String toString()
    {
        String alias_str    =   (column_alias != null)? MessageFormat.format(" AS {0}", column_alias) : "";
        String output       =   MessageFormat.format("{0}{1}", column_name, alias_str);
        return output;
    }
   
}
