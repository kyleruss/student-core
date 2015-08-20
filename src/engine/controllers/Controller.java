//####################################
//  KYLE RUSSELL
//  13831056
//  PDC Project
//####################################

package engine.controllers;

public abstract class Controller
{
    public enum RequestType
    {
        GET,
        POST
    }
    
    protected ControllerMessage postData;
    protected RequestType requestType;
    
    public Controller()
    {
        this(new ControllerMessage(), RequestType.GET);
    }
    
    public Controller(ControllerMessage postData)
    {
        this(postData, RequestType.POST);
    }
    
    public Controller(ControllerMessage postData, RequestType requestType)
    {
        this.postData       =   postData;
        this.requestType    =   requestType;
    }
    
    protected ControllerMessage getPostData()
    {
        return postData;
    }
    
    public RequestType getRequestType()
    {
        return requestType;
    }
    
    public boolean hasPostData()
    {
        return postData.hasMessages();
    }
    
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
