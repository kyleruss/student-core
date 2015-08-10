
package engine.core.database;

import engine.core.DataConnector;
import java.sql.SQLException;
import java.text.MessageFormat;

public class QueryBuilder 
{
    public static final String TABLE_KEY    =   "table";
    public static final String TYPE_KEY     =   "query_type";
    public static final String WHERE_KEY    =   "conditions";
    public static final String JOIN_KEY     =   "joins";
    public static final String SELECT_KEY   =   "filters";
    
    private final Query query;
    
    
    public QueryBuilder(String table)
    {
        query  =   new Query(table);
     //   query.addSelections(new String[] {"name", "email", "date"});
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
    
    private String buildSelect()
    {
        String query_str    =   MessageFormat.format("SELECT {0} FROM {1}", query.formatSelections(), query.getTable());
        return query_str;
    }
    
    private String buildConditionals()
    {
        return query.formatConditionals();
    }
    
    public String build()
    {
        String query_raw;
        String db_operation =   "";
        String conditionals =   "";
        
        switch(query.getType())
        {
            case INSERT:
            case SELECT: 
                db_operation = buildSelect();
                break;
            case DELETE:
        }
        
        query_raw   =   MessageFormat.format("{0} {1}", db_operation, conditionals);
        return query_raw;
    }
    
    
    public boolean execute()
    {
        String query_str    =   build();
        try(DataConnector conn =   new DataConnector())
        {
            conn.execute(query_str);
            return true;
        }
        
        catch(SQLException e)
        {
            System.out.println("[SQL EXCEPTION ON EXECUTE] " + e.getMessage());
            return false;
        }
    }
    
    public static void main(String[] args)
    {
        QueryBuilder q  =   new QueryBuilder("dasd");
        q.where(TYPE_KEY, TYPE_KEY, TYPE_KEY).where(TYPE_KEY, TYPE_KEY, TYPE_KEY);
    }
}