
package engine.core;


public class Routes extends Router 
{
    public Routes()
    {
        super();
    }
    
    @Override
    protected void initRoutes()
    {
        add("home", "BaseController", "getHome", "/");
    }
}
