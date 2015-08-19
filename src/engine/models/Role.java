
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
        Role role   =   new Role("" + 1);
        System.out.println(role.get("ID").isLiteral());
      /*  role.set("name", "Teacher");
        if(role.update());
            System.out.println("updated!"); */
    }
    
}
