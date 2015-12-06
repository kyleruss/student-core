//====================================
//  KYLE RUSSELL
//  13831056
//  PDC Project
//====================================

package engine.core.loggers;

import org.junit.Test;
import static org.junit.Assert.*;


public class MainLoggerTest 
{     
    //Tests writing to a log 
    //Writes to all logs 
    @Test
    public void testLogWrite()
    {
        final int numIterations  =   100;
        try
        {
            //Loop to observe any issues with concurrency and redunancy
           for (int i = 0; i < numIterations; i++)
           {
                MainLogger.log("-- ADMIN LOGGER TEST --", MainLogger.ADMIN_LOGGER);
                MainLogger.log("-- AUTH LOGGER TEST --", MainLogger.AUTH_LOGGER);
                MainLogger.log("-- DATA LOGGER TEST --", MainLogger.DATA_LOGGER);
                MainLogger.log("-- DEBUG LOGGER TEST --", MainLogger.DEBUG_LOGGER);
            }
        }
        
        catch(Exception e)
        {
            fail("Failed writing to log - " + e.getMessage());
        }
    } 

    //Tests creating all loggers 
    //Uses all valid loggers and tests invalid loggers
    @Test
    public void testCreate() 
    {
        assertNotNull(MainLogger.create(MainLogger.ADMIN_LOGGER));
        assertNotNull(MainLogger.create(MainLogger.AUTH_LOGGER));
        assertNotNull(MainLogger.create(MainLogger.DATA_LOGGER));
        assertNotNull(MainLogger.create(MainLogger.DEBUG_LOGGER));
        assertNull(MainLogger.create("invalid"));
        assertFalse(MainLogger.loggerEnabled("invalid"));
    }
    
}
