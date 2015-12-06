//====================================
//  KYLE RUSSELL
//  13831056
//  PDC Project
//====================================

package engine.core.loggers;

import java.io.IOException;
import java.util.logging.Logger;


public class DataLogger extends AbstractLogger
{
    private static final String LOG_FILE        =   "DataLog";
    private static final Logger logger          =   Logger.getLogger(MainLogger.DATA_LOGGER);
    
    public DataLogger() throws IOException
    {
        super(LOG_FILE);
    }
}
