
package engine.models;


public class AssessmentSubmissionsModel extends Model
{
    public AssessmentSubmissionsModel()
    {
        super();
    }
    
    public AssessmentSubmissionsModel(Object id)
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
