//====================================
//  KYLE RUSSELL
//  13831056
//  PDC Project
//====================================

package engine.core;

import com.bethecoder.ascii_table.ASCIITable;
import engine.views.cui.Utilities.CUITextTools;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public abstract class CommandInterpreter implements CommandExecute
{
    protected Map<String, Command>commands;
    
    public CommandInterpreter()
    {
        initCommands();
    }
    
    @Override
    public void fire(String commandRaw, Object instance)
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
            command.call(params, instance);
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
    
    protected abstract String getCommandsFile();
    
    
    protected void initCommands()
    {
        String listenerFile     =   getCommandsFile();
        commands                =   CommandListener.loadFactory(listenerFile).getCommands();
    }
    
    public static void main(String[] args)
    {
        RouteHandler.go("getStudents", null).display();
    }
}
