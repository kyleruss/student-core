
package engine.models;

public class ClassesModel extends Model
{
    public ClassesModel()
    {
        super();
    }
    
    public ClassesModel(Object id)
    {
        super(id);
    }

    @Override
    protected void initTable() 
    {
        table       =   "classes";
        primaryKey  =   "id";
    }
    
}
