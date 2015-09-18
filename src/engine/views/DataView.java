//====================================
//  KYLE RUSSELL
//  13831056
//  PDC Project
//====================================

package engine.views;

import engine.controllers.ControllerMessage;


public interface DataView extends View
{
    public boolean getResponseStatus();
    
    public String getResponseMessage();
    
    public ControllerMessage getResponseData();
    
    public String getJson();
    
}
