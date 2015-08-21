
package engine.views;

import engine.controllers.ControllerMessage;
import engine.core.CommandInterpreter;
import engine.views.cui.Utilities.CUITextTools;


public abstract class AbstractView extends CommandInterpreter implements View
{
    protected ControllerMessage messages;
    
    protected String viewName;
    protected String viewDescription;
    protected String viewLocation;
    
    public AbstractView()
    {
        this(new ControllerMessage());
    }
    
    public AbstractView(String viewName, String viewDescription, String viewLocation)
    {
        this(new ControllerMessage(), viewName, viewDescription, viewLocation);
    }
    
    public AbstractView(ControllerMessage messages)
    {
        this(messages, "Layout", "Layout view", "/");
    }
    
    public AbstractView(ControllerMessage messages, String viewName, String viewDescription, String viewLocation)
    {
        this.messages           =   messages;
        this.viewName           =   viewName;
        this.viewDescription    =   viewDescription;
        this.viewLocation       =   viewLocation;
    }
    
    public String getViewName()
    {
        return viewName;
    }
    
    public String getViewDescription()
    {
        return viewDescription;
    }
    
    public void setViewName(String viewName)
    {
        this.viewName   =   viewName;
    }
    
    public void setViewDescription(String viewDescription)
    {
        this.viewDescription    =   viewDescription;
    }
    
    public void pass(String messageName, String message)
    {
        messages.add(messageName, message);
    }
    
    @Override
    public ControllerMessage getMessages()
    {
        return messages;
    }
    
    @Override
    public void display()
    {
       String headerMain    =   CUITextTools.drawLargeHeader(viewName, viewDescription, CUITextTools.GREEN, CUITextTools.CYAN);
       String cmdSubheader  =   CUITextTools.drawSubHeader("Commands", CUITextTools.PLAIN, CUITextTools.GREEN, "=");
       String breadcrumb    =   CUITextTools.underline(CUITextTools.changeColour("Location: ", CUITextTools.CYAN) + viewLocation);
      
       System.out.println("\n\n" + headerMain + "\n\n" + breadcrumb + "\n\n" + cmdSubheader + "\n");
       showCommands();
    }
    
    @Override
    public int getAccessLevel()
    {
        return 0;
    }
    
    @Override
    protected abstract String getCommandsFile();
}
