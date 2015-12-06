//====================================
//	Kyle Russell
//	jdamvc
//	AbstractLogger
//====================================

package engine.core.loggers;

import engine.config.LoggingConfig;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.SimpleFormatter;

//--------------------------------------
//          ABSTRACTLOGGER
//--------------------------------------
//- Provides general logger type 
//- Defines the log path and pattern for name
//- Functions for reading the log 


public abstract class AbstractLogger implements Logging
{
    protected String log_file;
    protected SimpleFormatter log_formatter;
    
    public AbstractLogger(String log_file)
    {
        this
        (
            MainLogger.formatLogName(log_file) + "_%u%g" + LoggingConfig.LOG_FILE_EXT, 
            new SimpleFormatter()
        );
    }
    
    
    public AbstractLogger(String log_file, SimpleFormatter log_formatter)
    {     
        this.log_file       =   log_file;
        this.log_formatter  =   log_formatter;
    }
    
    //Returns the loggers file handler
    //handler must be flushed and closed when finished
    @Override
    public Handler getHandler() throws IOException, SecurityException
    {
        Handler fh =  new FileHandler
        (
                log_file, //Log file name
                LoggingConfig.LOG_MAX_SIZE, //Log max file size
                LoggingConfig.LOG_FILE_MAX_COUNT, //Max number of logs created after max size
                true //Allow appending to existing logs
        );
        
        //SimpleFormatter should be used over XMLFormatter
        fh.setFormatter(log_formatter);
        return fh;
    }
    
    //Returns the logs path 
    @Override
    public String getLogFile() 
    {
        return log_file;
    }

    //Returns the logs formatter
    @Override
    public SimpleFormatter getFormatter()
    {
        return log_formatter;
    }
}
