
package engine.views.cui;

import com.bethecoder.ascii_table.ASCIITable;
import engine.core.Agent;
import engine.views.AbstractView;
import engine.views.View;
import engine.views.cui.Utilities.CUITextTools;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;


public class RegisterView extends AbstractView implements View
{

    @Override
    public void display()
    {
        Agent.commandInProgress();
        Map<String, String> accDetails = getRegisterInput();
        //accDetails.put("", null)
        
        /*String[] header        =   { "Username", "Password", "First name", "Last name", "Gender", "Phone", "Email", "Ethnicity" };
        String[][] data      =   new String[accDetails.size()][accDetails.size()];
        data[0]              =   new String[] { accDetails.get("registerUsername"), accDetails.get("registerPassword") };//(String[]) accDetails.values().toArray(new String[accDetails.size()]);
        
        System.out.println(accDetails.size());
        System.out.println("Please verify that the following details are correct before proceeding");
        System.out.println(data[0][0]);*/
        //printDetails(headers, details);
        
       //ASCIITable.getInstance().printTable(header, data);
        System.out.println("Please verify that the following details are correct before proceeding");
                
        
        Agent.commandFinished();
    }
    
    public Map<String, String> getRegisterInput()
    {
       
        
        Thread p = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                 Map<String, String> form    =   new LinkedHashMap<>();
                Scanner inputScan           =   new Scanner(System.in);

            //Username
            printFormField("Username", "Your unique StudentCore account name, 6-16 alphanumeric characters");
            form.put("registerUsername", inputScan.nextLine());

            //Password
            printFormField("Password", "Your StudentCore password, 6-18 characters");
            form.put("registerPassword", inputScan.nextLine());
            
            //Password
            printFormField("First name", "Your legal first name");
            form.put("registerFirstname", inputScan.nextLine());

            //Password
            printFormField("Last name", "Your legel last name/surname");
            form.put("registerLastname", inputScan.nextLine());

            //Password
            printFormField("Gender/Sex", "What is your gender/sex? [Male/Female]");
            form.put("registerGender", inputScan.nextLine());

            //Password
            printFormField("Contact phone", "What is your contact phone number? 7-12 digits");
            form.put("registerPhone", inputScan.nextLine());   

            //Password
            printFormField("Contact email", "What is your contact email address, may be used for verification");
            form.put("registerEmail", inputScan.nextLine());

            //Password
            printFormField("Ethnicity", "What is your recognized ethnicity?");
            form.put("registerEthnicity", inputScan.nextLine()); 
            
            System.out.println("TEST: " + form.get("registerUsername"));
            String[] header        =  { "Username", "Password", "First name", "Last name", "Gender", "Phone", "Email", "Ethnicity" };
            String[][] data      =   new String[1][form.size()];
            data[0]              =   (String[]) form.values().toArray(new String[form.size()]);//new String[] { form.get("registerUsername"), form.get("registerPassword") };


            ASCIITable.getInstance().printTable(header, data);
            }
        });
        p.start();
        try {
            p.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(RegisterView.class.getName()).log(Level.SEVERE, null, ex);
        }
       
        return null;
    }
    
    public void printDetails(String[] headers, String[][] rows)
    {
        ASCIITable.getInstance().printTable(headers, rows);
    }
    
    public void printFormField(String title, String description)
    {
        System.out.println("\n" + CUITextTools.drawSubHeader(title, CUITextTools.PLAIN, CUITextTools.CYAN, "#") + "\n");
        System.out.println(CUITextTools.changeColour(description, CUITextTools.YELLOW));
        System.out.println(CUITextTools.changeColour("Enter your " + title + ":\n", CUITextTools.GREEN));
    }

    @Override
    protected String getCommandsFile()
    {
        return "/engine/config/listeners/RegisterListener.json";
    }
    
    
    public static void main(String[] args)
    {
          String [] header = { "User Name", "asdsd" };
                
            String[][] data = { {"asdsad","Aasdsd"}};


            ASCIITable.getInstance().printTable(header, data);
    }
    
}
