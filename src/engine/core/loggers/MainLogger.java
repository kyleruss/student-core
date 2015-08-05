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
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainLogger 
{
    public static final String ADMIN_LOGGER =   "admin_logger";
    public static final String AUTH_LOGGER  =   "auth_logger";
    public static final String DATA_LOGGER  =   "data_logger"; 
    
    public static Logger get(String logger_name)
    {
        Logger logger               =   Logger.getLogger(logger_name);
        AbstractLogger core_logger  =   create(logger_name);
        
        if(logger == null || core_logger == null) return null;
        else
        {
            logger.setLevel(Level.FINE);
            logger.setUseParentHandlers(false);
            logger.addHandler(core_logger.getHandler());

            return logger;
        }
    }
    
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
    
    public static String formatLogName(String file)
    {
        final String extension          =   ".log";
        final char delimiter            =   '_';
        final String folder             =   LoggingConfig.config().get(LoggingConfig.LOG_PATH_KEY);
        Date time                       =   new Date();
        SimpleDateFormat date_format    =   new SimpleDateFormat("dd-mm-yyyy");
        String date                     =   date_format.format(time);
        
        String formatted_file   =   folder + file + delimiter + date + extension;
        System.out.println(formatted_file);
        return formatted_file;
    }
    
    
}
