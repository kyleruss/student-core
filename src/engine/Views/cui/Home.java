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
       System.out.println(CUITextTools.drawLargeHeader(header, desc, CUITextTools.GREEN));
    }
    
    @Override
    public void fire(String command)
    {
        
    }
    
}