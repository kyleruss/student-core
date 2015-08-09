
package engine.Models;


public class Conditional 
{
    private String column;
    
    private String operator;
    
    private String value;
    
    public Conditional(String column, String operator, String value)
    {
        this.column     =   column;
        this.operator   =   operator;
        this.value      =   value;
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
}
