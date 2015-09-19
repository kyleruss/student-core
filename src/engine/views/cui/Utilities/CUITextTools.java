
package engine.views.cui.Utilities;

import com.bethecoder.ascii_table.ASCIITable;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.text.MessageFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import org.apache.commons.lang3.StringUtils;


public class CUITextTools 
{
    public final static int WHITE   = 37;
    public final static int CYAN    = 36;
    public final static int RED     = 31;
    public final static int GREEN   = 32;
    public final static int BLUE    = 34;
    public final static int YELLOW  = 33;
    public final static int PLAIN   = 0;
    public final static int MAGENTA = 35;
    
    
    public static String drawButton(String buttonText)
    {
        String button;
        final int horPadding      =   5;
        final int horizontalLen   =   buttonText.length() + horPadding * 2;
        String horizontalFrame    =   new String(new char[horizontalLen]).replace("\0", "-");
        String spaces             =   new String(new char[(horizontalLen / 2) - (buttonText.length()/2) - 1]).replace("\0", " ");
        
        button = horizontalFrame + "\n|" + spaces + buttonText + spaces + "|\n" + horizontalFrame;
        System.out.println(button);
        return button;
    }
    
    public static String drawLargeHeader(String headerText, String headerDescription, int borderColour, int textColour)
    {
        String header;
        final int maxLength     =   25;
        final int padding       =   10;
        final int horizontalLen =   Math.max(headerDescription.length(), maxLength) + (padding * 2);
        
        String horizontalFrame       =   new String(new char[horizontalLen]).replace("\0", "* ");
        String spacesOuter           =   new String(new char[horizontalFrame.length() - 3]).replace("\0", " ");
        int lengthBeforeFormat       =   horizontalFrame.length();
        horizontalFrame              =   horizontalFrame.replace("* ", changeColour("* ", borderColour));
        
        
        String titleColoured        =   (textColour != PLAIN)? changeColour(headerText, textColour) : headerText;
        String descColoured         =   (textColour != PLAIN)? changeColour(headerDescription, textColour) : headerDescription;
        
        int colorOverflowTitle          =   titleColoured.length() - headerText.length();
        int colourOverflowDesc          =   descColoured.length() - headerDescription.length();
        
        String extraSpaceTitle          =   new String(new char[colorOverflowTitle]).replace("\0", " ");
        String extraSpacesDesc          =   new String(new char[colourOverflowDesc]).replace("\0", " ");
        
        header  =   horizontalFrame + "\n" + changeColour("*", borderColour) + spacesOuter  + changeColour("*", borderColour) + "\n";
        header  +=  changeColour("*", borderColour) + StringUtils.center(titleColoured, lengthBeforeFormat - 3) + extraSpaceTitle +  changeColour("*", borderColour) +"\n";
        header  +=  changeColour("*", borderColour) + StringUtils.center(descColoured, lengthBeforeFormat - 3) + extraSpacesDesc + changeColour("*", borderColour) + "\n";
        header  +=  changeColour("*", borderColour) + spacesOuter  + changeColour("*", borderColour) + "\n" + horizontalFrame;
       
        return header;
    }
    
    public static String drawSubHeader(String headerText, int borderColour, int textColour, String pattern)
    {
        final int padding           =   5;
        final int maxLength         =   12;
        final int horizontalLength  =   Math.max(headerText.length(), maxLength) + (padding * 2);
       
        String headerColoured           =   (textColour != PLAIN)? changeColour(headerText, textColour) : headerText;
        int colorOverflowTitle          =   headerColoured.length() - headerText.length();
        String extraSpaceTitle          =   new String(new char[colorOverflowTitle]).replace("\0", " ");
        
        String horizontalFrame  =   new String(new char[horizontalLength]).replace("\0", pattern + " ");
        String header           =   MessageFormat.format("{0}\n{1}\n{2}", horizontalFrame, StringUtils.center(headerColoured, horizontalFrame.length() + extraSpaceTitle.length()), horizontalFrame);
        return header;
    }
    
    public static String underline(String text)
    {
        String underlinedText;
        final int length    =   text.length();
        String underline    =   new String(new char[length]).replace("\0", "-");
        underlinedText      =   text + "\n" + underline;
        
        return underlinedText;
    }
    
    public static String keyText(String key, String text)
    {
        String keyText  =   MessageFormat.format("<{0}> {1}", key, text);
        return keyText;
    }
    
    public static String keyTextBrackets(String text, String key)
    {
        String keyText  =   MessageFormat.format("[{0}] {1}", key, text);
        return keyText;
    }
    
    public static void printDelayedText(String message)
    {
        Thread textThread   =   new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                for(int i = 0; i < message.length(); i++)
                {
                    System.out.print(message.charAt(i));

                    synchronized(this)
                    {
                        try { wait(300); }
                        catch(InterruptedException e)
                        {
                            System.out.println(e.getMessage());
                        }
                    }
                }
            }
        });
        textThread.start();
    }
    
    public static String changeColour(String text, int colour)
    {
        return ((char)27 + "[" + colour + "m"  + text + (char)27 + "[0m");
    }
    
    public static Map<String, String> getFormInput(List<String> fieldTitles, List<String> inputKeys, String[] headers)
    {
        Map<String, String> form    =   new LinkedHashMap<>();
        
        Thread inputThread  =   new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                boolean formInProgress  =   true;
                Scanner inputScan       =   new Scanner(System.in);

                while(formInProgress)
                {
                    for(int inputIndex = 0; inputIndex < inputKeys.size(); inputIndex++)
                    {
                     //   System.out.println(fieldTitles.get(inputIndex));
                     //   form.put(inputKeys.get(inputIndex), inputScan.nextLine());

                        try 
                        {
                            System.out.println(fieldTitles.get(inputIndex));
                            form.put(inputKeys.get(inputIndex), inputScan.nextLine());
                            synchronized(this)
                            {
                                wait(200);
                            }
                        } 

                        catch (InterruptedException ex) 
                        {
                        }
                    }

                    formInProgress = !confirmForm(form, headers, inputScan);
                }
            }
        });
        
        inputThread.start();
        
        try{ inputThread.join(); }   
        catch(InterruptedException e)
        {
            System.out.println("Error: " + e.getMessage());
        }
        
        return form;
    }
    
    public static boolean confirmForm(Map<String, String> form, String[] headers, Scanner scanner)
    {
        System.out.println("\n" + CUITextTools.changeColour("Please verify that the following details are correct before proceeding\n", CUITextTools.RED) + "\n");
        
        String[][] data      =   new String[1][form.size()];
        data[0]              =   (String[]) form.values().toArray(new String[form.size()]);

        ASCIITable.getInstance().printTable(headers, data);

        System.out.println("\n" + CUITextTools.changeColour("Continue? [Y/N]", CUITextTools.GREEN) + "\n");
        String continueAnswer   =   scanner.nextLine();
        return continueAnswer.equalsIgnoreCase("y");
    }
    
    public static String createFormField(String title, String description)
    {
        String formField    =   "";
        formField += "\n" + CUITextTools.drawSubHeader(title, CUITextTools.PLAIN, CUITextTools.CYAN, "#") + "\n";
        formField += CUITextTools.changeColour(description, CUITextTools.YELLOW) + "\n";
        formField += CUITextTools.changeColour("Enter " + title + ":\n", CUITextTools.GREEN);
        
        return formField;
    }
    
    public static void responseToTable(JsonArray response)
    {
            JsonArray columns   =   response.get(0).getAsJsonObject().get("columnNames").getAsJsonArray();
            
            String[][] data     =   new String[response.size() - 1][columns.size()];
            String[] headers    =   new String[columns.size()];
            
            for(int colIndex = 0; colIndex < headers.length; colIndex++)
                headers[colIndex] = columns.get(colIndex).getAsString();
            
            for(int rowIndex = 1; rowIndex <= data.length; rowIndex++)
            {
                JsonObject userRow  =   response.get(rowIndex).getAsJsonObject();
                
                for(int colIndex = 0; colIndex < headers.length; colIndex++)
                {
                    String colName  =   headers[colIndex];
                    String userCol  =   userRow.get(colName).getAsString();
                    data[rowIndex - 1][colIndex] = userCol;
                }
            }
            
            ASCIITable.getInstance().printTable(headers, data);
    }
}
