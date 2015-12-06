//====================================
//  KYLE RUSSELL
//  13831056
//  PDC Project
//====================================

package engine.models;

import com.google.gson.JsonArray;
import java.sql.SQLException;

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
    
    public static JsonArray getClassAnnouncementsFor(int classID)
    {
        try
        {
            JsonArray results   =   new ClassAnnouncementsModel().builder()
                                    .where("class_id", "=", "" +  classID)
                                    .get();
            return results;
        }
        
        catch(SQLException e)
        {
            return new JsonArray();
        }
    }
}
