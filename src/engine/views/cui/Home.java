package engine.views.cui;

import engine.views.View;
import engine.views.cui.Utilities.CUITextTools;
import engine.core.CommandInterpreter;

public class Home extends CommandInterpreter implements View
{

    @Override
    public void display() 
    {
       String header        =   "Welcome to Student core!";
       String desc          =   "Explore a great DMS";
       String headerMain    =   CUITextTools.drawLargeHeader(header, desc, CUITextTools.GREEN, CUITextTools.CYAN);
       String cmdSubheader  =   CUITextTools.drawSubHeader("Commands", CUITextTools.PLAIN, CUITextTools.GREEN, "=");
       System.out.println(headerMain + "\n\n" + cmdSubheader + "\n" + showCommands());
    }
    
    public void viewTest(String message)
    {
        System.out.println("View message " + message);
    }
    
    
    @Override
    public String getCommandsFile()
    {
        return "/engine/config/listeners/HomeListener.json";
    } 
}