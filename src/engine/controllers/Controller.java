//====================================
//  KYLE RUSSELL
//  13831056
//  PDC Project
//====================================

package engine.controllers;

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
    
    //Create general controller session with GET request
    public Controller()
    {
        this(new ControllerMessage(), RequestType.GET);
    }
    
    //Create a controller session with POST request and respective input data
    public Controller(ControllerMessage postData)
    {
        this(postData, RequestType.POST);
    }
    
    //Creates controller with specific request type and input params
    //Passing input data is not restricted to POST
    public Controller(ControllerMessage postData, RequestType requestType)
    {
        this.postData       =   postData;
        this.requestType    =   requestType;
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
    
    //Checks post data such that it must contain all expected keys
    public boolean validatePostData(String[] expectedNames)
    {
        if(!hasPostData()) return false;
        else
        {
            for(String name : expectedNames)
                if(!postData.messageExists(name)) return false;
            return true;
        }
    }
}
