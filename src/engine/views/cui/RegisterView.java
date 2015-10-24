//====================================
//  KYLE RUSSELL
//  13831056
//  PDC Project
//====================================

package engine.views.cui;

import engine.views.ResponseDataView;
import com.bethecoder.ascii_table.ASCIITable;
import engine.controllers.ControllerMessage;
import engine.core.Agent;
import engine.core.ExceptionOutput;
import engine.core.RouteHandler;
import engine.views.AbstractView;
import engine.views.cui.Utilities.CUITextTools;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


public class RegisterView extends AbstractView
{

    @Override
    public void display()
    {
        Agent.commandInProgress();
        
        System.out.println("\n" + CUITextTools.underline(CUITextTools.changeColour("Step 1/3: Account details", CUITextTools.MAGENTA)) + "\n");
        Map<String, String> accDetails = getAccDetailsInput();
        
        
        System.out.println(CUITextTools.underline(CUITextTools.changeColour("Step 2/3: Medical contact", CUITextTools.MAGENTA)));
        Map<String, String> contactDetails = getMedicalContactInput();
        
        System.out.println(CUITextTools.underline(CUITextTools.changeColour("Step 3/3: Medical details", CUITextTools.MAGENTA)));
        Map<String, String> medicalDetails  =   getMedicalDetailsInput(); 
        
        
        ControllerMessage postData  =   new ControllerMessage().addAll(accDetails).addAll(contactDetails).addAll(medicalDetails);
        ResponseDataView response   =   (ResponseDataView) RouteHandler.go("postRegister", postData);
        
        System.out.println(response.getResponseMessage());
        
        if(response.getResponseStatus())
        {
           System.out.println("Redirecting in 5 seconds..");

            try { Thread.sleep(5000); } 
            catch (InterruptedException ex) 
            {
                System.out.println(ex.getMessage());
            }

            Agent.setView("getLogin");
        }
        
        else Agent.setView("getLogin");
        
        
        Agent.commandFinished();
    }
    
    public Map<String, String> getFormInput(List<String> fieldTitles, List<String> inputKeys, String[] headers)
    {
        Map<String, String> form    =   new LinkedHashMap<>();
        
        Thread inputThread  =   new Thread(() ->
        {
            boolean formInProgress  =   true;
            Scanner inputScan       =   new Scanner(System.in);
            
            while(formInProgress)
            {
                for(int inputIndex = 0; inputIndex < inputKeys.size(); inputIndex++)
                {
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
                        ExceptionOutput.output("Interrupted: " + ex.getMessage(), ExceptionOutput.OutputType.DEBUG);
                    }
                }
                
                formInProgress = !confirmForm(form, headers, inputScan);
            }
        });
        
        inputThread.start();
        
        try{ inputThread.join(); }   
        catch(InterruptedException e)
        {
            ExceptionOutput.output("Interrupted: " + e.getMessage(), ExceptionOutput.OutputType.DEBUG);
        }
        
        return form;
    }
    
    public boolean confirmForm(Map<String, String> form, String[] headers, Scanner scanner)
    {
        System.out.println("\n" + CUITextTools.changeColour("Please verify that the following details are correct before proceeding\n", CUITextTools.YELLOW) + "\n");
        
        String[][] data      =   new String[1][form.size()];
        data[0]              =   (String[]) form.values().toArray(new String[form.size()]);

        ASCIITable.getInstance().printTable(headers, data);

        System.out.println("\n" + CUITextTools.changeColour("Continue? [Y/N]", CUITextTools.GREEN) + "\n");
        String continueAnswer   =   scanner.nextLine();
        return continueAnswer.equalsIgnoreCase("y");
    }
    
    public Map<String, String> getAccDetailsInput()
    {
       List<String> fieldTitles =   new ArrayList<>();
       
       fieldTitles.add(createFormField("Username", "Your unique StudentCore account name, 6-16 alphanumeric characters"));
       fieldTitles.add(createFormField("Password", "Your StudentCore password, 6-18 characters"));
       fieldTitles.add(createFormField("First name", "Your legal first name"));
       fieldTitles.add(createFormField("Last name", "Your legel last name/surname"));
       fieldTitles.add(createFormField("Gender/Sex", "What is your gender/sex? [Male/Female]"));
       fieldTitles.add(createFormField("Birth date", "When were you born? yyyy-mm-dd"));
       fieldTitles.add(createFormField("Contact phone", "What is your contact phone number? 7-12 digits"));
       fieldTitles.add(createFormField("Contact email", "What is your contact email address, may be used for verification"));
       fieldTitles.add(createFormField("Ethnicity", "What is your recognized ethnicity?"));
       
       List<String> inputKeys   =   new ArrayList<>();
       
       inputKeys.add("registerUsername");
       inputKeys.add("registerPassword");
       inputKeys.add("registerFirstname");
       inputKeys.add("registerLastname");
       inputKeys.add("registerGender");
       inputKeys.add("registerBirth");
       inputKeys.add("registerPhone");
       inputKeys.add("registerEmail");
       inputKeys.add("registerEthnicity");
       
       String[] headers =   { "Username", "Password", "First name", "Last name", "Gender", "Birthdate", "Phone", "Email", "Ethnicity" };
       
       return getFormInput(fieldTitles, inputKeys, headers);
    }
    
    public Map<String, String> getMedicalContactInput()
    {
        List<String> fieldTitles    =   new ArrayList<>();
        fieldTitles.add(createFormField("Doctor first name", "Your doctor/GPs legal first name"));
        fieldTitles.add(createFormField("Doctor last name", "Your doctor/GPs legal last/surname"));
        fieldTitles.add(createFormField("Doctor contact phone", "Your doctor/GPs phone number"));
        fieldTitles.add(createFormField("Doctors email", "Your doctor/GPs email address"));
        
        List<String> inputKeys  =   new ArrayList<>();
        
        inputKeys.add("registerContactFirstname");
        inputKeys.add("registerContactLastname");
        inputKeys.add("registerContactPhone");
        inputKeys.add("registerContactEmail");
        
        String[] headers    =   { "Doctor firstname", "Doctor lastname", "Doctor phone", "Doctor email" };
        
        return getFormInput(fieldTitles, inputKeys, headers);
    }
    
    
    public Map<String, String> getMedicalDetailsInput()
    {
        List<String> fieldTitles    =   new ArrayList<>();
        
        fieldTitles.add(createFormField("Medical description", "Do you have any medical conditions? If no enter none"));
        
        List<String> inputKeys  =   new ArrayList<>();
        
        inputKeys.add("registerMedicalDescription");
        
        String[] headers    =   { "Medical description" };
        
        return getFormInput(fieldTitles, inputKeys, headers);
        
    } 
    
    
    public String createFormField(String title, String description)
    {
        String formField    =   "";
        formField += "\n" + CUITextTools.drawSubHeader(title, CUITextTools.PLAIN, CUITextTools.CYAN, "#") + "\n";
        formField += CUITextTools.changeColour(description, CUITextTools.YELLOW) + "\n";
        formField += CUITextTools.changeColour("Enter your " + title + ":\n", CUITextTools.GREEN);
        
        return formField;
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
