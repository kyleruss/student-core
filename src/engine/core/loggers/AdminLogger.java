//####################################
//  KYLE RUSSELL
//  13831056
//  PDC Project
//####################################

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
