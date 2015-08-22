
package engine.views.cui;

import engine.controllers.ControllerMessage;
import engine.views.AbstractView;

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
    }
    
    @Override
    protected String getCommandsFile() 
    {
        return "/engine/config/listeners/RegisterListener.json";
    }
    
}
