
package engine.views.cui;

import engine.core.CommandInterpreter;
import engine.views.View;
import engine.views.cui.Utilities.CUITextTools;

public class Login extends CommandInterpreter implements View
{

    @Override
    public void display()
    {
       String header        =   "Welcome to Student core!";
       String desc          =   "Explore a great DMS";
       String headerMain    =   CUITextTools.drawLargeHeader(header, desc, CUITextTools.GREEN, CUITextTools.CYAN);
       String breadcrumb    =   CUITextTools.underline(CUITextTools.changeColour("Location: ", CUITextTools.CYAN) + "/login/");
       String cmdSubheader  =   CUITextTools.drawSubHeader("Commands", CUITextTools.PLAIN, CUITextTools.GREEN, ".");
       System.out.println(headerMain + "\n\n" + breadcrumb + "\n\n" + cmdSubheader + "\n" + showCommands());
       
       System.out.println("\n\n" + CUITextTools.changeColour("Please login or register an account", CUITextTools.YELLOW));
    }

    @Override
    public int getAccessLevel()
    {
        return 0;
    }

    @Override
    protected String getCommandsFile() 
    {
        return "/engine/config/listeners/AgentListener.json";
    }
    
}
