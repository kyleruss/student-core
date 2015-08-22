
package engine.models;


public class DepartmentModel extends Model
{
    public DepartmentModel()
    {
        super();
    }
    
    public DepartmentModel(Object id)
    {
        super(id);
    }
    
    @Override
    protected void initTable() 
    {
        table       =   "department";
        primaryKey  =   "id";
    }
    
}
