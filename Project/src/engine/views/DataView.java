//====================================
//	Kyle Russell
//	jdamvc
//	DataView
//====================================

package engine.views;

import engine.controllers.ControllerMessage;

//------------------------------------
//              DATAVIEW
//------------------------------------
//- DataViews are responses from controllers
//- They hold the response status, message and data
//as well as a Json representation of the data
//- These are typically used for DML to confirm they were successful

public interface DataView extends View
{
    //Returns the status of the response
    //True: successful
    //False: failed
    public boolean getResponseStatus();
    
    //Returns the response message
    public String getResponseMessage();
    
    //Returns the data from the controller
    public ControllerMessage getResponseData();
    
    //Returns a json representation of the data
    public String getJson();
    
}
