
package engine.core.database;

import java.text.MessageFormat;


public class Conditional 
{
    private String column;
    
    private String operator;
    
    private String value;
    
    private boolean isConjunction;
    
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
    
    @Override
    public String toString()
    {
       String output    =   MessageFormat.format("WHERE {0} {1} {2}", column, operator, value);
       return output;
    }
}
