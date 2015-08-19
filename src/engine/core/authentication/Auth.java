package engine.core.authentication;

import engine.config.AuthConfig;
import engine.core.security.Input;
import engine.models.User;


public class Auth 
{
    private Session activeSession;
    
    public Auth()
    {
        
    }
    
    public boolean login(String username, String password)
    {
        try
        {
            username        =   Input.clean(username);
            password        =   Input.clean(password);
            String userCol  =   (String) AuthConfig.config().get(AuthConfig.USERNAME_COL_KEY);
            String passCol  =   (String) AuthConfig.config().get(AuthConfig.PASSWORD_COL_KEY);
            
            User attempt    =   new User(username);
            if(attempt.get(userCol) == null || attempt.get(passCol) == null) throw new Exception("Invalid info");
            
            
            
            return true;
        }
        
        catch(Exception e)
        {
            System.out.println("Login failed, please try again\nError: " + e.getMessage());
            return false;
        }
    }
}
