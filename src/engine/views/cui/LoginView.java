
package engine.views.cui;

import engine.controllers.ControllerMessage;
import engine.core.Agent;
import static engine.core.Agent.commandFinished;
import engine.core.RouteHandler;
import engine.views.AbstractView;
import engine.views.View;
import engine.views.cui.Utilities.CUITextTools;
import java.util.Scanner;

public class LoginView extends AbstractView implements View
{
    
    public LoginView()
    {
        this(new ControllerMessage());
    }
    
    public LoginView(ControllerMessage messages)
    {
        super(messages, "Welcome to Student core by Kyle Russell", "Explore a great DMS", "/login/");
    }

    @Override
    public void display()
    {
        super.display();
        System.out.println("\n\n" + CUITextTools.changeColour("Please login or create an account", CUITextTools.YELLOW));
    }
    
    
    public void loginAttempt()
    {       
        try
        {
              Scanner inputScan   =   new Scanner(System.in);
            
            String usernameText =   CUITextTools.changeColour("Please enter your username", CUITextTools.GREEN);
            String passwordText =   CUITextTools.changeColour("Please enter your password", CUITextTools.GREEN);
            String enteredUsername;
            String enteredPassword;

            Thread.sleep(200);
            System.out.println(usernameText);
            enteredUsername =   inputScan.nextLine();

            Thread.sleep(200);
            System.out.println(passwordText);
            enteredPassword =   inputScan.nextLine();
            
            
            ControllerMessage postData   =   new ControllerMessage();
            postData.add("loginUsername", enteredUsername);
            postData.add("loginPassword", enteredPassword);
            
            ResponseDataView response   =   (ResponseDataView) RouteHandler.go("postLogin", postData);
            
            System.out.println("\n" + response.getResponseMessage());
            
            if(response.getResponseStatus())
            {
                System.out.println("Redirecting in 5 seconds..");
                Thread.sleep(5000); 
                Agent.setView("getHome");
            }
        }
            
        catch (InterruptedException ex)
        {
            System.out.println(ex.getMessage());
        }
            
        commandFinished();
    }
        
        
    public void registerAttempt()
    {
        Agent.setView("getRegister");
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
