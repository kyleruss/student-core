//====================================
//  KYLE RUSSELL
//  13831056
//  PDC Project
//====================================

package engine.core;


//------------------------------------------
//                  PATH
//------------------------------------------
//- Paths are route definitions
//- They describe and include details about a route
//- Details include the route name, controller class, 
//the controllers method to call and the route url 


public class Path 
{
    //The routes name
    //Route can be identified just by name
    private final String name;
    
    //The routes controller class
    //Controller must house the controllerMethod
    private final String controller;
    
    //The routes controller method
    //Called on succession that handles route
    private final String controllerMethod;
    
    //The routes URL that can identify route
    //Defines any params enclosed in {}
    private final String location;
    
    public Path(String name, String controller, String controllerMethod, String location)
    {
        this.name               =   name;
        this.controller         =   controller;
        this.controllerMethod   =   controllerMethod;
        this.location           =   location;
    }
    
    //Returns the routes name
    public String getName()
    {
        return name;
    }
    
    //Returns the routes controller name
    public String getController()
    {
        return controller;
    }
    
    //Returns the routes controller method name
    public String getControllerMethod()
    {
        return controllerMethod;
    }
    
    //Returns the routes URL
    public String getLocation()
    {
        return location;
    }
    
    @Override
    public String toString()
    {
        return "Name: " + name + " controller: " + controller + " method: " + controllerMethod + " location: " + location;
    }
}
