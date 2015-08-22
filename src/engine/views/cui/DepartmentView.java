package engine.views.cui;

import engine.controllers.ControllerMessage;
import engine.core.Agent;
import engine.core.RouteHandler;
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
                message.getData().get(1).getAsJsonObject().get("name".toUpperCase()).getAsString() + " Department", 
                "Access and manage your staff department", 
                "/"  + "department"//Agent.getActiveSession().getUser().get("USERNAME").getNonLiteralValue() + "/department/"
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
    
    
    public static void main(String[] args)
    {
        RouteHandler.go("getMyDepartment", null).display();
    }
}
