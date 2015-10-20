
package engine.models;

public class ClassAnnouncementsModel extends Model 
{
    
    public ClassAnnouncementsModel()
    {
        super();
    }
    
    public ClassAnnouncementsModel(Object id)
    {
        super(id);
    }

    @Override
    protected void initTable()
    {
        table       =   "class_announcements";
        primaryKey  =   "id";   
    }
    
}
