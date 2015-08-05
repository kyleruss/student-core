//####################################
//  KYLE RUSSELL
//  13831056
//  PDC Project
//####################################

package engine.core.loggers;

import java.util.Date;
import java.util.logging.Handler;
import java.util.logging.SimpleFormatter;


public interface Logging
{
    //Returns the loggers log file 
    public String getLogFile();
    
    //Returns the loggers handler 
    public Handler getHandler();
    
    //Returns the loggers formatter
    public SimpleFormatter getFormatter();
    
    //Returns the log at log_date if it exists
    public String outputLog(Date log_date);
    
    
}
