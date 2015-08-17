

package engine.core;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class CommandListener 
{
    public Map<String, Command> commands;
    
    public CommandListener()
    {
        commands    =   new HashMap<>();
    }
    
    public Collection<Command> getCommands()
    {
        return commands.values();
    }
    
    public CommandListener load(String listenerFile)
    {
        try
        {
            Gson gson                   =   new Gson();
            String listenerPath           =   getClass().getResource(listenerFile).getFile();
            CommandListener listener    =   gson.fromJson(new JsonReader(new FileReader(listenerPath)), CommandListener.class);
            return listener;
        }
        
        catch(FileNotFoundException e)
        {
            System.out.println(e.getMessage());
            return null;
        }
    }
    
    public Command get(String commandName)
    {
        return commands.get(commandName);
    }
    
    public static CommandListener loadFactory(String listenerFile)
    {
        return new CommandListener().load(listenerFile);
    }
    
    
    
    public static void main(String[] args)
    {
        CommandListener listener    =   CommandListener.loadFactory("/engine/config/listeners/AgentListener.json");
        Iterator it =   listener.getCommands().iterator();
        Command c = listener.get("test");
        System.out.println(c);
        c.call(new String[] {"HELLO!"});
    }
}
