package engine.core.loggers;

import org.junit.Test;
import static org.junit.Assert.*;


public class MainLoggerTest 
{     
    @Test
    public void testLogWrite()
    {
        final int stress_num  =   100;
        try
        {
            for (int i = 0; i < stress_num; i++)
           {
                MainLogger.log("-- ADMIN LOGGER TEST --", MainLogger.ADMIN_LOGGER);
                MainLogger.log("-- AUTH LOGGER TEST --", MainLogger.AUTH_LOGGER);
                MainLogger.log("-- DATA LOGGER TEST --", MainLogger.DATA_LOGGER);
            }
        }
        
        catch(Exception e)
        {
            fail("Failed writing to log - " + e.getMessage());
        }
    } 

    
    @Test
    public void testCreate() 
    {
        String real_log_name        =   MainLogger.ADMIN_LOGGER;
        String bogus_log_name       =   "fake"; 
        
        AbstractLogger result_real  =   MainLogger.create(real_log_name);
        AbstractLogger result_fake  =   MainLogger.create(bogus_log_name);
        
        assertNotNull(result_real);
        assertNull(result_fake);
    }
    
}
