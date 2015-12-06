//====================================
//	Kyle Russell
//	jdamvc
//	Conditional
//====================================

package engine.core.database;

import engine.models.Model;
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
    
    //The conditional value
    private String value;
    
    //Indicates connectors are conjunctions(true)/disjunctions(false)
    //Where more than 1 conditional in a query
    //Conditionals are joined by disjunctions/conjunctions
    private boolean isConjunction;
    
    //Indicates the conditional value ia a literal
    //At format time the value will be handled accordingly
    private boolean isLiteral;
    
    //Create a conditional with default conjunction connectives
    public Conditional(String column, String operator, String value)
    {
        this(column, operator, value, true);
    }
    
    //Create a conditional with specified connectives
    //isConjunction: (true) => conjunctives, (false) => disjunctive
    //isLiteral defaults to false and to enable call isLiteral()
    public Conditional(String column, String operator, String value, boolean isConjunction)
    {
        this.column          =   column;
        this.operator        =   operator;
        this.value           =   value;
        this.isConjunction   =   isConjunction;
        isLiteral            =   false;
    }
    
    //Returns the conditionals acting column name
    public String getColumn()
    {
        return column;
    }
    
    //Returns the conditionals operator
    public String getOperator()
    {
        return operator;
    }
    
    //Returns the conditionals querying value
    public String getValue()
    {
        return value;
    }
    
    //Returns the conditionals connectivity type
    //true => conjunctives
    //false => disjunctives
    public boolean isConjunction()
    {
        return isConjunction;
    }
    
    //Returns true if the conditionals value is a literal
    public boolean isLiteral()
    {
        return isLiteral;
    }
    
    //Set the conditionals value to literal
    //value will be transformed when formatted for raw query
    public Conditional literal()
    {
        isLiteral   =   true;
        return this;
    }
    
    
    //Returns the conditional SQL formatted
    //Connectives are added at later build states
    @Override
    public String toString()
    {
       String outVal        =   (isLiteral)? Model.makeLiteral(value) : value;
       String output        =   MessageFormat.format("WHERE {0} {1} {2}", column, operator, outVal);
       return output;
    }
}
