
package engine.core;


public class Path 
{
    private final String name;
    
    private final String controller;
    
    private final String controllerMethod;
    
    private final String location;
    
    public Path(String name, String controller, String controllerMethod, String location)
    {
        this.name               =   name;
        this.controller         =   controller;
        this.controllerMethod   =   controllerMethod;
        this.location           =   location;
    }
    
    public String getName()
    {
        return name;
    }
    
    public String getController()
    {
        return controller;
    }
    
    public String getControllerMethod()
    {
        return controllerMethod;
    }
    
    public String getLocation()
    {
        return location;
    }
}
