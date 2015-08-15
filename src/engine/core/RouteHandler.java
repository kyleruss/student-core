
package engine.core;

import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;

public class RouteHandler 
{
    private static final String CONTROLLER_PACKAGE  =   "engine.Controllers";
    private final Routes routes;
    
    public RouteHandler()
    {
        routes  =   new Routes();
    }
    
    public void go(String url)
    {
        Path path           =   routes.getPath(url);
        String pathName     =   path.getName();
        String urlPattern   =   path.getLocation();
        
        String[] params     =   getParamsFromUrl(url, urlPattern);
        go(pathName, params);
    }
    
    public void go(String routeName, String[] params)
    {
        Path foundPath  =   routes.getPath(routeName);   
        try
        {
            if(foundPath == null) throw new NoSuchMethodException();
            else
            {
                buildCall(foundPath, params);
            }
        }
        
        catch(ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e)
        {
            System.out.println("Path not found");
        }
    }
    
    public static void buildCall(Path path, String[] params) 
    throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException
    {
        String controllerName    =   MessageFormat.format("{0}.{1}", CONTROLLER_PACKAGE, path.getController());
        String controllerMethod  =   path.getControllerMethod();
        
        Class<?> controller      =   Class.forName(controllerName);
        Method method            =   controller.getMethod(controllerMethod);
        method.invoke(controller.newInstance(), (Object[]) params);
    }
    
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
            
            
            if(!patternParam.equalsIgnoreCase(urlParam))
            {
                String[] assigns    =   urlParam.split("=");
                
                if(assigns.length > 1) params[matchIndex] = assigns[1];
                else params[matchIndex] = urlParam;
            }
        }
            
        return params;
    }
    
    public static void main(String[] args)
    {
        Path path   =   new Path("home", "BaseController", "getHome", "/");
        RouteHandler handler    =   new RouteHandler();
        handler.go("home");
    }
}
