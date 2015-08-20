package engine.core;

import engine.core.authentication.Auth;
import engine.views.View;
import engine.views.cui.Utilities.CUITextTools;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Agent extends CommandInterpreter
{
    public enum Context
    {
        AGENT("agent"),
        VIEW("current");
        
        private final String trigger;
        
        Context(String trigger)
        {
            this.trigger    =   trigger;
        }
        
        public String getTrigger()
        {
            return trigger;
        }
        
        public static Context getContext(String trigger)
        {
            return Context.valueOf(trigger);
        }
    }
    
    private View activeView;
    private Context activeContext;
    private List<View> viewTree;
    public static Thread agentThread;
    private static volatile boolean waitingOnCommand = false;
    private static volatile boolean serving = true;
    
    public Agent()
    {
        viewTree        =   new LinkedList<>();
        agentThread     =   new Thread(listen);
        begin();
    }
    
    private void begin()
    {
        final String startRoute   =   "/";
        setView(RouteHandler.go(startRoute));
        agentContext();
        agentThread.start();
    }
    
    public static void commandFinished()
    {
        synchronized(agentThread)
        {
            System.out.println("notify");

            waitingOnCommand = false;
            agentThread.notify();
            System.out.println("state: " + agentThread.getState().toString());
        }
    }
    
    public static void stopServing()
    {
        synchronized(agentThread)
        {
            serving =   false;
            agentThread.notify();
        }
    }
    
    public void agentContext()
    {
        activeContext   =   Context.AGENT;
    }
    
    public void viewContext()
    {
        activeContext   =   Context.VIEW;
    }
    
    public void setView(View view)
    {
        if(view == null) return;
        else
        {
            this.activeView =   view;
            view.display(); 
        }
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
    
    public void exitApp()
    {
        String exitText =   CUITextTools.changeColour("Thanks for trying out Student core by Kyle Russell!", CUITextTools.GREEN);
        System.out.println(exitText);
        stopServing();
    }
    
    public void registerAttempt()
    {
        System.out.println("Register attempt");
    }
    
    public void switchContext(Context context)
    {
        this.activeContext  =   context;
    }
    
    public boolean isAgentContext(String command)
    {
        if(command == null || command.length() == 0 || activeContext == Context.AGENT) return true;
        
        String[] params =   command.split(" ");
        String trigger  =   Context.AGENT.getTrigger();
        return (params[0].equalsIgnoreCase(trigger + ":"));
    }
    
    public void testAgentContext(String message)
    {
        System.out.println("message: " + message);
    }
    
    
    @Override
    public String getCommandsFile()
    {
        return "/engine/config/listeners/AgentListener.json";
    }
    
    Runnable listen =   () -> 
    {
        Agent agentInstance =   Agent.this;
        final String FINISHED   =   "exit";
        Scanner input           =   new Scanner(System.in);
        String command;
        
        synchronized(agentThread)
        {     
            try
            {
                while(serving)
                {
                    while(waitingOnCommand)
                        agentThread.wait();
                
                    command = input.nextLine();
                    if(isAgentContext(command))
                    {
                        String commandStr   =   command.replace("agent: ", "");
                        fire(commandStr, agentInstance);
                    }

                    else if(activeView != null)
                        activeView.fire(command, activeView); 
                }
            }
            
            catch(InterruptedException e)
            {
                e.printStackTrace();
            }     
                
                
                
           /* while(!(command = input.nextLine()).equals(FINISHED))
            {

                if(isAgentContext(command))
                {
                    String commandStr   =   command.replace("agent: ", "");
                    fire(commandStr, agentInstance);
                }

                else if(activeView != null)
                    activeView.fire(command, activeView); 
            } */
        }
    };
   
    
    public static void main(String[] args)
    {
        Agent agent =   new Agent();
    }
}