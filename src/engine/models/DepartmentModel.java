
package engine.models;


public class DepartmentModel extends Model
{

    @Override
    protected void initTable() 
    {
        table       =   "department";
        primaryKey  =   "id";
    }
    
}
