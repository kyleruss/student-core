//####################################
//  KYLE RUSSELL
//  13831056
//  PDC Project
//####################################


package engine.core.loggers;

import java.io.IOException;


public class DataLogger extends AbstractLogger
{
    private static final String LOG_FILE    =   "DataLog";
    
    public DataLogger() throws IOException
    {
        super(LOG_FILE);
    }
}
