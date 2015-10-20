package engine.models;

public class DeptAnnouncementsModel extends Model 
{
    
    public DeptAnnouncementsModel()
    {
        super();
    }
    
    public DeptAnnouncementsModel(Object id)
    {
        super(id);
    }

    @Override
    protected void initTable() 
    {
        table   =   "dept_announcements";
    }
    
}
