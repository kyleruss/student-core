//====================================
//  KYLE RUSSELL
//  13831056
//  PDC Project
//====================================

package engine.views.cui;

import engine.controllers.ControllerMessage;
import engine.core.Agent;
import engine.views.AbstractView;


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
            "Manage user accounts, student grades and more"
        );
    }
    
    public void showStudents()
    {
        Agent.setView("getStudents");
    }
    
    public void showHouses()
    {
        System.out.println("houses!");
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
