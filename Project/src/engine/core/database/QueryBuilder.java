//====================================
//	Kyle Russell
//	jdamvc
//	QueryBuilder
//====================================

package engine.core.database;

import com.google.gson.JsonArray;
import engine.core.JsonParser;
import engine.core.DataConnector;
import engine.core.ExceptionOutput;
import engine.core.database.Join.JoinType;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;


//--------------------------------
//          QUERY BUILDER
//--------------------------------
//- Create and modify SQL queries 
//- Uses convenient chaning techniques 
//- Capable of building queries with:
//Selecting, conditionals, grouping, ordering, joins,
//Limiting & offsets and more + custom additions

public class QueryBuilder 
{
    //QueryBuilder's working Query
    private Query query;
        
    
    //Create a builder that maps to an existing table
    //Models builder() uses this constructor
    public QueryBuilder(String table)
    {
        this(new Query(table));
    }
    
    //Create a builder from an existing to build on
    public QueryBuilder(Query query)
    {
        this.query =   query;
    }
    
    //Adds a where clause without a non-literal value to the query 
    //See where(String, String, String boolean) for passing literals
    public QueryBuilder where(String column, String operator, String value)
    {
        return where(column, operator, value, false);
    }
    
    public QueryBuilder orWhere(String column, String operator, String value)
    {
       return where(column, operator, value, false, false);
    }
    
    public QueryBuilder orWhere(String column, String operator, String value, boolean isLiteral)
    {
        return where(column, operator, value, false, true);
    }
    
    //Adds a where clause with a literal value
    //Where clauses can be changed and Query resolves conjunctions
    public QueryBuilder where(String column, String operator, String value, boolean isLiteral)
    {
        return where(column, operator, value, isLiteral, true);
    }
    
    public QueryBuilder where(String column, String operator, String value, boolean isLiteral, boolean isConjunction)
    {
        Conditional condition   =   new Conditional(column, operator, value, isConjunction);
        
        //Makes conditional value a literal if isLiteral passed true
        if(isLiteral) condition.literal();
        
        query.addConditional(condition);
        return this;
    }
    
    //Adds a select statement to the query
    //Pass the column name you wish to select
    //For non DML queries the default Selecttion(*) is used
    public QueryBuilder select(String selection)
    {
        query.addSelection(new Selection(selection));
        return this;
    }
    
    public QueryBuilder select(String selection, String selectionAlias)
    {
        if(selectionAlias.split("\\s").length > 1)
            selectionAlias  = "\"" + selectionAlias + "\"";
            
        query.addSelection(new Selection(selection, selectionAlias));
        return this;
    }
    
    //Adds selections.length many selections to the query
    //Query resolves concatinationa and multiple selects
    //Pass an array of columns you want to select
    public QueryBuilder select(String[] selections)
    {
        Selection[] selection_arr   =   new Selection[selections.length];
        for(int i = 0; i < selection_arr.length; i++)
            selection_arr[i] = new Selection(selections[i]);
        query.addSelections(selection_arr);
        return this;
    }
    
    
    //Adds a joinType join to the query
    //Join specifies a link between tables table_from and table_to
    //Joins can be chained to achieve multiple join levels
    //Joins use the default column ID names as the FK to join
    public QueryBuilder join(String table_from, String table_to, JoinType joinType)
    {
        Join join   =   new Join(table_from, table_to, joinType);
        query.addJoin(join);
        return this;
    }
    
    //Adds a joinType join to the query
    //If either tables don't use default FK and PK column names
    //then pass their join from and join to FK column names 
    public QueryBuilder join(String table_from, String table_to, String from_pk, String to_pk, JoinType joinType)
    {
        Join join   =   new Join(table_from, table_to, from_pk, to_pk, joinType);
        query.addJoin(join);
        return this;
    }
    
    public QueryBuilder join(Join join)
    {
        query.addJoin(join);
        return this;
    }
    
    //Executes and returns a JSON representation
    //of the query results if any
    public JsonArray get() throws SQLException
    {
        return execute();
    }
    
    //Set the query results offset
    //selects only results after offset
    public QueryBuilder offset(int offset)
    {
        String offsetFormat  =   MessageFormat.format("OFFSET {0} ROWS", offset);
        query.addExtra(offsetFormat);
        return this;
    }
    
    //Adds a ordering (ascending/descending) to the query
    //Sorts the results by the passed column name
    //orderBy can be chained to achieve multiple sorting
    public QueryBuilder orderBy(String column)
    {
        String orderFormat  =   MessageFormat.format("ORDER BY {0}", column);
        query.addExtra(orderFormat);
        return this;
    }
    
    //Adds a grouping to the query
    //Groups the result by the passed column\
    //Passing functions such as COUNT(*) is allowed
    public QueryBuilder groupBy(String column)
    {
        if(query.getSelections().isEmpty()) return null;
     
        String selections   =   query.formatSelections().replace(column + ",", "").replace(", " + column, "");
        String groupFormat  =   MessageFormat.format("GROUP BY {0}, {1}", column, selections);
        query.addExtra(groupFormat);
        return orderBy(column);
    }
    
    //Query will only return the first result
    public QueryBuilder first()
    {
        return limit(1);
    }
    
    //Adds a limit to the number of results
    //There will be at most rowLimit rows
    public QueryBuilder limit(int rowLimit)
    {
        String limitFormat  =    MessageFormat.format("FETCH FIRST {0} ROWS ONLY", rowLimit);
        query.addExtra(limitFormat);
        return this;
    }
    
    //Builds the queries select statements
    //Query handles formatting of select columns
    private String buildSelect()
    {
        String query_str    =   MessageFormat.format("SELECT {0} FROM {1}", query.formatSelections(), query.getTable());
        return query_str;
    }
    
    //Builds the queries conditional (where clauses)
    private String buildConditionals()
    {
        return query.formatConditionals(query.getConditionals());
    }
    
    //Builds any query extras
    private String buildExtras()
    {
        return query.formatExtras();
    }
    
    private String buildJoins()
    {
        return query.formatJoins();
    }
    
    public int getNumPages(int numResults)
    {
        try
        {
            JsonArray results   =   get();
            if(results == null) return 0;
            
            int totalResults   =   results.size() - 1;
            if(totalResults > 0) return (int) Math.ceil((totalResults * 1.0) / numResults);
            else return 0;
        }
        
        catch(SQLException e)
        {
            ExceptionOutput.output(e.getMessage(), ExceptionOutput.OutputType.DEBUG);
            return 0;
        }
    }
    
    private int getPageOffset(int pageNum, int numResults, int numPages)
    {
        if(pageNum > numPages) pageNum = numPages;
        
        int index   =   (pageNum > 0)? (pageNum - 1) : 0;
        int offset  =   numResults * index;
        return offset;
    }
    
    public QueryBuilder setPage(int page, int numResults) throws SQLException
    {
        int numPages    =   getNumPages(numResults);
        int offset      =   getPageOffset(page, numResults, numPages);
        return offset(offset).limit(numResults);
    }
    
    //Builds and returns the raw query of the QueryBuilder
    //Building can resume but cannot be chaned after
    public String build()
    {
        String query_raw;
        String db_operation =   buildSelect();
        String conditionals =   buildConditionals();
        String extras       =   buildExtras();
        String joins        =   buildJoins();
        
        
        //Builds order: Db operations => conditionals => joins => extras
        query_raw   =   MessageFormat.format("{0} {1} {2} {3}", db_operation, conditionals, joins, extras);
        return query_raw;
    }
    
    //Returns th built raw query
    @Override
    public String toString()
    {
        return build();
    }
    
    
    //Executes the built query and returns results
    //Building ends here and may no longer be chained
    //Save the query and create new instance if necessary
    //Use get() if you need a JSON response
    public JsonArray execute() throws SQLException
    {
        //Build query
        String query_str    =   build();
        
        //Connect and execute query
        try(DataConnector conn  =   new DataConnector())
        {
            conn.execute(query_str);

            //Return results
            ResultSet results   =   conn.getResults();
            return JsonParser.resultsToJson(results);
        }
        
    }
}  