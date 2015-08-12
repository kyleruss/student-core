//####################################
//  KYLE RUSSELL
//  13831056
//  PDC Project
//####################################

package engine.core.loggers;

import engine.config.LoggingConfig;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainLogger 
{
    public static final String ADMIN_LOGGER =   "admin_logger";
    public static final String AUTH_LOGGER  =   "auth_logger";
    public static final String DATA_LOGGER  =   "data_logger"; 
    
    
    //Returns a core logger of known type
    //TODO: get rid of IOEXception, it's no longer thrown
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
    
    //Commits a log messsage* into logger*
    //Log level is set to FINE for all logging
    public static void log(String message, String logger_name)
    {
        Handler fh      =   null;
        Logger current  =   null;
        AbstractLogger core_logger;
        
        boolean loggingEnabled =  (boolean) LoggingConfig.config().get(logger_name);
        
        if(loggingEnabled)
        {
            try
            {
                core_logger  =   create(logger_name);
                fh           =   core_logger.getHandler();
                current      =   Logger.getLogger(logger_name);

                if(current == null || fh == null) throw new IOException();
                else
                {

                    //set logger params and attach handler
                    current.setLevel(Level.FINE);
                    current.setUseParentHandlers(false);
                    current.addHandler(fh);

                    //commit log
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
                if(fh != null && current != null)
                {
                    fh.flush();
                    fh.close();
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
