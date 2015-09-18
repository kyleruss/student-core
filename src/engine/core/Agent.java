//####################################
//  KYLE RUSSELL
//  13831056
//  PDC Project
//####################################

package engine.core;

import engine.controllers.ControllerMessage;
import engine.core.authentication.Session;
import engine.views.View;
import engine.views.cui.Utilities.CUITextTools;
import java.util.Scanner;


//------------------------------------------
//              AGENT
//------------------------------------------
//- Agent is the main class and controls the running of the app
//- Is the bridge of communication between sessions, views and data

public final class Agent extends CommandInterpreter
{
    
    //A conext is the flag for who handlesz communication
    //Contexts include agent and view (active)
    public enum Context
    {
        AGENT("agent"),
        VIEW("current");
        
        //The command trigger/prefix
        private final String trigger;
        
        Context(String trigger)
        {
            this.trigger    =   trigger;
        }
        
        //Returns the context trigger/prefix
        public String getTrigger()
        {
            return trigger;
        }
        
        //Returns the context based on trigger 
        public static Context getContext(String trigger)
        {
            return Context.valueOf(trigger);
        }
    }
    
    private static View activeView; //The currenbt user view
    private static Context activeContext; //The current context 
    private static Session activeSession; //The session, includes auth and logged in users
    private static Thread agentThread; 
    private static volatile boolean waitingOnCommand = false;
    private static volatile boolean serving = true;
    
    public Agent()
    {
        agentThread     =   new Thread(listen);
        begin();
    }
    
    private void begin()
    {
        final String startRoute   =   "/";
        setView(startRoute);
        viewContext();
        agentThread.start();
    }
    
    public static void commandFinished()
    {
        synchronized(agentThread)
        {
            waitingOnCommand = false;
            agentThread.notify();
        }
    }
    
    public static void commandInProgress()
    {
        synchronized(agentThread)
        {
            waitingOnCommand = true;
            agentThread.notify();
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
    
    public static void setActiveSession(Session activeSession)
    {
        Agent.activeSession   =   activeSession;
    }
    
    public static Session getActiveSession()
    {
        return activeSession;
    }
    
    public static void setView(View view)
    {
        synchronized(agentThread)
        {
            if(view == null) return;
            else
            {
                if(activeView != null)
                {
                    view.setPrevView(activeView);
                    activeView.setNextView(view);
                }
                
                activeView =   view;
                view.display(); 
                agentThread.notify();
            }
        }
    }
    
    public static void setView(String route)
    {
        setView(route, null);
    }
    
    public static void setView(String route, ControllerMessage data)
    {
        View controllerView =   RouteHandler.go(route, data);
        setView(controllerView);
    }
    
    public void setPrevView()
    {
        if(activeView == null || activeView.getPrevView() == null)
            System.out.println(CUITextTools.changeColour("Can't go to that view", CUITextTools.RED));
        else
            setView(activeView.getPrevView());
        
    }
    
    public void setNextView()
    {
        if(activeView == null || activeView.getPrevView() == null)
            System.out.println(CUITextTools.changeColour("Can't go to that view", CUITextTools.RED));
        else
            setView(activeView.getNextView());
    }
    
    public void refreshView()
    {
         if(activeView == null)
             System.out.println(CUITextTools.changeColour("Can't refresh this view", CUITextTools.RED));
         else
             activeView.display();
    }

    public void showHelp()
    {
        System.out.println("\n" + CUITextTools.underline(CUITextTools.changeColour("Agent commands", CUITextTools.CYAN)) + "\n");
        showCommands();
        
        if(activeView != null)
        {
            System.out.println("\n" + CUITextTools.underline(CUITextTools.changeColour("View commands", CUITextTools.MAGENTA)) + "\n");
            ((CommandInterpreter) activeView).showCommands();
        }
    }
    
    public void exitApp()
    {
        String exitText =   CUITextTools.changeColour("Thanks for trying out Student core by Kyle Russell!", CUITextTools.GREEN);
        System.out.println(exitText);
        stopServing();
    }
    
    public void sessionLogout()
    {
        if(activeSession == null)
        {
            System.out.println(CUITextTools.changeColour("You aren't logged in!", CUITextTools.RED));
            return;
        }
        
        System.out.println("\n" + CUITextTools.changeColour("You have been logged out", CUITextTools.RED) + "\n");
        System.out.println("Redirecting in 5 seconds..");
        
        try { Thread.sleep(5000); } 
        catch (InterruptedException ex) 
        {
            //System.out.println(ex.getMessage());
        }
        
        setView("logout");
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
        return (params[0].equalsIgnoreCase(trigger + ":")) || commands.containsKey(params[0]);
    } 
    
    @Override
    public String getCommandsFile()
    {
        return "/engine/config/listeners/AgentListener.json";
    }
    
    Runnable listen =   () -> 
    {
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
                        fire(commandStr, Agent.this);
                    }

                    else if(activeView != null)
                        activeView.fire(command, activeView);
                    
                    
                }
            }
            
            catch(InterruptedException e)
            {
                //e.printStackTrace();
            }     
        }
    };
   
    
    public static void main(String[] args)
    {
        Agent agent =   new Agent();
    }
}