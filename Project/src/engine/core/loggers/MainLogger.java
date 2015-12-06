//====================================
//	Kyle Russell
//	jdamvc
//	MainLogger
//====================================

package engine.core.loggers;

import engine.config.LoggingConfig;
import engine.core.ExceptionOutput;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;


//------------------------------------
//            MAINLOGGER
//------------------------------------
//- Core logger that provides bridge to other loggers
//- Allows creation of loggers from type name
//- Logging is handled directly with MainLogger 


public final class MainLogger 
{
    public static final String ADMIN_LOGGER =   "admin_logger";
    public static final String AUTH_LOGGER  =   "auth_logger";
    public static final String DATA_LOGGER  =   "data_logger"; 
    public static final String DEBUG_LOGGER =   "debug_logger";
    
    
    //Returns a logger of known type
    public static AbstractLogger create(String logName)
    {
        try
        {
            switch(logName)
            {
                case AUTH_LOGGER: return new AuthLogger();
                case ADMIN_LOGGER: return new AdminLogger();
                case DATA_LOGGER: return new DataLogger();
                case DEBUG_LOGGER: return new DebugLogger();
                default: throw new IOException();
            }
        }
        
        catch(IOException e)
        {
            ExceptionOutput.output("[" + logName + "] Default log file cannot be found", ExceptionOutput.OutputType.DEBUG);
            return null;
        }
    }
    
    //Commits a log messsage from logger
    //Log is only written if logging is enabled
    public static void log(String message, String logger_name)
    {
        Handler handler      =   null;
        Logger current       =   null;
        AbstractLogger logger;
        
        
        if(loggerEnabled(logger_name))
        {
            try
            {
                //Get loggers and handler
                logger       =   create(logger_name);
                handler      =   logger.getHandler();
                current      =   Logger.getLogger(logger_name);

                if(current == null || handler == null) throw new IOException();
                else
                {

                    //set logger params and attach handler
                    current.setLevel(Level.FINE);
                    current.setUseParentHandlers(false);
                    current.addHandler(handler);

                    //Write log with message
                    current.fine(message);

                }
            }

            catch(IOException | SecurityException e)
            {
                ExceptionOutput.output("Failed to commit log: " + e.getMessage(), ExceptionOutput.OutputType.DEBUG);
            }

            //flush and close handlers
            finally
            {
                if(handler != null && current != null)
                {
                    handler.flush();
                    handler.close();
                }
            }
        }
    }
    
    //Returns true if the logger passed is enabled in config
    //To enable/disable loggers see engine\core\loggers\LoggingConfig
    public static boolean loggerEnabled(String loggerName)
    {
        switch(loggerName)
        {
            case ADMIN_LOGGER:
            case AUTH_LOGGER:
            case DATA_LOGGER:
            case DEBUG_LOGGER:
                return true;
            default: return false;
        }
    }
    
    //Returns the correct formatting including path of log file
    //Template: /FOLDER/logname_dd-mm-yyy
    public static String formatLogName(String file)
    {
        final char delimiter            =   '_';
        final String folder             =   LoggingConfig.LOG_PATH;
        Date time                       =   new Date();
        SimpleDateFormat date_format    =   new SimpleDateFormat("dd-MM-yyyy");
        String date                     =   date_format.format(time);
        
        String formatted_file   =   folder + file + delimiter + date;
        return formatted_file;
    }   
}
