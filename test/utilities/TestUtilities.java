
package utilities;

import java.text.MessageFormat;


public class TestUtilities 
{
    public static void formatHeader(String title, String message)
    {
        formatHeader(title);
        System.out.println(message);
    }
    
    public static void formatHeader(String title)
    {
        String outline  =   new String(new char[40]).replace("\0", "-");
        String format   =   MessageFormat.format("\n{0}\n\t{1}\n{2}", outline, title, outline);
        System.out.println(format);
    }
    
    public static void formatSubHeader(String title, String message)
    {
        formatSubHeader(title);
        System.out.println(message);
    }
    
    public static void formatSubHeader(String title)
    {
        String format   =   MessageFormat.format("\n[{0}]\n", title);
        System.out.println(format);
    }
}
