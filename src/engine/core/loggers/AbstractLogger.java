//####################################
//  KYLE RUSSELL
//  13831056
//  PDC Project
//####################################

package engine.core.loggers;

import engine.config.LoggingConfig;
import java.io.IOException;
import java.util.Date;

import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.SimpleFormatter;


public abstract class AbstractLogger implements Logging
{
    protected String log_file;
    protected Handler log_handler;
    protected SimpleFormatter log_formatter;
    
    public AbstractLogger(String log_file) throws IOException, NumberFormatException, SecurityException
    {
        this
        (
            MainLogger.formatLogName(log_file), 
            new FileHandler
            (
                MainLogger.formatLogName(log_file), 
                Integer.parseInt(LoggingConfig.config().get(LoggingConfig.LOG_SIZE_KEY)), 
                Integer.parseInt(LoggingConfig.config().get(LoggingConfig.LOG_FILE_COUNT_KEY))
            ), 
            new SimpleFormatter()
        );
    }
    
    
    public AbstractLogger(String log_file, Handler log_handler, SimpleFormatter log_formatter)
    {     
        this.log_file       =   log_file;
        this.log_handler    =   log_handler;
        this.log_formatter  =   log_formatter;
    }
    
    public static String test()
    {
        return "";
    }
    
    @Override
    public String getLogFile() 
    {
        return log_file;
    }

    @Override
    public Handler getHandler()
    {
        log_handler.setFormatter(log_formatter);
        return log_handler;
    }

    @Override
    public SimpleFormatter getFormatter()
    {
        return log_formatter;
    }

    @Override
    public String outputLog(Date log_date) 
    {
        return "";
    }
}
