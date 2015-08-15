
package engine.core;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public abstract class Router
{
    private final Map<String, Path> namedPaths;
    private final Map<String, Path> urlPaths;
    
    public Router()
    {
        namedPaths   =   new HashMap<>();
        urlPaths     =   new HashMap<>();
        initRoutes();
    }
    
    protected abstract void initRoutes();

    public void add(String name, String controller, String controllerMethod, String location)
    {
        Path path   =   new Path(name, controller, controllerMethod, location);
        add(path);
    }
    
    public void add(Path path)
    {
        String routeName  =   path.getName();
        String location   =   path.getLocation();
        
        namedPaths.put(routeName, path);
        urlPaths.put(location, path);
    }
    
    public boolean isPathName(String name)
    {
        Pattern pattern =   Pattern.compile("[a-z]+", Pattern.CASE_INSENSITIVE);
        Matcher match   =   pattern.matcher(name);
        return match.find();
    }
    
    public Path getPath(String name)
    {
        if(isPathName(name)) return namedPaths.get(name);
        else return urlPaths.get(name);
    }
    
    public Map<String, Path> getNamedPaths()
    {
        return namedPaths;
    }
    
    public Map<String, Path> getUrlPaths()
    {
        return urlPaths;
    }
}
