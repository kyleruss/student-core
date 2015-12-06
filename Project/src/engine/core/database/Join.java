//====================================
//	Kyle Russell
//	jdamvc
//	Join
//====================================

package engine.core.database;

import engine.config.DatabaseConfig;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;


//--------------------------------
//              JOIN
//--------------------------------
//- Represents a join relationship
//- Supports left & right (outer) and inner joins

public class Join 
{    
    
    //JoinType enum identifies join type
    //Support for inner, left and right joins
    //Value holds join syntax 
    
    public enum JoinType
    {
        INNERR_JOIN("INNER JOIN"), //Basic join (explicit inner)
        LEFT_JOIN("LEFT OUTER JOIN"), //Left join (explicit outer)
        RIGHT_JOIN("RIGHT OUTER JOIN"); //Right join (explicit outer)
        
        private final String joinSyntax;
        
        JoinType(String joinSyntax)
        {
            this.joinSyntax    =   joinSyntax;
        }
        
        //Returns the join syntax
        //Call for building query
        public String getJoinSyntax()
        {
            return joinSyntax;
        }
    }
    
    //The joining from table
    //This table should house the FK
    private String fromTable;
    
    //The joining/linking table
    private String toTable;
    
    //The FK of the linking (toTable) table 
    //this FK should point to the linking table
    private String fromFK;
    
    //The PK of the linking table 
    private String toPK;
    
    private List<Conditional> joinConditionals;
    
    //The join type
    //Default to inner join 
    private final JoinType joinType;
    
    //Creates a basic join relationship between 2 tables
    //Uses default key ids from conifg and uses inner join
    public Join(String fromTable, String toTable)
    {
        this(fromTable, toTable, JoinType.INNERR_JOIN);
    }
    
    //Creates a join with default key names and specific join type
    public Join(String fromTable, String toTable, JoinType joinType)
    {
        this
        (
            fromTable, 
            toTable, 
            MessageFormat.format("{0}_{1}", toTable, DatabaseConfig.DEFAULT_KEY), 
            DatabaseConfig.DEFAULT_KEY,
            joinType
        );
    }
    
    //Creates a join with non-default key names
    //Still defaults to inner join
    public Join(String fromTable, String toTable, String fromFK, String toPK)
    {
        this(fromTable, toTable, fromFK, toPK, JoinType.INNERR_JOIN);
    }
    
    //Creates a join with non-default key names and specific join type
    public Join(String fromTable, String toTable, String fromPK, String toPK, JoinType joinType)
    {
        this.fromTable          =   fromTable;
        this.toTable            =   toTable;
        this.fromFK             =   fromPK;
        this.toPK               =   toPK;    
        this.joinType           =   joinType;
        this.joinConditionals   =   new ArrayList<>();
    }
    
    //Returns the joins join from table
    public String getFromTable()
    {
        return fromTable;
    }
    
    //Returns the joins joining to/link table
    public String getToTable()
    {
        return toTable;
    }
    
    //Returns the joins from table FK
    public String getFromFK()
    {
        return fromFK;
    }
    
    //Returns the joins to table PK
    public String getToPK()
    {
        return toPK;
    }
    
    //Adds a conditional to the join
    //join conditionals can be chained similalry to normal conditionals
    public Join filter(Conditional condition)
    {
        joinConditionals.add(condition);
        return this;
    }
    
    //Builds and returns the syntax for join ready for query
    @Override
    public String toString()
    {
        //Format columns to include table prefixes
        String colFrom      =   MessageFormat.format("{0}.{1}", fromTable, fromFK); 
        String colTo        =   MessageFormat.format("{0}.{1}", toTable, toPK);
        String conditionals =   (joinConditionals.size() > 0)? Query.formatConditionals(joinConditionals).replace("WHERE", " and ") : "";
        
        //Format: JOIN TYPE [toTable] ON fromTable.fromFK = toTable.toPK
        String syntax    =   MessageFormat.format("{0} {1} ON {2} = {3} {4}", joinType.getJoinSyntax(), toTable, colFrom, colTo, conditionals);
        return syntax;
    }
}
