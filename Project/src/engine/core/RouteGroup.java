//====================================
//	Kyle Russell
//	jdamvc
//	RouteGroup
//====================================

package engine.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RouteGroup
{
    private final String prefix; //group prefix, children will inherit prefix in address
    private RouteGroup parent; //the parent group, allows nested and hierachy in routes
    private final Map<String, Path> childPaths; //the children paths (non-groups) of the group
    private final List<RouteGroup> childGroups; //the children groups (non-paths) of the group
    
    //Create a group with prefix
    //Add paths to it later
    public RouteGroup(String prefix)
    {
        this(prefix, null, new Path[]{});
    }
    
    //Create a group with a prefix add the paths to children
    public RouteGroup(String prefix, Path[] paths)
    {
        this(prefix, null, paths);
    }
    
    //Create group with parent and add paths to children
    public RouteGroup(String prefix, RouteGroup parent, Path[] paths)
    {
        this.prefix =   prefix;
        this.parent =   parent;
        childPaths  =   new HashMap<>();
        childGroups =   new ArrayList<>();
        initPaths(paths);
    }
    
    //Adds all the paths in the array to the children
    //sets the group for the children paths
    private void initPaths(Path[] paths)
    {
        for(Path path : paths)
        {
            childPaths.put(path.getName(), path);
            path.setGroup(this);
        }
    }
    
    //Returns the group address prefix
    public String getPrefix()
    {
        return prefix;
    }
    
    //Returns the group trees entire prefix
    public String getRootPrefix()
    {
        if(parent == null) return prefix + "/";
        else return getParent().getRootPrefix() + prefix + "/";
    }
    
    //Returns the root of the group tree 
    public RouteGroup root()
    {
        if(parent == null) return this;
        else return getParent().root();
    }
    
    //Set the parent of the group
    public void setParent(RouteGroup parent)
    {
        this.parent = parent; 
    }
    
    //Returns the parent of the group
    public RouteGroup getParent() 
    {
        return parent;
    }
    
    //Returns the children of the group
    public Map<String, Path> getChildren()
    {
        return childPaths;
    }
    
    //Adds a group to the group
    //Group will be added to this groups children
    //This group will be the passed groups parent
    public void addGroup(RouteGroup group)
    {
        childGroups.add(group);
        group.setParent(this);
    }
    
    //Adds a path to the children
    public void addPath(Path path)
    {
        if(path != null)
            childPaths.put(path.getName(), path);
        
    }
}
