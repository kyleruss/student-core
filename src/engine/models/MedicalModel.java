
package engine.models;


public class MedicalModel extends Model 
{
    public MedicalModel()
    {
        super();
    }
    
    public MedicalModel(Object id)
    {
        super(id);
    }

    @Override
    protected void initTable() 
    {
        table       =   "medical";
        primaryKey  =   "id";
    }
    
}
