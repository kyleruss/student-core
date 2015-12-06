//====================================
//	Kyle Russell
//	jdamvc
//	Selection
//====================================

package engine.core.database;

import java.text.MessageFormat;

//-------------------------------
//          SELECTION
//-------------------------------
//- Represents select column(s) in a select
// for QueryBuilder/Query
//- Can create with/without column alias
//- table prefixes need to be used if joining

public class Selection 
{
    //Default selects all columns in the table
    public static final String DEFAULT_SELECT   =   "*";
    
    //Selections column name
    private String column_name;
    
    //Selections column alias (AS)
    private String column_alias;
    
    //Default selects all columns
    public Selection()
    {
        this(DEFAULT_SELECT);
    }
    
    //Create a seelct with signle column
    public Selection(String column_name)
    {
        this(column_name, null);
    }
    
    //Create t select with column and it's alias name
    public Selection(String column_name, String column_alias)
    {
        this.column_name    =   column_name;
        this.column_alias   =   column_alias;
    }
    
    //Returns the selections column name
    public String getName()
    {
        return column_name;
    }
    
    //Returns the selections column alias 
    public String getAlias()
    {
        return column_alias;
    
    }
    
    //Returns string representation of select
    //Is formatted ready for query use
    @Override
    public String toString()
    {
        String alias_str    =   (column_alias != null)? MessageFormat.format(" AS {0}", column_alias) : "";
        String output       =   MessageFormat.format("{0}{1}", column_name, alias_str);
        return output;
    }
   
}
