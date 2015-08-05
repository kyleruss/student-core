//####################################
//  KYLE RUSSELL
//  13831056
//  PDC Project
//####################################

package engine.config;

import java.util.Properties;

public class LoggingConfig extends Configuration 
{
    
    //-----------------------------------------------------------
    //                  LOG CONFIG KEYS
    //-----------------------------------------------------------
    public static final String AUTH_KEY     =   "auth_log";
    public static final String ADMIN_KEY    =   "admin_log";
    public static final String DATA_KEY     =   "data_log";
    //-----------------------------------------------------------
    
    
    //Create default config
    public LoggingConfig()
    {
        super();
    }
    
   //Create custom configs 
    public LoggingConfig(Properties config)
    {
        super(config);
    }
    
    @Override
    protected void initConfig()
    {
        //Enables/disables logging of all admin activities in system
        add(ADMIN_KEY, true); 
        
        //Enables/disables logging of user authentications and access
        add(AUTH_KEY, true);
        
        //Enables/disables logging of all database queries from server
        add(DATA_KEY, true);
    }
    
    //Factory => returns default logging config
    public static LoggingConfig config()
    {
        return new LoggingConfig();
    }
    
    //Factory => returns logging config with custom params
    public static LoggingConfig config(Properties config)
    {
        return new LoggingConfig(config);
    }
}