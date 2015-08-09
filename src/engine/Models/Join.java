
package engine.Models;


public class Join 
{    
    private String join_from;
    
    private String join_to;
    
    private String from_pk;
    
    private String to_pk;
    
    public Join(String join_from, String join_to)
    {
        this(join_from, join_to, Model.DEFAULT_PK_NAME, Model.DEFAULT_PK_NAME);
    }
    
    public Join(String join_from, String join_to, String from_pk, String to_pk)
    {
        this.join_from  =   join_from;
        this.join_to    =   join_to;
        this.from_pk    =   from_pk;
        this.to_pk      =   to_pk;
    }
    
    public String getFromTable()
    {
        return join_from;
    }
    
    public String getToTable()
    {
        return join_to;
    }
    
    public String getFromPK()
    {
        return from_pk;
    }
    
    public String getToPK()
    {
        return to_pk;
    }
}
