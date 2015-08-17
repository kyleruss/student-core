
package engine.core;

public interface CommandInterpreter 
{
    public void fire(String commannd);
    
    public void unrecognizedCommand(String command);
    
    public String getCommandsFile();
    
    public void showCommands();
    
    public void initCommands();
}
