package engine.core.authentication;

import engine.config.AuthConfig;
import engine.core.security.Crypto;
import engine.core.security.Input;
import engine.models.User;


public class Auth 
{
    private Session activeSession;
    
    public Auth()
    {
        
    }
    
    public static boolean login(String username, String password)
    {
        try
        {
            username        =   Input.clean(username);
            password        =   Input.clean(password);
            String userCol  =   ((String) AuthConfig.config().get(AuthConfig.USERNAME_COL_KEY)).toUpperCase();
            String passCol  =   ((String) AuthConfig.config().get(AuthConfig.PASSWORD_COL_KEY)).toUpperCase();
            
            User attempt    =   new User(username);
            if(attempt.get(userCol) == null || attempt.get(passCol) == null) throw new Exception("Account was not found");
            else
            {
                String passStored   =   attempt.get(passCol).getNonLiteralValue().toString();
                
                //salt & hash the attempted password
                String salt         =   Crypto.salt(username);
                String passHash     =   Crypto.makeHash(salt, password);
                
                //hash passed password and compare
                if(passHash.equals(passStored)) System.out.println("Successfully logged in!");
                else throw new Exception("Invalid password");
            }
            
            
            
            return true;
        }
        
        catch(Exception e)
        {
            e.printStackTrace();
            System.out.println("Login failed, please try again\nError: " + e.getMessage());
            return false;
        }
    }
    
    public static void main(String[] args)
    {
        login("kyleruss", "kyleruss123");
    }
}
