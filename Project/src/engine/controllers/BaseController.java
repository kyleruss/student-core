//====================================
//	Kyle Russell
//	jdamvc
//	BaseController
//====================================

package engine.controllers;

import engine.core.Path;
import engine.views.View;

public class BaseController extends Controller 
{
    public BaseController(Path path)
    {
        super(path);
    }
    
    public BaseController(ControllerMessage postData, Path path)
    {
        super(postData, path);
    }
    
    public BaseController(ControllerMessage postData, RequestType requestType, Path path)
    {
        super(postData, requestType, path);
    }
    
    
    //Returns an error view
    //The passed errorMessage is displayed in the views title
    //GUI Only - CUI should report errors normally
    public View getErrorView(String errorMessage)
    {
        ControllerMessage data  =   new ControllerMessage();
        data.add("errorMessage", errorMessage);
        return prepareView(new engine.views.gui.layout.ErrorView(data));
    }
}
