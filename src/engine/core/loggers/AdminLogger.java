//####################################
//  KYLE RUSSELL
//  13831056
//  PDC Project
//####################################

package engine.core.loggers;

import java.io.IOException;

public class AdminLogger extends AbstractLogger
{
    private static final String LOG_FILE    =   "AdminLog";
    
    public AdminLogger() throws IOException
    {
        super(LOG_FILE);
    }   

}
