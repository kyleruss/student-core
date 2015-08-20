package engine.views;

import engine.controllers.ControllerMessage;
import engine.core.CommandExecute;

public interface View extends CommandExecute
{
    
    public void display();
    
    public int getAccessLevel();
    
    public ControllerMessage getMessages();
}
