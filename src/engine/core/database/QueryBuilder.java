
package engine.core.database;

import engine.core.DataConnector;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;

public class QueryBuilder 
{
    private final Query query;
    
    
    public QueryBuilder(String table)
    {
        this(new Query(table));
    }
    
    public QueryBuilder(Query query)
    {
        this.query =   query;
    }
    
    public QueryBuilder where(String column, String operator, String value)
    {
        Conditional condition   =   new Conditional(column, operator, value);
        query.addConditional(condition);
        return this;
    }
    
    public QueryBuilder select(String selection)
    {
        query.addSelection(new Selection(selection));
        return this;
    }
    
    public QueryBuilder select(String[] selections)
    {
        Selection[] selection_arr   =   new Selection[selections.length];
        for(int i = 0; i < selection_arr.length; i++)
            selection_arr[i] = new Selection(selections[i]);
        query.addSelections(selection_arr);
        return this;
    }
    
    
    public QueryBuilder join(String table_from, String table_to)
    {
        Join join   =   new Join(table_from, table_to);
        query.addJoin(join);
        return this;
    }
    
    public QueryBuilder join(String table_from, String table_to, String from_pk, String to_pk)
    {
        Join join   =   new Join(table_from, table_to, from_pk, to_pk);
        query.addJoin(join);
        return this;
    }
    
    public String get()
    {
        return query.getJson();
    }
    
    public QueryBuilder offset(int offset)
    {
        String offsetFormat  =   MessageFormat.format("OFFSET {0} ROWS", offset);
        query.addExtra(offsetFormat);
        return this;
    }
    
    public QueryBuilder orderBy(String column)
    {
        String orderFormat  =   MessageFormat.format("ORDER BY {0}", column);
        query.addExtra(orderFormat);
        return this;
    }
    
    public QueryBuilder groupBy(String column)
    {
        String groupFormat  =   MessageFormat.format("GROUP BY {0}", column);
        query.addExtra(groupFormat);
        return this;
    }
    
    public QueryBuilder first()
    {
        return limit(1);
    }
    
    public QueryBuilder limit(int rowLimit)
    {
        String limitFormat  =    MessageFormat.format("FETCH FIRST {0} ROWS ONLY", rowLimit);
        query.addExtra(limitFormat);
        return this;
    }
    
    private String buildSelect()
    {
        String query_str    =   MessageFormat.format("SELECT {0} FROM {1}", query.formatSelections(), query.getTable());
        return query_str;
    }
    
    private String buildConditionals()
    {
        return query.formatConditionals();
    }
    
    private String buildExtras()
    {
        return query.formatExtras();
    }
    
    public String build()
    {
        String query_raw;
        String db_operation =   "";
        String conditionals =   buildConditionals();
        String extras       =   buildExtras();
        
        switch(query.getType())
        {
            case INSERT:
            case SELECT: 
                db_operation = buildSelect();
                break;
            case DELETE:
        }
        
        query_raw   =   MessageFormat.format("{0} {1} {2}", db_operation, conditionals, extras);
        return query_raw;
    }
    
    
    public ResultSet execute() throws SQLException
    {
        String query_str    =   build();
        DataConnector conn  =   new DataConnector();
        
        conn.execute(query_str);
        conn.close();
        
        ResultSet results   =   conn.getResults();
        return results;
    }
}