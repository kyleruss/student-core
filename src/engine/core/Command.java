

package engine.core;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.MessageFormat;

public class Command 
{
    private String commandName;
    private String[] params;
    private String className;
    private String methodName;
    
    public Command(String commandName, String[] params, String className, String methodName)
    {
        this.commandName    =   commandName;
        this.params         =   params;
        this.className      =   className;
        this.methodName     =   methodName;
    }
    
    public String getCommandName()
    {
        return commandName;
    }
    
    public String[] getParams()
    {
        return params;
    }
    
    public String getClassName()
    {
        return className;
    }
    
    public String getMethodName()
    {
        return methodName;
    }
    
    public void setCommandName(String commandName)
    {
        this.commandName    =   commandName;
    }
    
    public void setParams(String[] params)
    {
        this.params =   params;
    }
    
    public void setClassName(String className)
    {
        this.className  =   className;
    }
    
    public void setMethodName(String methodName)
    {
        this.methodName =   methodName;
    }
    
    public Object call(String[] params)
    {
        try
        {
            Class<?> viewClass  =   Class.forName(className);
            Method listenMethod =   viewClass.getMethod(methodName, Class.forName(params[0]));
            return listenMethod.invoke(viewClass.newInstance(), (Object[]) params);
        }
        
        catch(ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e)
        {
            System.out.println("error: " + e.getMessage());
            return null;
        }
    }
    
    
    @Override
    public String toString()
    {
        String output   =   MessageFormat.format("Command: {0}\nCommand class: {1}\nCommand method: {2}\nMethod param count: {3}\n", commandName, className, methodName, params[0]);
        return output;
    }
}
