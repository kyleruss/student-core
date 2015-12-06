//====================================
//	Kyle Russell
//	StudentCore
//	NotificationView
//====================================

package engine.views.cui;

import com.google.gson.JsonArray;
import engine.core.Agent;
import engine.core.ExceptionOutput;
import engine.models.NotificationModel;
import engine.views.AbstractView;
import engine.views.cui.Utilities.CUITextTools;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NotificationView extends AbstractView
{
    @Override
    public void display()
    {
        super.display();
        JsonArray notifications =   NotificationModel.getUserNotificationsFiltered(Agent.getActiveSession().getUser()
                                    .get("username").getNonLiteralValue().toString());
        if(notifications != null && notifications.size() > 1)
        {
            int unreadNotifications   =   NotificationModel.getNumUnreadNotifications(Agent.getActiveSession().getUser()
                                          .get("username").getNonLiteralValue().toString());
            System.out.println(CUITextTools.changeColour("You have " + unreadNotifications + " unread notification(s)", CUITextTools.GREEN));
            CUITextTools.responseToTable(notifications);
        }
        
        else System.out.println(CUITextTools.changeColour("You have no notifications", CUITextTools.RED));
    }
    
    public void readNotification()
    {
        List<String> fieldTitles    =   new ArrayList<>();
        fieldTitles.add(CUITextTools.createFormField("Notification ID", "What is the ID of the notification you want to view?"));
        
        List<String> fieldKeys      =   new ArrayList<>();
        fieldKeys.add("notificationID");
        
        String[] headers    =   { "Notification ID" };
        Map<String, String> inputData   =   CUITextTools.getFormInput(fieldTitles, fieldKeys, headers);
        
        int notificationID  =   Integer.parseInt(inputData.get("notificationID"));
        displayNotification(notificationID);
    }
    
    public void displayNotification(int notificationID)
    {
        String message  =   NotificationModel.readNotificationMessage(notificationID);
        if(message == null || message.equals(""))
            ExceptionOutput.output("Notification content not found", ExceptionOutput.OutputType.MESSAGE);
        else
        {
            System.out.println(CUITextTools.changeColour("Reading notification [" + notificationID + "]:", CUITextTools.GREEN));
            System.out.println(message);
        }
    }
    
    @Override
    protected String getCommandsFile() 
    {
        return "/engine/config/listeners/NotificationListener.json";
    }
    
}
