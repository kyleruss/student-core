//====================================
//	Kyle Russell
//	jdamvc
//	Controller
//====================================

package engine.controllers;

import engine.core.Path;
import engine.views.View;


//------------------------------------------
//             CONTROLLER
//------------------------------------------
//- Controller provides the bridge between routes and views
//- Methods in Controller children should interface routes


public abstract class Controller
{
    //Defines the type of request for a controller call
    public enum RequestType
    {
        GET, //Requesting data from a source
        POST //Submitting data to a source
    }
    
    protected ControllerMessage postData; //the passed input data for a post request
    protected RequestType requestType; //immediate request type (GET, POST)
    protected Path path; //the controller sessions path
    
    public Controller()
    {
        this(null);
    }
    
    //Create general controller session with GET request
    public Controller(Path path)
    {
        this(new ControllerMessage(), RequestType.GET, path);
    }
    
    //Create a controller session with POST request and respective input data
    public Controller(ControllerMessage postData, Path path)
    {
        this(postData, RequestType.POST, path);
    }
    
    //Creates controller with specific request type and input params
    //Passing input data is not restricted to POST
    public Controller(ControllerMessage postData, RequestType requestType, Path path)
    {
        this.postData       =   postData;
        this.requestType    =   requestType;
        this.path           =   path;
    }
    
    //Returns the controller sessions input data
    //Data may be empty if GET request
    protected ControllerMessage getPostData()
    {
        return postData;
    }
    
    //Returns the controllers request type
    public RequestType getRequestType()
    {
        return requestType;
    }
    
    //Returns true if the controller session has input data
    public boolean hasPostData()
    {
        return postData.hasMessages();
    }

    //Returns the path that was used to create the controller
    public Path getPath()
    {
        return path;
    }
    
    public View prepareView(View view)
    {
        view.setPath(path);
        return view;
    }
    
    //Checks post data such that it must contain all expected keys
    public boolean validatePostData(String[] expectedNames)
    {
        if(!hasPostData()) return false;
        else
        {
            boolean validate = true;
            for(String name : expectedNames)
                if(!postData.messageExists(name)) 
                    validate = false;
            
            return validate;
        }
    }
}
