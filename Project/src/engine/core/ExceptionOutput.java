//====================================
//	Kyle Russell
//	jdamvc
//	ExceptionOutput
//====================================

package engine.core;

import engine.config.AppConfig;
import engine.core.loggers.MainLogger;
import engine.views.cui.Utilities.CUITextTools;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.swing.JOptionPane;

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
    
    //outputs the exception message 
    //output destination depends on debug mode and gui mode
    public static void output(String message, OutputType outputType)
    {
        //Output to console
        if(!AppConfig.GUI_MODE)
        {
            String prefix;
            if(outputType == OutputType.DEBUG)
            {
                prefix = "[Debug] ";
                MainLogger.log(message, MainLogger.DEBUG_LOGGER);
            }
            
            else
                prefix = "[Error] ";
            
            String output   =   CUITextTools.changeColour(prefix + message, CUITextTools.RED);
            System.out.println(output);
        }
        
        //Output to GUI
        else
        {
            //Only output to console messages to avoid spamming user
            if(outputType == OutputType.MESSAGE)
                JOptionPane.showMessageDialog(null, message);
            else if(AppConfig.DEBUG_MODE)
                System.out.println("[Debug] " + message);
        }
    }
    
    //outputs the exception message from e to appropriate destination
    //stackTrace: pass true/false to output the stacktrace
    public static void output(Exception e, boolean stackTrace, OutputType outputType)
    {
        if(!stackTrace)
            output(e.getMessage(), outputType);
        else
        {
            String trace;
            StringWriter writer =   new StringWriter();
            e.printStackTrace(new PrintWriter(writer));
            trace = writer.toString();
            
            output(trace, outputType);
        }
    }
}
