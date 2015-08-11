
package engine.Models;


public class User extends Model
{
    
    public User()
    {
        super();
    }
    
    public User(String id)
    {
        super(id);
    }
    
    @Override
    protected void initTable()
    {
        table       =   "app.testtable";
        primaryKey  =   "id";
    }
    
    public static void main(String[] args)
    {
        User user   =   new User();
        System.out.println("RESULT: " + user.fetchExisting("1"));
    }
}
