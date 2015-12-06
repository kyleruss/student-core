//====================================
//  KYLE RUSSELL
//  13831056
//  PDC Project
//====================================

package engine.core;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CommandListenerTest
{
    CommandListener listener;
    
    @Before
    public void setUp()
    {
        listener    =   new CommandListener();
    } 

    //Tests loading listener files
    //Attempts to load a valid and invalid listener file
    @Test
    public void testLoad()
    {
        final String REAL_PATH =   "/engine/config/listeners/AgentListener.json";
        final String FAKE_PATH =   "/engine/config/listeners/notfound.json";
        
        CommandListener realListener, fakeListener;
        realListener = fakeListener = null;
        
        try
        {
            realListener    =   listener.load(REAL_PATH);
            fakeListener    =   listener.load(FAKE_PATH);
            fail();
        }
        
        catch(Exception e)
        {
            assertNotNull(realListener);
            assertNull(fakeListener);
        }
    }

    //Tests loading a listener file from the CommandListeners factory method
    //Uses a valid listener file
    @Test
    public void testLoadFactory()
    {
        CommandListener realListener;
        final String REAL_PATH =   "/engine/config/listeners/AgentListener.json";
        
        try
        {
            realListener           =   CommandListener.loadFactory(REAL_PATH);
        }
        
        catch(Exception e)
        {
            fail();
        }
    }
    
}
