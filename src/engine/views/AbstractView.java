
package engine.views;

import engine.controllers.ControllerMessage;
import engine.core.CommandInterpreter;


public abstract class AbstractView extends CommandInterpreter implements View
{
    protected ControllerMessage messages;
    
    public AbstractView()
    {
        this(new ControllerMessage());
    }
    
    public AbstractView(ControllerMessage messages)
    {
        this.messages   =   messages;
    }
    
    public void pass(String messageName, String message)
    {
        messages.add(messageName, message);
    }
    
    @Override
    public ControllerMessage getMessages()
    {
        return messages;
    }
    
    @Override
    public abstract void display();
    
    @Override
    public int getAccessLevel()
    {
        return 0;
    }
    
    @Override
    protected abstract String getCommandsFile();
}
