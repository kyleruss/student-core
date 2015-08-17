package engine.core;

import engine.Views.View;
import engine.Views.cui.Utilities.CUITextTools;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Agent implements CommandInterpreter
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
    private List<Command> commands;
    
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
        processCommand();
    }
    
    @Override
    public void initCommands()
    {
        String listenerFile     =   getCommandsFile();
        commands                =   (List<Command>) CommandListener.loadFactory(listenerFile).getCommands();
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
    
    public void testAgentContext(String message)
    {
        System.out.println("AGENT: Message " + message);
    }
    
    @Override
    public void unrecognizedCommand(String command)
    {
        int errorColour =   CUITextTools.RED;
        String message  =   "Command: " + command  + " is not recognized!";
        message         =   CUITextTools.changeColour(message, errorColour);
        
        System.out.println(message);
    }
    
    @Override
    public String getCommandsFile()
    {
        return "/engine/config/listeners/AgentListener.json";
    }
    
    @Override
    public void showCommands()
    {
        
    }
    
    @Override
    public void fire(String command)
    {
        String[] params     =  command.split(" ");
        String commandCall  =   params[1];   
        switch(commandCall)
        {
            case "test": testAgentContext("D");
                break;
            default: unrecognizedCommand(commandCall);
        }
    }
    
    public void processCommand()
    {
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
                    fire(command);
                }
                
                else if(activeView != null)
                    activeView.fire(command);     
            }
        });
        
        agentThread.start();
    }
   
    
    public static void main(String[] args)
    {
        Agent agent =   new Agent();
    }
}