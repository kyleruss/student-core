
package engine.core;

public interface RouteTree<E>
{
    public E parent();
    
    public int level();
    
    public E root();
}
