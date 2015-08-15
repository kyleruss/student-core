
package engine.core;

import java.util.ArrayList;
import java.util.List;


public abstract class Routing
{
    private final List<Path> paths;
    
    public Routing()
    {
        paths   =   new ArrayList<>();
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
        paths.add(path);
    }
    
    public List<Path> getPaths()
    {
        return paths;
    }
}
