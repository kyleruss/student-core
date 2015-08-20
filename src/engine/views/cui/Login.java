
package engine.views.cui;

import static engine.core.Agent.commandFinished;
import engine.core.CommandInterpreter;
import engine.core.authentication.Auth;
import engine.views.View;
import engine.views.cui.Utilities.CUITextTools;
import java.util.Scanner;

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
    
    
    public void loginAttempt()
    {
        Scanner inputScan   =   new Scanner(System.in);
        
            String usernameText =   CUITextTools.changeColour("Please enter your username", CUITextTools.RED);
            String passwordText =   CUITextTools.changeColour("Please enter your password", CUITextTools.RED);
            String enteredUsername;
            String enteredPassword;

            System.out.println(usernameText);
            enteredUsername =   inputScan.nextLine();

            System.out.println(passwordText);
            enteredPassword =   inputScan.nextLine();

            Auth.login(enteredUsername, enteredPassword);

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
