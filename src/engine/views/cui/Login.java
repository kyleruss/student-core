
package engine.views.cui;

import engine.controllers.ControllerMessage;
import engine.core.Agent;
import static engine.core.Agent.commandFinished;
import engine.core.authentication.Auth;
import engine.views.AbstractView;
import engine.views.View;
import engine.views.cui.Utilities.CUITextTools;
import java.util.Scanner;

public class Login extends AbstractView implements View
{
    
    public Login()
    {
        super();
    }
    
    public Login(ControllerMessage messages)
    {
        super(messages);
    }

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
    
    
    public void loginAttempt()
    {
        Scanner inputScan   =   new Scanner(System.in);
        
            String usernameText =   CUITextTools.changeColour("Please enter your username", CUITextTools.GREEN);
            String passwordText =   CUITextTools.changeColour("Please enter your password", CUITextTools.GREEN);
            String enteredUsername;
            String enteredPassword;

            System.out.println(usernameText);
            enteredUsername =   inputScan.nextLine();

            System.out.println(passwordText);
            enteredPassword =   inputScan.nextLine();

            Auth.login(enteredUsername, enteredPassword);
            System.out.println("ACTIVE USER: " + Agent.getActiveSession().getUser().get(("username").toUpperCase()).getColumnValue());
            
            commandFinished();
         
    }
        
        
    public void registerAttempt()
    {
        System.out.println("Register attempt");
    }

    @Override
    public int getAccessLevel()
    {
        return 0;
    }

    @Override
    protected String getCommandsFile() 
    {
        return "/engine/config/listeners/LoginListener.json";
    }
    
}
