package engine.core.loggers;

import java.util.logging.Logger;
import org.junit.Test;
import static org.junit.Assert.*;


public class MainLoggerTest 
{     
   @Test
    public void testGet() 
    {
        String real_log_name =   MainLogger.AUTH_LOGGER;
        String fake_log_name =   "fake";
        
        Logger result_real  =   MainLogger.get(real_log_name);
        Logger result_fake  =   MainLogger.get(fake_log_name);

        assertNotNull(result_real);
        assertNull(result_fake);

    }
    
    
    @Test
    public void testLogWrite()
    {
        Logger admin_logger =   MainLogger.get(MainLogger.ADMIN_LOGGER);
        Logger auth_logger  =   MainLogger.get(MainLogger.AUTH_LOGGER);
        Logger data_logger  =   MainLogger.get(MainLogger.DATA_LOGGER);
        
        if (admin_logger == null) fail("Admin logger failed");
        else if (auth_logger == null) fail("Auth logger failed");
        else if (data_logger == null) fail("Data logger failed");
        else
        {
            try
            {
                admin_logger.fine("-- ADMIN LOGGER TEST --");
                auth_logger.fine("-- AUTH LOGGER TEST --");
                data_logger.fine("-- DATA LOGGER TEST --");
            }
            catch(Exception e)
            {
                fail("Failed writing to log - " + e.getMessage());
            }
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
