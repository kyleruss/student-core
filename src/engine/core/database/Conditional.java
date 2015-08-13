
package engine.core.database;

import java.text.MessageFormat;


//-----------------------------------
//             CONDITIONAL
//-----------------------------------
//- Represents WHERE clause in query
//- Supports all operators
//- Allows for and identifies literals

public class Conditional 
{
    //Conditionals column name
    //The querying column
    private String column;
    
    //The conditionals operator
    //Supports all operators including wildcards
    private String operator;
    
    private String value;
    
    private boolean isConjunction;
    
    private boolean isLiteral;
    
    public Conditional(String column, String operator, String value)
    {
        this(column, operator, value, true);
    }
    
    public Conditional(String column, String operator, String value, boolean isConjunction)
    {
        this.column         =   column;
        this.operator       =   operator;
        this.value          =   value;
        this.isConjunction  =   isConjunction;
        isLiteral           =   false;
    }
    
    public String getColumn()
    {
        return column;
    }
    
    public String getOperator()
    {
        return operator;
    }
    
    public String getValue()
    {
        return value;
    }
    
    public boolean isConjunction()
    {
        return isConjunction;
    }
    
    public boolean isLiteral()
    {
        return isLiteral;
    }
    
    public void literal()
    {
        isLiteral   =   true;
    }
    
    public static String makeLiteral(String value)
    {
        String literalFormat    =   MessageFormat.format("''{0}''", value);
        return literalFormat;
    }
    
    @Override
    public String toString()
    {
       String outVal        =   (isLiteral)? makeLiteral(value) : value;
       String output        =   MessageFormat.format("WHERE {0} {1} {2}", column, operator, outVal);
       return output;
    }
}
