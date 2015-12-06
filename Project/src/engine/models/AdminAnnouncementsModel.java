//====================================
//  KYLE RUSSELL
//  13831056
//  PDC Project
//====================================

package engine.models;

import com.google.gson.JsonArray;
import java.sql.SQLException;

public class AdminAnnouncementsModel extends Model 
{
    public AdminAnnouncementsModel()
    {
        super();
    }
    
    public AdminAnnouncementsModel(Object id)
    {
        super(id);
    }

    @Override
    protected void initTable() 
    {
        table       =   "admin_announcements";
        primaryKey  =   "id";   
    }

    public static JsonArray getAllAnnouncements()
    {
        try
        {
            return new AdminAnnouncementsModel().builder()
                    .get();
        }
        
        catch(SQLException e)
        {
            return new JsonArray();
        }
    }
}
