//====================================
//  KYLE RUSSELL
//  13831056
//  PDC Project
//====================================

package engine.config;

import engine.core.loggers.MainLogger;
import java.util.Properties;

public class LoggingConfig extends Configuration
{
    //--------------------------------------------------------------------
    //                          CONFIG KEYS
    //--------------------------------------------------------------------
    public static final String ENABLE_AUTH_LOG_KEY     =   MainLogger.AUTH_LOGGER;
    public static final String ENABLE_ADMIN_LOG_KEY    =   MainLogger.ADMIN_LOGGER;
    public static final String ENABLE_DATA_LOG_KEY     =   MainLogger.DATA_LOGGER;
    public static final String ENABLE_DEBUG_LOG_KEY    =   MainLogger.DEBUG_LOGGER;
    public static final String LOG_SIZE_KEY            =   "log_size";
    public static final String LOG_PATH_KEY            =   "log_path";
    public static final String LOG_FILE_COUNT_KEY      =   "log_count";
    public static final String LOG_EXTENSION           =   "log_ext";
    //--------------------------------------------------------------------
    
    
    public LoggingConfig()
    {
        super();
    }
    
    public LoggingConfig(Properties config)
    {
        super(config);
    }
    
    @Override
    protected void initConfig()
    {
        //enables/disables logging of all admin activities in system
        add(ENABLE_AUTH_LOG_KEY, true);
        
        //enables/disables logging of user authentication and access
        add(ENABLE_ADMIN_LOG_KEY, true);
        
        //enables/disable logging of all database queries from server
        add(ENABLE_DATA_LOG_KEY, true);
        
        //enables/disable logging of debug messages
        add(ENABLE_DEBUG_LOG_KEY, true);
        
        //set the max size in bytes for each log file
        add(LOG_SIZE_KEY, "5242880");
        
        //set the logs directory
        add(LOG_PATH_KEY, "logs/");
        
        //set the max number of log files created after hitting max size
        add(LOG_FILE_COUNT_KEY, "1");
        
        //set the default log extension
        add(LOG_EXTENSION, ".log");
    }
    
    //factory => default logging config
    public static LoggingConfig config()
    {
        return new LoggingConfig();
    }
    
    //factory => logging config with custom params
    public static LoggingConfig config(Properties config)
    {
        return new LoggingConfig(config);
    }
}
