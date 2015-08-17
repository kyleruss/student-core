package engine.Views.cui;

import engine.Views.View;
import engine.Views.cui.Utilities.CUITextTools;
import engine.core.CommandInterpreter;

public class Home extends CommandInterpreter implements View
{

    @Override
    public void display() 
    {
       String header    =   "Welcome to Student core!";
       String desc      =   "Explore a great DMS";
       System.out.println(CUITextTools.drawLargeHeader(header, desc, CUITextTools.GREEN, CUITextTools.CYAN));
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