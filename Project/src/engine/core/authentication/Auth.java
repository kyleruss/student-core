//====================================
//	Kyle Russell
//	jdamvc
//	Auth
//====================================

package engine.core.authentication;

import engine.config.AuthConfig;
import engine.core.Agent;
import engine.core.ExceptionOutput;
import engine.core.loggers.MainLogger;
import engine.models.User;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

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
    public static Session login(String username, String password, boolean storeCredentials)
    {
        try
        {
            //Get user and password column names
            String userCol  =   AuthConfig.USERNAME_COL.toUpperCase();
            String passCol  =   AuthConfig.PASSWORD_COL.toUpperCase();
            
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
                
                StoredCredentials credentials   =   Agent.getStoredCredentials();
                Credentials foundCredentials    =   credentials.getUserCredentials(username);
                
                //Compare hashed password with stored hash
                //Log successful attempts
                //Sets the session in agent if successful
                //StoredCredentials stores pass hashes, if user is stored check matching hashes
                if(passHash.equals(passStored) || 
                ((foundCredentials != null && foundCredentials.getPassword().equals(passStored)) && foundCredentials.getPassword().equals(password)))
                {
                    String logMessage   =   MessageFormat.format("User {0} has logged in", username);
                    Agent.setActiveSession(new Session(attempt));
                    MainLogger.log(logMessage, MainLogger.AUTH_LOGGER);
                    
                    if(storeCredentials)
                    {
                        Credentials storedCred          =   credentials.getUserCredentials(username);
                        DateFormat format               =   new SimpleDateFormat("yyyy-mm-dd");
                        String saveDate                 =   format.format(new Date());

                        if(storedCred == null)
                            storedCred  =   new Credentials(username, passStored, saveDate, saveDate);

                        storedCred.setPassword(passStored);
                        storedCred.setLastLogged(saveDate);

                        credentials.setLastAccessed(username);
                        credentials.addCredential(username, storedCred);
                        credentials.saveCredentials();
                    }
                    
                    else 
                    {
                        if(credentials.removeCredential(username) != null)
                            credentials.saveCredentials();    
                    }
                    
                    return new Session(attempt);
                }
                
                else throw new Exception("Invalid password");
            }
           
        }
        
        catch(Exception e)
        {
            ExceptionOutput.output("Login Failed: " + e.getMessage(), ExceptionOutput.OutputType.DEBUG);
            return null;
        }
    }
    

}
