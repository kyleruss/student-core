//####################################
//  KYLE RUSSELL
//  13831056
//  PDC Project
//####################################

package engine.core.loggers;

import java.io.IOException;
import java.util.logging.Logger;


public class AuthLogger extends AbstractLogger
{
    private static final String LOG_FILE        =   "AuthLog";
    private static final Logger logger          =   Logger.getLogger(MainLogger.AUTH_LOGGER);
    
    public AuthLogger() throws IOException
    {
        super(LOG_FILE);
    }
}
