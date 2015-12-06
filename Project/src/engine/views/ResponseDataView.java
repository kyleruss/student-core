//====================================
//	Kyle Russell
//	jdamvc
//	ResponseDataView
//====================================

package engine.views;

import engine.controllers.ControllerMessage;
import engine.core.Path;
import engine.views.cui.Utilities.CUITextTools;


public class ResponseDataView implements DataView
{
    protected boolean responseStatus;
    protected String responseMessage;
    protected ControllerMessage responseData;
    protected int accessLevel;
    protected Path path;
    
    public ResponseDataView(String responseMessage, boolean responseStatus)
    {
        this(responseMessage, responseStatus, new ControllerMessage(), 0);
    }
    
    public ResponseDataView(String responseMessage, boolean responseStatus, ControllerMessage responseData, int accessLevel)
    {
        this.responseMessage    =   responseMessage;
        this.responseStatus     =   responseStatus;
        this.responseData       =   responseData;
        this.accessLevel        =   accessLevel;
    }
    
    @Override
    public boolean getResponseStatus()
    {
        return responseStatus;
    }

    @Override
    public String getResponseMessage() 
    {
        final int SUCCESS_COLOUR    =   CUITextTools.GREEN;
        final int FAIL_COLOUR       =   CUITextTools.RED;
        
        return (responseStatus)? 
        CUITextTools.changeColour(responseMessage, SUCCESS_COLOUR) : CUITextTools.changeColour(responseMessage, FAIL_COLOUR);
    }
    
    public String getRawResponseMessage()
    {
        return responseMessage;
    }
    
    @Override
    public void setPath(Path path)
    {
        this.path   =   path;
    }
    
    @Override
    public Path getPath()
    {
        return path;
    }

    @Override
    public ControllerMessage getResponseData()
    {
        return responseData;
    }

    @Override
    public String getJson() 
    {
        return "";
    }

    @Override
    public void display() 
    {
        System.out.println(getJson());
    }

    @Override
    public int getAccessLevel() 
    {
        return accessLevel;
    }

    @Override
    public ControllerMessage getMessages() 
    {
        return responseData;
    }

    @Override
    public void fire(String command, Object instance) {}

    @Override
    public View getPrevView() 
    {
        return null;
    }

    @Override
    public View getNextView() 
    {
        return null;
    }

    @Override
    public void setPrevView(View prevView) {}

    @Override
    public void setNextView(View nextView) {}
}
