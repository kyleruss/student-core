//====================================
//  KYLE RUSSELL
//  13831056
//  PDC Project
//====================================

package engine.core.authentication;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import engine.config.AppConfig;
import engine.config.AuthConfig;
import engine.config.ConfigFactory;
import engine.core.Agent;
import engine.core.ExceptionOutput;
import engine.core.loggers.MainLogger;
import engine.core.security.Crypto;
import engine.core.security.Input;
import engine.models.User;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.MessageFormat;

//---------------------------------------
//              AUTH
//---------------------------------------
//- Auth provides methods for user authentication 
//- Has methods for logging users in and includes hash matching, logging and cleaning

public class Auth 
{
    //Attempts to log in with the username and password passed
    //Returns a session if sucessful attempt
    //Input is cleaned and successfuly attempts are logged
    //Passwords are hashed and compared with the stored hash
    //- username: the users valid distinct username
    //- password: plain text input password (NOT HASH)
    public static Session login(String username, String password)
    {
        try
        {
            //Clean username & password input
            username        =   Input.clean(username);
            password        =   Input.clean(password);
            
            //Get user and password column names
            String userCol  =   ((String) AuthConfig.config().get(AuthConfig.USERNAME_COL_KEY)).toUpperCase();
            String passCol  =   ((String) AuthConfig.config().get(AuthConfig.PASSWORD_COL_KEY)).toUpperCase();
            
            //Fetch user
            User attempt    =   new User(username);
            if(attempt.get(userCol) == null || attempt.get(passCol) == null) throw new Exception("Account was not found");
            else
            {
                //Get the stored hash
                String passStored   =   attempt.get(passCol).getNonLiteralValue().toString();
                
                //salt & hash the attempted password
                String salt         =   Crypto.salt(username);
                String passHash     =   Crypto.makeHash(salt, password);
                
                //Compare hashed password with stored hash
                //Log successful attempts
                //Sets the session in agent if successful
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
           // System.out.println("login exception: " + e.getMessage());
            return null;
        }
    }
    
    public void storeCredentials()
    {
        if(!(boolean) ConfigFactory.get(ConfigFactory.APP_CONFIG, AppConfig.ALLOW_PASS_SAVE))
        {
            
        }
    }
    
    //TODO: get saved credentials of pertricular user
    
    public static JsonArray getSavedCredentials()
    {
        if(!(boolean) ConfigFactory.get(ConfigFactory.APP_CONFIG, AppConfig.ALLOW_PASS_SAVE))
        {
            String file =   (String) ConfigFactory.get(ConfigFactory.APP_CONFIG, AppConfig.PASS_SAVE_FILE);
            if(file == null) return null;
            
            String credentials  =   "";
            try(BufferedReader br   =   new BufferedReader(new FileReader(file)))
            {
                String line;
                while((line = br.readLine()) != null)
                    credentials += line;
                
                Gson gson   =   new Gson();
               
            }
            
            catch(IOException e)
            {
                ExceptionOutput.output("Failed to oepn resourse, " + e.getMessage(), ExceptionOutput.OutputType.DEBUG);
                return null;
            }
        }
        
        else return null;
    }
}
