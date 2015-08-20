package engine.core.authentication;

import engine.config.AuthConfig;
import engine.core.Agent;
import engine.core.loggers.MainLogger;
import engine.core.security.Crypto;
import engine.core.security.Input;
import engine.models.User;
import java.text.MessageFormat;


public class Auth 
{
    
    public static Session login(String username, String password)
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
                if(passHash.equals(passStored)) 
                {
                    String logMessage   =   MessageFormat.format("User {0} has logged in", username);
                    Agent.setActiveSession(new Session(attempt));
                    MainLogger.log(logMessage, MainLogger.AUTH_LOGGER);
                    
                    return new Session(attempt);
                }
                
                else throw new Exception("Invalid password");
            }
           
        }
        
        catch(Exception e)
        {
            System.out.println(e.getMessage());
            return null;
        }
    }
    
    public static void main(String[] args)
    {
        login("kyleruss", "kyleruss123");
    }
}
