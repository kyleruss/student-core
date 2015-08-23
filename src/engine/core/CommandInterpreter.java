
package engine.core;

import com.bethecoder.ascii_table.ASCIITable;
import engine.views.cui.Utilities.CUITextTools;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
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
    
   /* public String showCommands1()
    {
        Iterator<Command> commIter  =   commands.values().iterator();
        int colCount                =   0;
        final int MAX_COLS          =   3;
        String commandText          =   "\n";
        while(commIter.hasNext())
        {
            Command command         =   commIter.next();
            String commandName      =   CUITextTools.changeColour(command.getCommandName(), CUITextTools.MAGENTA);
            String commandDesc      =   CUITextTools.changeColour(command.getCommandDescription(), CUITextTools.PLAIN);
            String commandDisp      =   CUITextTools.keyText(commandName, commandDesc);
            
            commandText += commandDisp + "\t";
            colCount++;
            
            if(colCount == MAX_COLS)
            {
                commandText += "\n";
                colCount = 0;
            }
        }
        return commandText;
    } */
    
    public void showCommands()
    {
        List<Command> commandCol            =   new ArrayList<>(commands.values());
        final int maxCols                   =   3;
        final int maxRows                   =   (int) Math.ceil((commandCol.size() * 1.0) / maxCols);
        int numRows                         =   Math.max(maxRows, 1);
        int numCols                         =   Math.min(maxCols, commandCol.size());
        
        System.out.println("size: " + maxRows);
        String[] headers                    =   {};
        String[][] data                     =   new String[numRows][numCols];
        int commandIndex                    =   0;
        
        for(int row = 0; row < numRows; row++)
        {
            for(int col = 0; col < numCols; col++)
            {
                
               // if((col * (row + 1)) >= numCols || commandIndex >= commandCol.size())
                if((col * (row + 1)) >= numCols || commandIndex >= commandCol.size())
                {
                    System.out.println(commandIndex);
                    data[row][col] = "";
                    continue;
                }
                
               // else if(commandIndex >= 2) break rowLoop;
               
                System.out.println("ci: " + commandIndex);
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
