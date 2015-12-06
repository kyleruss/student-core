//====================================
//	Kyle Russell
//	jdamvc
//	CommandInterpreter
//====================================

package engine.core;

import com.bethecoder.ascii_table.ASCIITable;
import engine.views.cui.Utilities.CUITextTools;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

//------------------------------------
//          COMMANDINTERPRETER
//------------------------------------
//- Handles execution and interpretation of commands
//- Loads commands from CommandListener and checks unrecognized commands
//- Has some utilities for showingCommands used by help


public abstract class CommandInterpreter implements CommandExecute
{
    protected Map<String, Command> commands; //Available commands fetched from listener
    
    public CommandInterpreter()
    {
        initCommands();
    }
    
    //Executes a command
    //- commandRaw: full command string passed
    //- instance: class instance to call the method on
    @Override
    public void fire(String commandRaw, Object instance)
    {
        if(commands == null) return;
        
        //Command structure: commandName param1 param2 .. paramN
        //Context triggers are resolved before being passed to interpreter
        List<String> paramList  =   new ArrayList<>(Arrays.asList(commandRaw.split("\\s")));
        String userCommand      =   paramList.get(0); //command name
        Command command         =   commands.get(userCommand); 
        
        //Check if command exists
        //Commands are listed in the classes respective listener file
        if(command == null)
            unrecognizedCommand();
        else
        {
            paramList.remove(0); //seperate command name from params
            String[] params =   paramList.toArray(new String[paramList.size()]);
            command.call(params, instance);
        }
    }
    
    //Prompts a message when the command is not found
    public void unrecognizedCommand()
    {
        int errorColour =   CUITextTools.RED;
        String message  =   "Command was not recognized!";
        message         =   CUITextTools.changeColour(message, errorColour);
        
        ExceptionOutput.output(message, ExceptionOutput.OutputType.MESSAGE);
    }
    
    //Displays a table of available commands from the listener
    //Each cell includes the command name and description
    //Used by help command for each view to show commands of the view
    public void showCommands()
    {
        if(commands  == null) return;
        
        List<Command> commandCol            =   new ArrayList<>(commands.values());
        final int maxCols                   =   3;
        final int maxRows                   =   (int) Math.ceil((commandCol.size() * 1.0) / maxCols);
        int numRows                         =   Math.max(maxRows, 1);
        int numCols                         =   Math.min(maxCols, commandCol.size());
        
        String[] headers                    =   {};
        String[][] data                     =   new String[numRows][numCols];
        int commandIndex                    =   0;
        
        for(int row = 0; row < numRows; row++)
        {
            for(int col = 0; col < numCols; col++)
            {
                if((col * (row + 1)) >= numCols || commandIndex >= commandCol.size())
                {
                    data[row][col] = "";
                    continue;
                }
               
                Command current         =   commandCol.get(commandIndex);
                String commandName      =   current.getCommandName();
                String commandDesc      =   current.getCommandDescription();
                String commandDisp      =   CUITextTools.keyTextBrackets(commandDesc, commandName);
                
                data[row][col] = commandDisp;
                commandIndex++;
            }
        }
       
        ASCIITable.getInstance().printTable(headers, data);
    }
    
    //Implementors should return the path to the listeners json file
    //Listeners can be found in engine\config\listeners
    protected abstract String getCommandsFile();
    
    
    //Gets the commands listener path
    //Fetches all commands from the listener
    protected void initCommands()
    {
        String listenerFile     =   getCommandsFile();
        
        if(listenerFile != null)
            commands                =   CommandListener.loadFactory(listenerFile).getCommands();
    }
}
