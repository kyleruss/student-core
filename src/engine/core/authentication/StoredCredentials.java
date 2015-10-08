
package engine.core.authentication;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import engine.config.AppConfig;
import engine.config.ConfigFactory;
import engine.core.ExceptionOutput;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class StoredCredentials
{
    private String lastAccessed;
    private Map<String, Credentials> credentials;
    
    public StoredCredentials()
    {
        lastAccessed    =   null;
        credentials     =   new HashMap<>();
    }
    
    public String getLastAccessed()
    {
        return lastAccessed;
    }
    
    public Map<String, Credentials> getCredentials()
    {
        return credentials;
    }
    
    public void setLastAccessed(String lastAccessed)
    {
        this.lastAccessed   =   lastAccessed;
    }
    
    public void setCredentials(Map<String, Credentials> credentials)
    {
        this.credentials    =   credentials;
    }
    
    public void addCredential(String username, Credentials cred)
    {
        credentials.put(username, cred);
    }
    
    public void removeCredential(String username)
    {
        credentials.remove(username);
    }
    
    public void replaceCredential(String username, Credentials cred)
    {
        credentials.replace(username, cred);
    }
    
    public Credentials getUserCredentials(String username)
    {
        return credentials.get(username);
    }
    
    public void saveCredentials()
    {
        if((boolean) ConfigFactory.get(ConfigFactory.APP_CONFIG, AppConfig.ALLOW_PASS_SAVE))
        {
            String file         =   (String) ConfigFactory.get(ConfigFactory.APP_CONFIG, AppConfig.PASS_SAVE_FILE);
            Gson gson           =   new GsonBuilder().setPrettyPrinting().create();
            String storedCreds  =   gson.toJson(this);
            System.out.println("save: " + storedCreds);
            
            try(BufferedWriter bw   =   new BufferedWriter(new FileWriter(file)))
            {
                bw.write(storedCreds);
            }
            
            catch(IOException e)
            {
                ExceptionOutput.output("Failed to load resource, " + e.getMessage(), ExceptionOutput.OutputType.DEBUG);
            }
        }
    }
    
    //TODO: get saved credentials of pertricular user
    
   public static StoredCredentials getSavedCredentials()
    {
        if((boolean) ConfigFactory.get(ConfigFactory.APP_CONFIG, AppConfig.ALLOW_PASS_SAVE))
        {
            String file =   (String) ConfigFactory.get(ConfigFactory.APP_CONFIG, AppConfig.PASS_SAVE_FILE);
            if(file == null) return null;
            
            try(BufferedReader br   =   new BufferedReader(new FileReader(file)))
            {
                
                Gson gson   =   new Gson();
                return gson.fromJson(br, StoredCredentials.class);
            }
            
            catch(IOException e)
            {
                ExceptionOutput.output("Failed to oepn resourse, " + e.getMessage(), ExceptionOutput.OutputType.DEBUG);
                return null;
            }
        }
        
        else return null;
    } 
   
   public static void main(String[] args)
   {
       StoredCredentials cred   =   new StoredCredentials();
       cred.setLastAccessed("asdsad");
       cred.addCredential("test", new Credentials("username", "pass", "223sad", "a2342"));
       cred.addCredential("test2", new Credentials("username", "pass", "223sad", "a2342")); 
       
       Gson gson    =   new GsonBuilder().setPrettyPrinting().create();
       
       System.out.println(gson.toJson(cred));
       
   }
}
