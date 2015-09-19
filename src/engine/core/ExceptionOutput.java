//====================================
//  KYLE RUSSELL
//  13831056
//  PDC Project
//====================================

package engine.core;

public class ExceptionOutput
{
    //The output type of the exception
    //DEBUG: for debug/testing types - not shown when DEBUG_MODE off
    //MESSAGE: for outputing general user errors messages to user 
    public enum OutputType
    {
        DEBUG,
        MESSAGE
    }
    
    //outputs the exceptio message 
    //output destination depends on debug mode and gui mode
    public static void output(String message)
    {
        
    }
    
    //outputs the exception message from e to appropriate destination
    //stackTrace: pass true/false to output the stacktraxce
    public static void output(Exception e, boolean stackTrace)
    {
        
    }
}
