
package engine.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RouteGroup
{
    private final String prefix;
    private RouteGroup parent;
    private final Map<String, Path> childPaths;
    private final List<RouteGroup> childGroups;
    
    public RouteGroup(String prefix)
    {
        this(prefix, null, new Path[]{});
    }
    
    public RouteGroup(String prefix, Path[] paths)
    {
        this(prefix, null, paths);
    }
    
    public RouteGroup(String prefix, RouteGroup parent, Path[] paths)
    {
        this.prefix =   prefix;
        this.parent =   parent;
        childPaths  =   new HashMap<>();
        childGroups =   new ArrayList<>();
        initPaths(paths);
    }
    
    private void initPaths(Path[] paths)
    {
        for(Path path : paths)
        {
            childPaths.put(path.getName(), path);
            path.setGroup(this);
        }
    }
    
    public String getPrefix()
    {
        return prefix;
    }
    
    public String getRootPrefix()
    {
        if(parent == null) return prefix + "/";
        else return getParent().getRootPrefix() + prefix + "/";
    }
    
    public RouteGroup root()
    {
        if(parent == null) return this;
        else return getParent().root();
    }
    
    public void setParent(RouteGroup parent)
    {
        this.parent = parent; 
    }
    
    public RouteGroup getParent() 
    {
        return parent;
    }
    
    public Map<String, Path> getChildren()
    {
        return childPaths;
    }
    
    public void addGroup(RouteGroup group)
    {
        childGroups.add(group);
        group.setParent(this);
    }
    
    public void addPath(Path path)
    {
        if(path != null)
            childPaths.put(path.getName(), path);
        
    }
}
