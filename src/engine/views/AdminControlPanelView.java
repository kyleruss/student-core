
package engine.views;

import engine.controllers.ControllerMessage;
import engine.core.Agent;


public class AdminControlPanelView extends AbstractView
{
    public AdminControlPanelView()
    {
        this(new ControllerMessage());
    }
    
    public AdminControlPanelView(ControllerMessage messages)
    {
        super
        (
                messages, 
                "Administrator control panel", 
                "Manage user accounts, student grades and more", 
                "/" + Agent.getActiveSession().getUser().get("USERNAME").getNonLiteralValue() + "/admincp/"
        );
    }
    
    @Override
    public void display()
    {
        super.display();
    }
    
    @Override
    protected String getCommandsFile()
    {
         return "/engine/config/listeners/AdminCPListener.json";
    }
    
}
