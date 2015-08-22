
package engine.views.cui;

import engine.controllers.ControllerMessage;
import engine.core.RouteHandler;
import engine.views.AbstractView;
import engine.views.View;
import engine.views.cui.Utilities.CUITextTools;

public class MyClassesView extends AbstractView
{
    public MyClassesView()
    {
        this(new ControllerMessage());
    }
    
    public MyClassesView(ControllerMessage messages)
    {
        super
        (
                messages, 
                "My classes", 
                "View and manage your classes", 
                "/" + "students"//Agent.getActiveSession().getUser().get("USERNAME").getNonLiteralValue() + "/students/"
        );
    }
    
    @Override
    public void display()
    {
        super.display();
        CUITextTools.responseToTable(messages.getData());
    }
    
    @Override
    protected String getCommandsFile() 
    {
        return "/engine/config/listeners/MyClassesListener.json";
    }
    
    public static void main(String[] args)
    {
        View view   =   RouteHandler.go("getMyClasses", null);
        view.display();
    }
    
}
