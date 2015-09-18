//====================================
//  KYLE RUSSELL
//  13831056
//  PDC Project
//====================================

package engine.views.cui;

import engine.controllers.ControllerMessage;
import engine.views.DataView;
import engine.views.View;
import engine.views.cui.Utilities.CUITextTools;


public class ResponseDataView implements DataView
{
    protected boolean responseStatus;
    protected String responseMessage;
    protected ControllerMessage responseData;
    protected int accessLevel;
    
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
