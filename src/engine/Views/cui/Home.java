package engine.Views.cui;

import engine.Views.View;
import engine.Views.cui.Utilities.CUITextTools;

public class Home implements View
{

    @Override
    public void display() 
    {
       String header    =   "Welcome to Student core!";
       String desc      =   "Explore a great DMS";
       System.out.println(CUITextTools.drawLargeHeader(header, desc, CUITextTools.GREEN, CUITextTools.PLAIN));
    }
    
    public void viewTest(String message)
    {
        System.out.println("View message " + message);
    }
    
    @Override
    public void unrecognizedCommand(String command)
    {
        System.out.println("VIEW: unrecognized command");
    }
    
    @Override
    public String getCommandsFile()
    {
        return "";
    }
    
    @Override
    public void fire(String command)
    {
        String[] params     =   command.split(" ");
        String commandCall  =   params[0];
        
        switch(commandCall)
        {
            case "test": viewTest(params[1]);
                break;
            default: unrecognizedCommand(commandCall);
        }
    }
    
}