

package engine.core;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.MessageFormat;

public class Command 
{
    private String commandName;
    private String[] paramTypes;
    private String className;
    private String methodName;
    
    public Command(String commandName, String[] paramTypes, String className, String methodName)
    {
        this.commandName    =   commandName;
        this.paramTypes         =   paramTypes;
        this.className      =   className;
        this.methodName     =   methodName;
    }
    
    public String getCommandName()
    {
        return commandName;
    }
    
    public String[] getParamTypes()
    {
        return paramTypes;
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
    
    public void setParamTypes(String[] paramTypes)
    {
        this.paramTypes =   paramTypes;
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
            if(params.length > paramTypes.length || params.length < paramTypes.length) throw new NoSuchMethodException();
            
            Class<?> viewClass  =   Class.forName(className);
            Method listenMethod =   viewClass.getMethod(methodName, Class.forName(paramTypes[0]));
            return listenMethod.invoke(viewClass.newInstance(), (Object[]) params);
        }
        
        catch(ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e)
        {
            System.out.println("Listener method was not found, or the paramaters do not match: " + e.getMessage());
            return null;
        }
    }
    
    
    @Override
    public String toString()
    {
        String output   =   MessageFormat.format("Command: {0}\nCommand class: {1}\nCommand method: {2}\nMethod param count: {3}\n", commandName, className, methodName, paramTypes[0]);
        return output;
    }
}