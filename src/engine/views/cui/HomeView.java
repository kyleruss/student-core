package engine.views.cui;

import engine.views.cui.Utilities.CUITextTools;
import engine.views.AbstractView;

public class HomeView extends AbstractView
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
    
    @Override
    public int getAccessLevel()
    {
        return 0;
    }
}