
package engine.models;


public class Role extends Model
{
    public Role()
    {
        super();
    }
    
    public Role(String id)
    {
        super(id);
    }

    @Override
    protected void initTable() 
    {
        table       =   "role";
        primaryKey  =   "id";   
    }
    
    public static void main(String[] args)
    {
        Role role   =   new Role("3");
        role.set("name", "bla");
        if(role.update()) System.out.println("updated!");

    }
    
}
