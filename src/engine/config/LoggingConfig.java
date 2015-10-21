//====================================
//  KYLE RUSSELL
//  13831056
//  PDC Project
//====================================

package engine.config;

import engine.core.loggers.MainLogger;
import java.util.Properties;

public class LoggingConfig
{
    //--------------------------------------------------------------------
    //                          CONFIG KEYS
    //--------------------------------------------------------------------
    //enables/disables logging of all admin activities in system
    public static final boolean ENABLE_AUTH_LOG       =       true;
    
    //enables/disables logging of user authentication and access
    public static final boolean ENABLE_ADMIN_LOG      =       true;
    
    //enables/disable logging of all database queries from server
    public static final boolean ENABLE_DATA_LOG       =       true;
    
    //enables/disable logging of debug messages
    public static final boolean ENABLE_DEBUG_LOG      =       true;
    
    //set the max size in bytes for each log file
    public static final int LOG_MAX_SIZE              =       5242880;
    
    //set the logs directory
    public static final String LOG_PATH               =       "logs/";
    
    //set the max number of log files created after hitting max size
    public static final int LOG_FILE_MAX_COUNT        =       1;
    
    //set the default log extension
    public static final String LOG_FILE_EXT           =       ".log";
}
