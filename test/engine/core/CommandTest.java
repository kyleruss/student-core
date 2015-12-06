//====================================
//  KYLE RUSSELL
//  13831056
//  PDC Project
//====================================

package engine.core;

import engine.views.cui.TestView;
import org.junit.Test;
import org.junit.*;
import static org.junit.Assert.*;
import utilities.TestUtilities;


public class CommandTest
{
    Command withoutParams; //Command method call that has no parameters
    Command withParams; //Command method call that has parameters
    
    @Before
    public void setUp()
    {
        //Uses engine/views/cui/TestView
        //withoutParams: methodWithoutParam()
        //withParams: methodWithParam(String)
        withoutParams   =   new Command("test", new String[] {}, "engine.views.cui.TestView", "methodWithoutParam", "Description");
        withParams      =   new Command("test", new String[] { "java.lang.String" }, "engine.views.cui.TestView","methodWithParam", "Description");
    }
    
    //Tests calling a method from the command
    //Tests both with and without param commands
    //Does not use an existing instance
    @Test
    public void testCall()
    {
        try
        {
            TestUtilities.formatHeader("WITHOUT INSTANCE");
            Object callNoParams =   withoutParams.call(new String[] {}, null);
            assertNull(callNoParams);

            Object callParams   =   withParams.call(new String[] { "Message" }, null);
            assertNotNull(callParams); 
        }
        
        catch(Exception e)
        {
            fail();
        }
    }
    
    //Tests calling a method from the command
    //Tests both with and without param commands
    //Uses a passed instance 
    @Test
    public void testCallWithInstance()
    {
        TestView instance   =   new TestView();
        try
        {
            TestUtilities.formatHeader("WITH INSTANCE");
            Object callNoParams =   withoutParams.call(new String[] {}, instance);
            assertNull(callNoParams);

            Object callParams   =   withParams.call(new String[] { "Instance" }, instance);
            assertNotNull(callParams); 
        }
        
        catch(Exception e)
        {
            fail();
        }
    }
}
