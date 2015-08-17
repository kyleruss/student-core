
package engine.core;

import engine.Views.cui.Utilities.CUITextTools;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public abstract class CommandInterpreter implements CommandExecute
{
    protected Map<String, Command>commands;
    
    @Override
    public void fire(String commandRaw)
    {
        List<String> paramList  =   new ArrayList<>(Arrays.asList(commandRaw.split("\\s")));
        String userCommand      =   paramList.get(0);
        Command command         =   commands.get(userCommand);
        
        if(command == null)
            unrecognizedCommand();
        else
        {
            paramList.remove(0);
            String[] params =   paramList.toArray(new String[paramList.size()]);
            command.call(params);
        }
    }
    
    public void unrecognizedCommand()
    {
        int errorColour =   CUITextTools.RED;
        String message  =   "Command was not recognized!";
        message         =   CUITextTools.changeColour(message, errorColour);
        
        System.out.println(message);
    }
    
    public void showCommands()
    {
        
    }
    
    protected abstract String getCommandsFile();
    
    
    public void initCommands()
    {
        String listenerFile     =   getCommandsFile();
        commands                =   CommandListener.loadFactory(listenerFile).getCommands();
    }
}
