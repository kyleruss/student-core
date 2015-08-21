package engine.views.cui;

import engine.views.cui.Utilities.CUITextTools;
import engine.views.AbstractView;

public class HomeView extends AbstractView
{

    @Override
    public void display() 
    {
       String header        =   "Home";
       String desc          =   "Manage personal profile, administration and more";
       String headerMain    =   CUITextTools.drawLargeHeader(header, desc, CUITextTools.GREEN, CUITextTools.CYAN);
       String cmdSubheader  =   CUITextTools.drawSubHeader("Commands", CUITextTools.PLAIN, CUITextTools.GREEN, "=");
      System.out.println("\n\n" + headerMain + "\n\n" + cmdSubheader + "\n");
      showCommands();
       
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