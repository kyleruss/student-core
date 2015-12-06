//====================================
//	Kyle Russell
//	jdamvc
//	AbstractView
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
    protected String viewTitle; //The views name (can be general)
    protected String viewDescription; //Brief description of the view
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
    public AbstractView(String viewTitle, String viewDescription)
    {
        this(new ControllerMessage(), viewTitle, viewDescription);
    }
    
    //Create a general view with messages passed
    public AbstractView(ControllerMessage messages)
    {
        this(messages, "Layout", "Layout view");
    } 
    
    public AbstractView(ControllerMessage messages, String viewTitle, String viewDescription)
    {
        this.messages           =   messages;
        this.viewTitle           =   viewTitle;
        this.viewDescription    =   viewDescription;
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
    public String getViewTitle()
    {
        return viewTitle;
    }
    
    //Returns the views description
    public String getViewDescription()
    {
        return viewDescription;
    }
    
    //Set the name of the view
    public void setViewTitle(String viewTitle)
    {
        this.viewTitle   =   viewTitle;
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
       String headerMain    =   CUITextTools.drawLargeHeader(viewTitle, viewDescription, CUITextTools.GREEN, CUITextTools.CYAN);
       String cmdSubheader  =   CUITextTools.drawSubHeader("Commands", CUITextTools.PLAIN, CUITextTools.GREEN, "=");
       String breadcrumb    =   CUITextTools.underline(CUITextTools.changeColour("Location: ", CUITextTools.CYAN) + path.getFullURL());
      
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
    
    @Override
    public Path getPath()
    {
        return path;
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
    
    @Override
    public void setPath(Path path)
    {
        this.path   =   path;
    }
}
