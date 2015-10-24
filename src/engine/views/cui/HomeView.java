//====================================
//  KYLE RUSSELL
//  13831056
//  PDC Project
//====================================

package engine.views.cui;

import engine.controllers.ControllerMessage;
import engine.core.Agent;
import engine.views.AbstractView;

public class HomeView extends AbstractView
{
    public HomeView()
    {
        this(new ControllerMessage());
    }
    
    public HomeView(ControllerMessage message)
    {
        super
        (
            message,
            "Home", 
            "Personal profile, administration and more" 
        );
    }
    
    @Override
    public void display() 
    {
        super.display();  
    }
    
    public void goAdmincp()
    {
        Agent.setView("getAdmincp");
    }
    
    public void showMyClasses()
    {
        Agent.setView("getMyClasses");
    }
    
    public void showDepartment()
    {
        Agent.setView("getMyDepartment");
    }
    
    public void showNotifications()
    {
        Agent.setView("getNotifications");
    }
    
    @Override
    public String getCommandsFile()
    {
        return "/engine/config/listeners/HomeListener.json";
    } 
    
    @Override
    public int getAccessLevel()
    {
        return 0;
    }
}