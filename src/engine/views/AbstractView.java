//====================================
//  KYLE RUSSELL
//  13831056
//  PDC Project
//====================================

package engine.views;

import engine.controllers.ControllerMessage;
import engine.core.CommandInterpreter;
import engine.core.Path;
import engine.views.cui.Utilities.CUITextTools;

//------------------------------------
//            ABSTRACTVIEW
//------------------------------------
//- Provides a general View for sub-classes to use
//- Has CommandInterpreter and View functions
//- Has default display() which shows basic view info


public abstract class AbstractView extends CommandInterpreter implements View
{
    protected ControllerMessage messages; //The messages passed to the view
    protected String viewName; //The views name (can be general)
    protected String viewDescription; //Brief description of the view
    protected String viewLocation; //The route address of the view
    protected View nextView; //The next view  if set
    protected View prevView; //The previous view if set
    protected Path path; //The path that was used to fetch the view
    
    //Creates a default view
    //view details should be set later
    public AbstractView()
    {
        this(new ControllerMessage());
    }
    
    //Creates a view with name, description and address
    public AbstractView(String viewName, String viewDescription, String viewLocation)
    {
        this(new ControllerMessage(), viewName, viewDescription, viewLocation);
    }
    
    //Create a general view with messages passed
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
    
    //Views still need to output their listener file path
    //This path is necessary to load commands for the view
    @Override
    protected abstract String getCommandsFile();
    
    //Send a message to the view
    public void pass(String messageName, String message)
    {
        messages.add(messageName, message);
    }
    
    //Returns the views name
    public String getViewName()
    {
        return viewName;
    }
    
    //Returns the views description
    public String getViewDescription()
    {
        return viewDescription;
    }
    
    //Set the name of the view
    public void setViewName(String viewName)
    {
        this.viewName   =   viewName;
    }
    
    //Set the description of the view
    public void setViewDescription(String viewDescription)
    {
        this.viewDescription    =   viewDescription;
    }
    
    //Returns the messages currently in the view
    @Override
    public ControllerMessage getMessages()
    {
        return messages;
    }
    
    //Displays a header with view name and description
    //Shows all available commands in the view
    @Override
    public void display()
    {
       String headerMain    =   CUITextTools.drawLargeHeader(viewName, viewDescription, CUITextTools.GREEN, CUITextTools.CYAN);
       String cmdSubheader  =   CUITextTools.drawSubHeader("Commands", CUITextTools.PLAIN, CUITextTools.GREEN, "=");
       String breadcrumb    =   CUITextTools.underline(CUITextTools.changeColour("Location: ", CUITextTools.CYAN) + viewLocation);
      
       System.out.println("\n\n" + headerMain + "\n\n" + breadcrumb + "\n\n" + cmdSubheader + "\n");
       showCommands();
    }
    
    //Returns the access level of the view
    //Generally all views are accessible
    //If this is not the case override
    @Override
    public int getAccessLevel()
    {
        return 0;
    }
    
    //Returns the previous view
    //May be null if not set
    @Override
    public View getPrevView()
    {
        return prevView;
    }
    
    //Returns the next view
    //May be null if not set
    @Override
    public View getNextView()
    {
        return nextView;
    }
    
    //Set the previous view
    @Override
    public void setPrevView(View prevView)
    {
        this.prevView   =   prevView;
    }
    
    //Set the next view
    @Override
    public void setNextView(View nextView)
    {
        this.nextView   =   nextView;
    }
}
