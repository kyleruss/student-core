package engine.models;

import com.google.gson.JsonArray;
import java.sql.SQLException;

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
    
    public static JsonArray getDeptAnnouncementsFor(int deptid)
    {
        try
        {
            JsonArray results   =   new DeptAnnouncementsModel().builder()
                                    .where("dept_id", "=", "" + deptid)
                                    .get();
            
            return results;
        }
        
        catch(SQLException e)
        {
            return new JsonArray();
        }
    }
}
