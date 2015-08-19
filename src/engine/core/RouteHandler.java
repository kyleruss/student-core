//####################################
//  KYLE RUSSELL
//  13831056
//  PDC Project
//####################################

package engine.core;

import engine.views.View;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;


//------------------------------------------
//             ROUTE HANDLER
//------------------------------------------
//- Provides the bridge between controllers and routes
//- Redirects valid routes to controllers
//- Supports route names and urls

public class RouteHandler 
{
    //The frameworks controller package
    private static final String CONTROLLER_PACKAGE  =   "engine.controllers";
    
    //Routes that the handler can access
    //Routes need to be initialized otherwise 
    //they will be unrecognized when called
    private static final Routes routes = new Routes();
    
    
    //Pass the routes path or url
    //Path must be valid for redirection
    //Params are stripped if url
    public static View go(String route)
    {
        try
        {
            //Path must be valid and created in Routes.initRoutes()
            Path path           =   routes.getPath(route);
            if(path == null) throw new NoSuchMethodException();
            
            String pathName     =   path.getName();
            String urlPattern   =   path.getLocation();

            //If a route name is passed and has params
            //call go(String, String[]) 
            String[] params     =   (Router.isPathName(route))? new String[] {} : getParamsFromUrl(route, urlPattern);
            return go(pathName, params);
        }
        
        catch(NoSuchMethodException e)
        {
            System.out.println("Path was not found");
            return null;
        }
    }
    
    //Calls a controllers method with params
    //The number of params must match the routes 
    //controller method number of params
    public static View go(String routeName, String[] params)
    {
        Path foundPath  =   routes.getPath(routeName);   
        try
        {
            if(foundPath == null) throw new NoSuchMethodException();
            else           
                return call(foundPath, params);
        }
        
        catch(ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e)
        {
            e.printStackTrace();
            System.out.println("Path not found: " + e.getMessage());
            System.out.println(foundPath);
            return null;
        }
    }
    
    //Creates and calls the routes controller method
    //Controller and method of querying route must exist
    //Further redirection must be handled by the called controller
    public static View call(Path path, String[] params) 
    throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException
    {
        String controllerName    =   MessageFormat.format("{0}.{1}", CONTROLLER_PACKAGE, path.getController());
        String controllerMethod  =   path.getControllerMethod();
        
        Class<?> controller      =   Class.forName(controllerName);
        
        Method method            =   controller.getMethod(controllerMethod);
        View calledView          =   (View) method.invoke(controller.newInstance(), (Object[]) params);
        return calledView;
    }
    
    //Fetches params from a routes url
    //Runs comparisons on the routes url pattern against the url to 
    //extract params and returns them
    //- url:  the url without placeholders
    //- urlPattern: the Routes url with placeholders
    public static String[] getParamsFromUrl(String url, String urlPattern)
    {
        String[] patternMatches =   urlPattern.split("/");
        String[] urlMatches     =   url.split("/");
        
        int length              =   Math.min(patternMatches.length, patternMatches.length);
        String[] params         =   new String[length];
        
        for (int matchIndex = 0; matchIndex < length; matchIndex++) 
        {
            String patternParam =   patternMatches[matchIndex];
            String urlParam     =   urlMatches[matchIndex];
            
            //Check for and eliminate param declorations in url
            if(!patternParam.equalsIgnoreCase(urlParam))
            {
                String[] assigns    =   urlParam.split("=");
                
                if(assigns.length > 1) params[matchIndex] = assigns[1];
                else params[matchIndex] = urlParam;
            }
        }
            
        return params;
    }
}
