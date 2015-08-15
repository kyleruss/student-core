package engine.core;

import engine.Views.View;
import java.util.LinkedList;
import java.util.List;

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
    
    public void setContext(Context context)
    {
        this.activeContext  =   context;
    }
    
    public void fire(String command)
    {
        
    }
    
    public static void main(String[] args)
    {
    }
}