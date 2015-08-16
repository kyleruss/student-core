package engine.core;

import engine.Views.View;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Agent
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
    
    public Agent()
    {
        viewTree        =   new LinkedList<>();
        begin();
    }
    
    private void begin()
    {
        final String startRoute   =   "home";
        setView(RouteHandler.go(startRoute));
        viewContext();
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
    
    public void fire(String command)
    {
        
    }
    
    public void processCommand()
    {
        final String FINISHED   =   "exit";
        Scanner input           =   new Scanner(System.in);
        String command;
        
        while(!(command = input.nextLine()).equals(FINISHED))
        {
            if(isAgentContext(command))
                fire(command);
            
            else if(activeView != null)
                activeView.fire(command);
        }
    }
   
    
    public static void main(String[] args)
    {
        Agent agent =   new Agent();
    }
}