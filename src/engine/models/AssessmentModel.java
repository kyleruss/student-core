
package engine.models;

public class AssessmentModel extends Model
{
    public AssessmentModel()
    {
        super();
    }
    
    public AssessmentModel(Object id)
    {
        super(id);
    }

    @Override
    protected void initTable() 
    {
        table       =   "assessment";
        primaryKey  =   "id";
    }
    
}
