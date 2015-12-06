//====================================
//	Kyle Russell
//	jdamvc
//	View
//====================================

package engine.views;

import engine.controllers.ControllerMessage;
import engine.core.CommandExecute;
import engine.core.Path;

//------------------------------------
//              VIEW
//------------------------------------
//- Views represent dynamically loadable displays
//- They present visually the face of the routes

public interface View extends CommandExecute, ViewExplorer
{
    //Draws or outputs the view to the user
    public void display();
    
    //Returns the access level required for the view
    //Users with insufficient privileges should be denied 
    public int getAccessLevel();
    
    //Returns the messages passed to the view
    //These can be from the controller 
    public ControllerMessage getMessages();
    
    public void setPath(Path path);
    
    public Path getPath();
}
