
package engine.core;


public class Routes extends Routing 
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
