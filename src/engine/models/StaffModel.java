//====================================
//  KYLE RUSSELL
//  13831056
//  PDC Project
//====================================

package engine.models;


public class StaffModel extends Model
{
    public StaffModel()
    {
        super();
    }
    
    public StaffModel(Object id)
    {
        super(id);
    }
    
    @Override
    protected void initTable() 
    {
        table       =   "staff";
        primaryKey  =   "user_id";
    }
    
}
