package engine.core;

import engine.views.View;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

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
        System.out.println("Login attempt!");
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
        System.out.println("AGENT: Message " + message);
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
                if(isAgentContext(command))
                {
                    String commandStr   =   command.replace("agent: ", "");
                    fire(commandStr, agentInstance);
                }
                
                else if(activeView != null)
                    activeView.fire(command, activeView);     
            }
        });
        
        agentThread.start();
    }
   
    
    public static void main(String[] args)
    {
        Agent agent =   new Agent();
    }
}