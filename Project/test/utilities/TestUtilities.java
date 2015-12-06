//====================================
//  KYLE RUSSELL
//  13831056
//  PDC Project
//====================================

package utilities;

import java.text.MessageFormat;

//-----------------------------------------------------------
//                         TEST UTILS
//-----------------------------------------------------------
//- A simple utility class for clear formatting in unit tests
//- Provides methods for headers & sub-headers

public class TestUtilities 
{
    //Prints a header with outline
    //pads title inside outline
    public static void formatHeader(String title)
    {
        String outline  =   new String(new char[40]).replace("\0", "-");
        String format   =   MessageFormat.format("\n{0}\n\t{1}\n{2}", outline, title, outline);
        System.out.println(format);
    }
    
    //Prints a header & outline with a lb and message content below
    public static void formatHeader(String title, String message)
    {
        formatHeader(title);
        System.out.println(message);
    }
    
    //Prints a subheader with a padded title inside a outline 
    public static void formatSubHeader(String title)
    {
        String format   =   MessageFormat.format("\n[{0}]\n", title);
        System.out.println(format);
    }
    
    //Prints a subheader of title and prints the message content below
    public static void formatSubHeader(String title, String message)
    {
        formatSubHeader(title);
        System.out.println(message);
    }
}
