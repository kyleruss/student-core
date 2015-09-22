
package engine.core;

import java.util.List;

public class RouteGroup<E extends Path> extends Path implements RouteTree<RouteGroup>
{
    private RouteGroup parent;
    private List<E> children;

    public RouteGroup(String name, String location)
    {
        super(name, null, null, location);
        
    }
    
    @Override
    public RouteGroup parent() 
    {
        return parent;
    }

    public List<E> children() 
    {
        return children;
    }

    @Override
    public int level() 
    {
        return 0;
    }
    
    @Override
    public RouteGroup root()
    {
        if(parent == null) return this;
        else return parent().root();
    }
    
    public void setParent(RouteGroup parent)
    {
        this.parent = parent;
        parent.addChild(this);
    }
    
    public void addChild(E child)
    {
        children.add(child);
    }
}
