//====================================
//	Kyle Russell
//	jdamvc
//	DebugLogger
//====================================

package engine.core.loggers;

import java.io.IOException;
import java.util.logging.Logger;


public class DebugLogger extends AbstractLogger
{
    private static final String LOG_FILE        =   "DebugLog";
    private static final Logger logger          =   Logger.getLogger(MainLogger.AUTH_LOGGER);
    
    public DebugLogger() throws IOException
    {
        super(LOG_FILE);
    }
}
