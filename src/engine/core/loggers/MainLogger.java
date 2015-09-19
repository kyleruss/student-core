//====================================
//  KYLE RUSSELL
//  13831056
//  PDC Project
//====================================

package engine.core.loggers;

import engine.config.LoggingConfig;
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
    public static AbstractLogger create(String log_type)
    {
        try
        {
            switch(log_type)
            {
                case AUTH_LOGGER: return new AuthLogger();
                case ADMIN_LOGGER: return new AdminLogger();
                case DATA_LOGGER: return new DataLogger();
                default: throw new IOException();
            }
        }
        
        catch(IOException e)
        {
            System.out.println("[" + log_type + "] Default log file cannot be found");
            return null;
        }
    }
    
    //Commits a log messsage from logger
    //Log is only written if logging is enabled
    //
    public static void log(String message, String logger_name)
    {
        Handler handler      =   null;
        Logger current       =   null;
        AbstractLogger logger;
        
        boolean loggingEnabled =  (boolean) LoggingConfig.config().get(logger_name);
        
        if(loggingEnabled)
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
                System.out.println("Failed to commit log");
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
    
    //Returns the correct formatting including path of log file
    //Template: /FOLDER/logname_dd-mm-yyy
    public static String formatLogName(String file)
    {
        final char delimiter            =   '_';
        final String folder             =   (String) LoggingConfig.config().get(LoggingConfig.LOG_PATH_KEY);
        Date time                       =   new Date();
        SimpleDateFormat date_format    =   new SimpleDateFormat("dd-MM-yyyy");
        String date                     =   date_format.format(time);
        
        String formatted_file   =   folder + file + delimiter + date;
        return formatted_file;
    }   
}
