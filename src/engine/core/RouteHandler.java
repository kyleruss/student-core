
package engine.core;

import java.lang.reflect.Method;

public class RouteHandler 
{
    private Routes routes;
    
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
}
