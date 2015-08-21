
package engine.models;


public class EmergencyContactModel extends Model
{

    @Override
    protected void initTable() 
    {
        table       =   "emergency_contact";
        primaryKey  =   "id";
    }
    
}
