package engine.core.authentication;

import engine.config.AuthConfig;
import engine.core.security.Input;
import engine.models.User;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;


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
    
    public static String hash(String message)
    {
        try 
        {
            String hash;
            MessageDigest enc   =   MessageDigest.getInstance("SHA-1");
            enc.update(message.getBytes("UTF-8"));
            byte[] digested     =   enc.digest();
     
            hash    = Base64.getEncoder().encodeToString(digested);
            return hash;
            //hash                =   digested.toString(16);
        } 
        
        catch (NoSuchAlgorithmException | UnsupportedEncodingException ex)
        {
            System.out.println(ex.getMessage());
            return null;
        }
    }
    
    public static void main(String[] args)
    {
        System.out.println(hash("fgsmg2asdsadsada" + "asdsd%@#$#$dASDs5352"));
    }
}
