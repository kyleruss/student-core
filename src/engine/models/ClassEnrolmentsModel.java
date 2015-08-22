
package engine.models;


public class ClassEnrolmentsModel extends Model
{
    public ClassEnrolmentsModel()
    {
        super();
    }
    
    public ClassEnrolmentsModel(Object id)
    {
        super(id);
    }
    
    @Override
    protected void initTable() 
    {
        table       =   "class_enrolments";
        primaryKey  =   "id";
    }
    
}
