
package engine.models;


public class EmergencyContactModel extends Model
{
    public EmergencyContactModel()
    {
        super();
    }

    public EmergencyContactModel(Object id)
    {
        super(id);
    }
    
    
    @Override
    protected void initTable() 
    {
        table       =   "emergency_contact";
        primaryKey  =   "id";
    }
    
}
