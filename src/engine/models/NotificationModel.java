
package engine.models;

import com.google.gson.JsonArray;
import engine.core.ExceptionOutput;
import java.sql.SQLException;


public class NotificationModel extends Model
{
    public NotificationModel()
    {
        super();
    }
    
    public NotificationModel(Object id)
    {
        super(id);
    }
    
    @Override
    public void initTable()
    {
        table       =   "notifications";
        primaryKey  =   "id";
    }
    
    public static boolean sendNotification(String user, String message) throws SQLException
    {
        NotificationModel notification  =   new NotificationModel();
        notification.set("username", user);
        notification.set("content", message);
        
        return notification.save();

    }
    
    public static int getNumUnreadNotifications(String user)
    {
        try
        {
            JsonArray results =  new NotificationModel().builder()
                    .where("username", "=", user, true)
                    .where("unread", "=", "true")
                    .get();

            if(results != null) 
                return results.size() - 1;

            else return 0;
        }
        
        catch(SQLException e)
        {
            return 0;
        }
    }
    
    public static JsonArray getUserNotifications(String user)
    {
        try
        {
            return new NotificationModel().builder()
                                    .where("username", "=", user, true)
                                    .get();
        }
        
        catch(SQLException e)
        {
            return new JsonArray();
        }
    }
    
    public static String readNotificationMessage(int notificationId)
    {
        NotificationModel notification  =   new NotificationModel(notificationId);

        String message  =   (String) notification.get("content").getNonLiteralValue();
        boolean unread  =   (boolean) notification.get("unread").getColumnValue();

        if(unread)
        {
            notification.set("unread", false);
            notification.update();
        }

        return message;
    }
}
