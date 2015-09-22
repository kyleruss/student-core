//====================================
//  KYLE RUSSELL
//  13831056
//  PDC Project
//====================================

package engine.core;

import engine.config.AppConfig;
import engine.config.ConfigFactory;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


//------------------------------------------
//             ROUTER
//------------------------------------------
//- Router provides a structure to control routes
//- Routes can be easily added via add()
//- Holds named and url routes


public abstract class Router
{
    //The named routes
    //Routes can be fetched by their route name
    private final Map<String, Path> namedRoutes;
    
    //The url routes
    //Routes can be fetched directly by their url
    private final Map<String, Path> urlRoutes;
    
    private final RouteGroup base;
    
    public Router()
    {
        namedRoutes   =   new HashMap<>();
        urlRoutes     =   new HashMap<>();
        base          =   new RouteGroup((String) ConfigFactory.get(ConfigFactory.APP_CONFIG, AppConfig.APP_NAME));
        initRoutes();
    }
    
    //Routes need to be created and defined
    //Only routes created here will be identified
    protected abstract void initRoutes();

    //Adds a new route
    //- name: the routes name (can be used to identify the route)
    //- controller: the routes controller class name (must house controllerMethod)
    //- controllerMethod: the routes controller method name
    //- location: the routes url  
    public void add(String name, String controller, String controllerMethod, String location)
    {
        Path path   =   new Path(name, controller, controllerMethod, location);
        path.setGroup(base);
        add(path);
    }
    
    public void registerGroup(RouteGroup group)
    {
        Iterator<Path> pathIter    =   group.getChildren().values().iterator();
        while(pathIter.hasNext())
            add(pathIter.next());
        
        if(group.getParent() == null)
            base.addGroup(group);
    }
    
    //Adds a new route path
    //Convenient if path is already created
    public void add(Path path)
    {
        String routeName  =   path.getName();
        String location   =   path.getLocation();
        
        namedRoutes.put(routeName, path);
        urlRoutes.put(location, path);
    }
    
    //Destinction between route names and urls is necessary
    //Returns true if name is a path name and not url
    public static boolean isPathName(String name)
    {
        //path names contain only alphabetical characters
        Pattern pattern =   Pattern.compile("[a-z]+", Pattern.CASE_INSENSITIVE);
        Matcher match   =   pattern.matcher(name);
        return match.find();
    }
    
    //Returns a routes path
    //- name: the routes name or url
    public Path getPath(String name)
    {
        if(isPathName(name)) return namedRoutes.get(name);
        else return urlRoutes.get(name);
    }
    
    //Returns the routers named routes
    public Map<String, Path> getNamedRoutes()
    {
        return namedRoutes;
    }
    
    //Returns the routers url routes
    public Map<String, Path> getUrlRoutes()
    {
        return urlRoutes;
    }
}
