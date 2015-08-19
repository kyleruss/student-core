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
            if(attempt.get(userCol) == null || attempt.get(passCol) == null) throw new Exception("Account was not found");
            else
            {
                String passwordAuth =   (String) attempt.get(passCol).getColumnValue();
                //hash passed password and compare
                if(password.equals(passwordAuth)) System.out.println("Successfully logged in!");
                else throw new Exception("Invalid password");
            }
            
            
            
            return true;
        }
        
        catch(Exception e)
        {
            System.out.println("Login failed, please try again\nError: " + e.getMessage());
            return false;
        }
    }
}
