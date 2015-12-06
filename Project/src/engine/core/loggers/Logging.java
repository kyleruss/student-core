//====================================
//	Kyle Russell
//	jdamvc
//	Logging
//====================================

package engine.core.loggers;

import java.io.IOException;
import java.util.logging.Handler;
import java.util.logging.SimpleFormatter;


public interface Logging
{
    //Returns the loggers log file 
    public String getLogFile();
    
    //Returns the loggers handler 
    public Handler getHandler() throws IOException, SecurityException;
    
    //Returns the loggers formatter
    public SimpleFormatter getFormatter();
}
