package engine.views.cui;

import engine.controllers.ControllerMessage;
import engine.core.Agent;
import engine.views.AbstractView;


public class DepartmentView extends AbstractView
{
    
    public DepartmentView()
    {
        this(new ControllerMessage());
    }
    
    public DepartmentView(ControllerMessage message)
    {
        super
        (
                message,
                "Department", 
                "Access and manage your staff department", 
                "/"  + Agent.getActiveSession().getUser().get("USERNAME").getNonLiteralValue() + "/department/"
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
        return "/engine/config/listeners/DepartmentListener.json";
    }
    
}
