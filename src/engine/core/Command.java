//====================================
//  KYLE RUSSELL
//  13831056
//  PDC Project
//====================================

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
    private String commandDescription;
    
    public Command(String commandName, String[] paramTypes, String className, String methodName, String commandDescription)
    {
        this.commandName        =   commandName;
        this.paramTypes         =   paramTypes;
        this.className          =   className;
        this.methodName         =   methodName;
        this.commandDescription =    commandDescription;
    }
    
    public String getCommandName()
    {
        return commandName;
    }
    
    public String getCommandDescription()
    {
        return commandDescription;
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
    
    public void setCommandDescription(String commandDescription)
    {
        this.commandDescription =   commandDescription;
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
    
    public Object call(String[] params, Object instance)
    {
        try
        {
            if(params.length > paramTypes.length || params.length < paramTypes.length) throw new NoSuchMethodException();
            
            Class<?> viewClass  =   Class.forName(className);
            Method listenMethod;
            
            if(params.length > 0 && paramTypes.length > 0)
            {
                listenMethod =   viewClass.getMethod(methodName, Class.forName(paramTypes[0]));
                return listenMethod.invoke(instance, (Object[]) params);
            }
            
            else
            {
                listenMethod    =   viewClass.getDeclaredMethod(methodName, new Class[]{});
               // listenMethod.getReturnType()
                return listenMethod.invoke(instance);
            }
        }
        
        catch(ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e)
        {
           // e.printStackTrace();
        //    System.out.println("Listener method was not found, or the paramaters do not match: " + e.getMessage());
        //    System.out.println("method: " + methodName);
        //    System.out.println("class: " + className);
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
