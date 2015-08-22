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
                "Manage personal profile, administration and more", 
                "/"  + Agent.getActiveSession().getUser().get("USERNAME").getNonLiteralValue() + "/home/"
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