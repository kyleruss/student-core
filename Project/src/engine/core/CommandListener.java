//====================================
//	Kyle Russell
//	jdamvc
//	CommandListener
//====================================

package engine.core;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandListener
{
    //Commands taken from the listener file
    //key: command name
    //value: command (includes class, method call etc)
    public Map<String, Command> commands;
    
    //Create default listener with empty commands
    //Commands should be added alter
    //Typical to use the factory method
    public CommandListener()
    {
        commands    =   new HashMap<>();
    }
    
    //Returns the listeners commands
    //Will be empty if not yet loaded
    public Map<String, Command> getCommands()
    {
        return commands;
    }
    
    //Returns a list of the commands
    //Empty if not not loaded
    public List<Command> getCommandList()
    {
        return new ArrayList<>(commands.values());
    }
    
    //Loads and returns a listener with commands from the listener file
    //Listener files are stored in engine/config/listeners
    public CommandListener load(String listenerFile)
    {
        Gson gson                   =   new Gson();
        CommandListener listener    =   gson.fromJson(new JsonReader(new InputStreamReader
                                        (getClass().getResourceAsStream(listenerFile))), CommandListener.class);
        return listener;
    }
    
    //Returns a command with the command name
    public Command get(String commandName)
    {
        return commands.get(commandName);
    }
    
    //Creates a listener and loads its commands
    //Returns the listener with loaded commands
    public static CommandListener loadFactory(String listenerFile)
    {
        return new CommandListener().load(listenerFile);
    }
}
