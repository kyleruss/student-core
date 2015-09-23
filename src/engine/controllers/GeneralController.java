//====================================
//  KYLE RUSSELL
//  13831056
//  PDC Project
//====================================

package engine.controllers;

import engine.core.Path;
import engine.views.View;
import engine.views.cui.HomeView;

public class GeneralController extends Controller
{
    public GeneralController(Path path)
    {
        super(path);
    }
    
    public GeneralController(ControllerMessage postData, Path path)
    {
        super(postData, path);
    }
    
    public GeneralController(ControllerMessage postData, RequestType requestType, Path path)
    {
        super(postData, requestType, path);
    }
    
    public View getHome()
    {
        return prepareView(new HomeView());
    }
}
