//####################################
//  KYLE RUSSELL
//  13831056
//  PDC Project
//####################################

package engine.core.loggers;

import java.io.IOException;


public class AuthLogger extends AbstractLogger
{
    private static final String LOG_FILE    =   "AuthLog";
    
    public AuthLogger() throws IOException
    {
        super(LOG_FILE);
    }
}
