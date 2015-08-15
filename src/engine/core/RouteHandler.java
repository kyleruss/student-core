
package engine.core;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RouteHandler 
{
    private Routes routes;
    
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
        
    }
    
    private Method buildCall(Path path) throws ClassNotFoundException, NoSuchMethodException
    {
        String controllerName    =   path.getController();
        String controllerMethod  =   path.getControllerMethod();
        
        Class<?> controller      =   Class.forName(controllerName);
        return controller.getMethod(controllerMethod);
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
        String url        =   "user/{username}/messages/{message_id}";
        String withParams = "user/name=kyleruss/messages/message=1";
        getParamsFromUrl(withParams, url);
    }
}
