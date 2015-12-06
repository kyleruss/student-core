//====================================
//	Kyle Russell
//	jdamvc
//	AdminLogger
//====================================

package engine.core.loggers;

import java.io.IOException;
import java.util.logging.Logger;

public class AdminLogger extends AbstractLogger
{
    private static final String LOG_FILE        =   "AdminLog";
    private static final Logger LOGGER          =   Logger.getLogger(MainLogger.ADMIN_LOGGER);
    
    public AdminLogger() throws IOException
    {
        super(LOG_FILE);
    }   

}
