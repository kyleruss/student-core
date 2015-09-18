//====================================
//  KYLE RUSSELL
//  13831056
//  PDC Project
//====================================

package engine.views;

import engine.controllers.ControllerMessage;
import engine.core.CommandExecute;

public interface View extends CommandExecute, ViewExplorer
{
    
    public void display();
    
    public int getAccessLevel();
    
    public ControllerMessage getMessages();
}
