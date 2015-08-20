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
    private Thread agentThread;
    
    public Agent()
    {
        viewTree        =   new LinkedList<>();
        begin();
    }
    
    private void begin()
    {
        final String startRoute   =   "getLogin";
        setView(RouteHandler.go(startRoute));
        agentContext();
        listen();
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
        try(Scanner inputScan   =   new Scanner(System.in))
        {
        String usernameText =   CUITextTools.changeColour("Please enter your username", CUITextTools.RED);
        String passwordText =   CUITextTools.changeColour("Please enter your password", CUITextTools.RED);
        String enteredUsername;
        String enteredPassword;
        
        System.out.println(usernameText);
        enteredUsername =   inputScan.nextLine();
        System.out.println(enteredUsername);
        System.out.println(passwordText);
        enteredPassword =   inputScan.nextLine();
        System.out.println(enteredPassword);
        
        Auth.login(enteredUsername, enteredPassword);
        
        synchronized(agentThread)
        {
            agentThread.notify();
        }
        } 
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
    
    public void listen()
    {
        Agent agentInstance =   this;
        agentThread  =   new Thread(() ->
        {
            final String FINISHED   =   "exit";
            Scanner input           =   new Scanner(System.in);
            String command;
            
            
            while(!(command = input.nextLine()).equals(FINISHED))
            {
                System.out.println("comamnd: " + command);
                
                synchronized(this)
                {
                    if(isAgentContext(command))
                    {
                        try {
                            String commandStr   =   command.replace("agent: ", "");
                            fire(commandStr, agentInstance);
                            wait();
                        } catch (InterruptedException ex) {
                            Logger.getLogger(Agent.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                
                    else if(activeView != null)
                        activeView.fire(command, activeView);  
                }   
            }
        });
        
        agentThread.start();
    }
   
    
    public static void main(String[] args)
    {
        Agent agent =   new Agent();
    }
}