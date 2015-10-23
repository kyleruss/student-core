
package engine.views.gui;

import engine.controllers.ControllerMessage;
import engine.core.Agent;
import engine.core.ExceptionOutput;
import engine.core.RouteHandler;
import engine.views.GUIView;
import engine.views.ResponseDataView;
import engine.views.gui.layout.Layout;
import engine.views.gui.layout.Transition;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.border.Border;
import org.jdesktop.xswingx.PromptSupport;


public class RegisterView extends GUIView implements ActionListener
{
    private JPanel userFormPanel;
    private JPanel medicalFormPanel;
    private JPanel formPanel;
    private JPanel formWrapperPanel;
    private JPanel formControls;
    private JPanel registerPanel;
    private JPanel headerPanel;
    private JPanel controlWrapper;

    private List<String> forms;
    private int step;
    private final String FIRST_FORM     = "user";
    private final String SECOND_FORM    = "medical";
    
    private JButton nextStep;
    private JButton prevStep;
    private JLabel stepLabel;
    private JScrollPane registerScroll;
    private BufferedImage backgroundImage;
    
    //---------------------------------------
    //          STATUS TEXT
    //---------------------------------------
    private JPanel statusPanel;
    private JLabel statusIcon;
    private JLabel statusText;
    
    
    //----------------------------------------
    //           USER DETAILS
    //----------------------------------------
    private JTextField usernameField, passwordField, firstnameField, lastnameField;
    private JTextField phoneField, emailField, birthdateField;
    private JComboBox genderField, ethnicityField;
    
    //-----------------------------------------
    
    
    //----------------------------------------
    //          EMERGENCY/MEDICAL CONTACT
    //----------------------------------------
    private JTextField contactFirstname, contactLastname, contactPhone, contactEmail;
    private JTextField contactRelationship; 
    private JTextField medicalDescription;
    //----------------------------------------
    
    
    
    @Override
    protected void initComponents() 
    {
        panel               =   new JPanel()
        {
            @Override
            public void paintComponent(Graphics g)
            {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, null);
            }
        };
        
        formPanel           =   new JPanel(new CardLayout());
        registerPanel       =   new JPanel(new BorderLayout());
        formWrapperPanel    =   new JPanel(new BorderLayout());
        formControls        =   new JPanel();
        medicalFormPanel    =   new JPanel(new GridLayout(11, 1));
        userFormPanel       =   new JPanel(new GridLayout(17, 1));
        headerPanel         =   new JPanel(new GridLayout(2, 1));
        controlWrapper      =   new JPanel(new GridLayout(2, 1));
        
        forms               =   new ArrayList<>();
        step                =   0;
        forms.add(FIRST_FORM);
        forms.add(SECOND_FORM);
        
        registerPanel.setBackground(Color.WHITE);
        registerPanel.setPreferredSize(new Dimension(350, 450));
        formWrapperPanel.setPreferredSize(new Dimension(350, 330));
        formControls.setPreferredSize(new Dimension(350, 50));
        formPanel.setPreferredSize(new Dimension(250, 500));
        
        formWrapperPanel.setBackground(Color.WHITE);
        formPanel.setBackground(Color.WHITE);
        userFormPanel.setBackground(Color.WHITE);
        medicalFormPanel.setBackground(Color.WHITE);
        formControls.setBackground(Color.WHITE);
        headerPanel.setBackground(Color.WHITE);
        
        JLabel headLabel    =   new JLabel("Registration");
        stepLabel           =   new JLabel("Step 1 - Account details");
        headerPanel.add(headLabel);
        headerPanel.add(stepLabel);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 10));
        
        headLabel.setFont(helveticaThin.deriveFont(Font.BOLD, 16f));
        stepLabel.setFont(helveticaThin.deriveFont(14f));
        
        //CONTROLS
        prevStep    =   new JButton("Prev");
        nextStep    =   new JButton("Next");     
        prevStep.setEnabled(false);
        
        formControls.add(prevStep);
        formControls.add(nextStep);
        
        prevStep.addActionListener(this);
        nextStep.addActionListener(this);
        
        // USER COMPONENTS
        usernameField       =   new JTextField();
        passwordField       =   new JPasswordField();
        firstnameField      =   new JTextField();
        lastnameField       =   new JTextField();
        phoneField          =   new JTextField();
        emailField          =   new JTextField();
        genderField         =   new JComboBox();
        ethnicityField      =   new JComboBox();
        birthdateField      =   new JTextField();
        
        genderField.addItem("Male");
        genderField.addItem("Female");
        
        ethnicityField.addItem("European");
        ethnicityField.addItem("Maori");
        ethnicityField.addItem("Pacific");
        ethnicityField.addItem("Asian");
        ethnicityField.addItem("Middle Eastern/Latin American/African");
        ethnicityField.addItem("Other");
        
        
        usernameField.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(209, 209, 209)));
        passwordField.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(209, 209, 209)));
        firstnameField.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(209, 209, 209)));
        lastnameField.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(209, 209, 209)));
        phoneField.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(209, 209, 209)));
        emailField.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(209, 209, 209)));
        birthdateField.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(209, 209, 209)));
        
        PromptSupport.setPrompt(" Username", usernameField);
        PromptSupport.setPrompt(" Password", passwordField);
        PromptSupport.setPrompt(" First name", firstnameField);
        PromptSupport.setPrompt(" Last name", lastnameField);
        PromptSupport.setPrompt(" Telephone number", phoneField);
        PromptSupport.setPrompt(" Email address", emailField);
        PromptSupport.setPrompt(" Birthdate yyyy-mm-dd", birthdateField);
        
        userFormPanel.add(usernameField);
        userFormPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        userFormPanel.add(passwordField);
        userFormPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        userFormPanel.add(firstnameField);
        userFormPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        userFormPanel.add(lastnameField);
        userFormPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        userFormPanel.add(phoneField);
        userFormPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        userFormPanel.add(emailField);
        userFormPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        userFormPanel.add(birthdateField);
        userFormPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        userFormPanel.add(genderField);
        userFormPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        userFormPanel.add(ethnicityField);
        
        //MEDICAL/EMERGENCY CONTACT COMPONENTS
        contactFirstname    =   new JTextField();
        contactLastname     =   new JTextField();
        contactPhone        =   new JTextField();
        contactEmail        =   new JTextField();
        contactRelationship =   new JTextField();
        medicalDescription  =   new JTextField();
        
        contactFirstname.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(209, 209, 209)));
        contactLastname.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(209, 209, 209)));
        contactPhone.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(209, 209, 209)));
        contactEmail.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(209, 209, 209)));
        contactRelationship.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(209, 209, 209)));
        medicalDescription.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(209, 209, 209)));
        
        medicalFormPanel.add(contactFirstname);
        medicalFormPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        medicalFormPanel.add(contactLastname);
        medicalFormPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        medicalFormPanel.add(contactPhone);
        medicalFormPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        medicalFormPanel.add(contactEmail);
        medicalFormPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        medicalFormPanel.add(contactRelationship);
        medicalFormPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        medicalFormPanel.add(medicalDescription);
        
        PromptSupport.setPrompt(" Contact first name", contactFirstname);
        PromptSupport.setPrompt(" Contact last name", contactLastname);
        PromptSupport.setPrompt(" Telephone number", contactPhone);
        PromptSupport.setPrompt(" Email address", contactEmail);
        PromptSupport.setPrompt(" Contact relationship", contactRelationship);
        PromptSupport.setPrompt(" Enter medical information", medicalDescription);
        
        
        formPanel.add(userFormPanel, FIRST_FORM);
        formPanel.add(medicalFormPanel, SECOND_FORM);
        //formPanel.revalidate();
        
        
        // STATUS COMPONENTS
        statusPanel =   new JPanel();
        statusIcon  =   new JLabel();
        statusText  =   new JLabel();
        statusPanel.setBackground(Color.WHITE);
        statusPanel.add(statusIcon);
        statusPanel.add(statusText);
        
        formWrapperPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        formPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        registerScroll  =   new JScrollPane(formPanel);
        registerScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        formWrapperPanel.add(registerScroll, BorderLayout.CENTER);
        
        controlWrapper.add(statusPanel);
        controlWrapper.add(formControls);
        controlWrapper.setBackground(Color.WHITE);
        
        registerPanel.add(headerPanel, BorderLayout.NORTH);
        registerPanel.add(formWrapperPanel, BorderLayout.CENTER);
        registerPanel.add(controlWrapper, BorderLayout.SOUTH);
        
        panel.add(Box.createRigidArea(new Dimension(0, 500)));
        panel.add(registerPanel);
    }
    
    @Override
    protected void initListeners()
    {
        
    }
    
    private boolean validateField(JTextField field, String regex, int minLength, int maxLength, Border valid, Border invalid)
    {
        String text         =   field.getText();
        
        Pattern pattern     =   Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher     =   pattern.matcher(text);
        
        if(matcher.matches() && !(text.length() < minLength || text.length() > maxLength)) 
        {
            field.setBorder(valid);
            return true;
        }
        
        else
        {
            field.setBorder(invalid);
            return false;
        }
    }
   
    private boolean validateForm(int step)
    {  
        final Border validBorder                    =   BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(209, 209, 209));
        final Border invalidBorder                  =   BorderFactory.createMatteBorder(1, 1, 1, 1, Color.RED);
        final ArrayList<Boolean> validateIndexes    =   new ArrayList<>();
        
        switch(step)
        {
            case 0:
                validateIndexes.add(validateField(usernameField, "\\w*", 6, 18, validBorder, invalidBorder));
                validateIndexes.add(validateField(passwordField, "\\w*", 6, 25, validBorder, invalidBorder));
                validateIndexes.add(validateField(firstnameField, "[a-z]*", 3, 35, validBorder, invalidBorder));
                validateIndexes.add(validateField(lastnameField, "[a-z]*", 3, 35, validBorder, invalidBorder));
                validateIndexes.add(validateField(phoneField, "\\d*", 7, 12, validBorder, invalidBorder));
                validateIndexes.add(validateField(emailField, "\\w*@?\\w*\\.?\\w*", 5, 40, validBorder, invalidBorder));
                validateIndexes.add(validateField(birthdateField, "[0-9]{4}-[0-9]{2}-[0-9]{2}", 10, 10, validBorder, invalidBorder));
                break;
                
            case 1:
                validateIndexes.add(validateField(contactFirstname, "[a-z]*", 6, 18, validBorder, invalidBorder));
                validateIndexes.add(validateField(contactLastname, "[a-z]*", 6, 25, validBorder, invalidBorder));
                validateIndexes.add(validateField(contactPhone, "\\d*", 7, 12, validBorder, invalidBorder));
                validateIndexes.add(validateField(contactEmail, "\\w*@?\\w*\\.?\\w*", 5, 40, validBorder, invalidBorder));
                validateIndexes.add(validateField(contactRelationship, "[a-z]*", 5, 100, validBorder, invalidBorder));
                validateIndexes.add(validateField(medicalDescription, "\\w*", 5, 100, validBorder, invalidBorder));
                break;
        }
        
        return validateIndexes.contains(false);
    }
    
    private void nextStep()
    {
        if(validateForm(step)) 
        {
            System.out.println("invalid");
            showInvalidForm();
            return;
        }
        
        if(step + 1 < forms.size())
        {
            step++;
            CardLayout cLayout  =   (CardLayout) formPanel.getLayout();
            cLayout.show(formPanel, forms.get(step));
            registerScroll.getVerticalScrollBar().setValue(0);
            
            if(step == forms.size() - 1) 
                nextStep.setText("Finish");
           prevStep.setEnabled(true);
        }
        
        else finish();
    }
    
    private void prevStep()
    {
        if(step - 1 >= 0)
        {
            step--;
            System.out.println("PREV: " + forms.get(step));
            CardLayout cLayout  =   (CardLayout) formPanel.getLayout();
            cLayout.show(formPanel, forms.get(step));
            registerScroll.getVerticalScrollBar().setValue(0);
            
            if(step < forms.size())
                nextStep.setText("Next");
            
            nextStep.setEnabled(true);
        }
    }
    
    public Map<String, String> getAccDetails()
    {
        Map<String, String> accDetails      =   new HashMap<>();
        accDetails.put("registerUsername", usernameField.getText());
        accDetails.put("registerPassword", passwordField.getText());
        accDetails.put("registerFirstname", firstnameField.getText());
        accDetails.put("registerLastname", lastnameField.getText());
        accDetails.put("registerGender", "Male");
        accDetails.put("registerPhone", phoneField.getText());
        accDetails.put("registerEmail", emailField.getText());
        accDetails.put("registerBirth", birthdateField.getText());
        accDetails.put("registerEthnicity", ethnicityField.getSelectedItem().toString());
        accDetails.put("registerGender", genderField.getSelectedItem().toString());
        
        return accDetails;
    }
    
    
    public Map<String, String> getContactDetails()
    {
        Map<String, String> contactDetails  =   new HashMap<>();
        contactDetails.put("registerContactFirstname", contactFirstname.getText());
        contactDetails.put("registerContactLastname", contactLastname.getText());
        contactDetails.put("registerContactPhone", contactPhone.getText());
        contactDetails.put("registerContactEmail", contactEmail.getText());
        contactDetails.put("registerMedicalDescription", medicalDescription.getText());
        
        return contactDetails;
    }
    
    private void attemptLoginRedirect()
    {
        ControllerMessage postData   =   new ControllerMessage();
        postData.add("loginUsername", usernameField.getText());
        postData.add("loginPassword", passwordField.getText()); 

        ResponseDataView response   =   (ResponseDataView) RouteHandler.go("postLogin", postData); 
        
        if(response.getResponseStatus())
            Agent.setView("getHome");
        else
        {
            System.out.println("FAILED TO LOGIN: " + response.getRawResponseMessage());
            Agent.setView("getLogin");
        }
    }
    
    private void showInvalidForm()
    {
        statusIcon.setIcon(new ImageIcon(Layout.getImage("failicon.png")));
        statusText.setText("Invalid form, please check your fields");
        statusIcon.setVisible(true);
        statusText.setVisible(true);
        Timer invalidTimer  =   new Timer(1500, (ActionEvent e) ->
        {
            statusIcon.setVisible(false);
            statusText.setVisible(false);
        });
        
        invalidTimer.setRepeats(false);
        invalidTimer.start();
    }
    
    private void finish()
    {
        statusIcon.setIcon(Transition.getSmallSpinner());
        statusText.setText("Processing...");
        statusIcon.setVisible(true);
        statusText.setVisible(true);
        
        ControllerMessage postData  =   new ControllerMessage().addAll(getAccDetails()).addAll(getContactDetails());
        ResponseDataView response   =   (ResponseDataView) RouteHandler.go("postRegister", postData);
        
        Timer registerTimer =   new Timer(2000, (ActionEvent e) ->
        {
            if(response.getResponseStatus())
            {
                statusIcon.setIcon(new ImageIcon(Layout.getImage("successicon.png")));
                statusText.setText("Registration successful!");
                Timer redirectTimer =   new Timer(1500, (ActionEvent ev) ->
                {
                    Agent.setView("getLogin");//attemptLoginRedirect();
                });
                
                redirectTimer.setRepeats(false);
                redirectTimer.start();
                
            }

            else
            {
                statusIcon.setIcon(new ImageIcon(Layout.getImage("failicon.png")));
                statusText.setText(response.getResponseMessage());
            }
        });
        
        registerTimer.setRepeats(false);
        registerTimer.start();
    }

    public JPanel getUserFormPanel()
    {
        return userFormPanel;
    }

    public JPanel getMedicalFormPanel()
    {
        return medicalFormPanel;
    }

    public JTextField getUsernameField()
    {
        return usernameField;
    }

    public JTextField getPasswordField() 
    {
        return passwordField;
    }

    public JTextField getFirstnameField()
    {
        return firstnameField;
    }

    public JTextField getLastnameField()
    {
        return lastnameField;
    }

    public JTextField getPhoneField()
    {
        return phoneField;
    }

    public JTextField getEmailField() 
    {
        return emailField;
    }

    public JTextField getBirthdateField()
    {
        return birthdateField;
    }

    public JComboBox getGenderField() 
    {
        return genderField;
    }

    public JComboBox getEthnicityField() 
    {
        return ethnicityField;
    }

    public JTextField getContactFirstname() 
    {
        return contactFirstname;
    }

    public JTextField getContactLastname()
    {
        return contactLastname;
    }

    public JTextField getContactPhone() 
    {
        return contactPhone;
    }

    public JTextField getContactEmail()
    {
        return contactEmail;
    }

    public JTextField getContactRelationship()
    {
        return contactRelationship;
    }

    public JTextField getMedicalDescription()
    {
        return medicalDescription;
    }

    public void setUsernameField(JTextField usernameField)
    {
        this.usernameField = usernameField;
    }

    public void setPasswordField(JTextField passwordField)
    {
        this.passwordField = passwordField;
    }

    public void setFirstnameField(JTextField firstnameField)
    {
        this.firstnameField = firstnameField;
    }

    public void setLastnameField(JTextField lastnameField)
    {
        this.lastnameField = lastnameField;
    }

    public void setPhoneField(JTextField phoneField)
    {
        this.phoneField = phoneField;
    }

    public void setEmailField(JTextField emailField) 
    {
        this.emailField = emailField;
    }

    public void setBirthdateField(JTextField birthdateField)
    {
        this.birthdateField = birthdateField;
    }

    public void setGenderField(JComboBox genderField)
    {
        this.genderField = genderField;
    }

    public void setEthnicityField(JComboBox ethnicityField)
    {
        this.ethnicityField = ethnicityField;
    }

    public void setContactFirstname(JTextField contactFirstname) 
    {
        this.contactFirstname = contactFirstname;
    }

    public void setContactLastname(JTextField contactLastname)
    {
        this.contactLastname = contactLastname;
    }

    public void setContactPhone(JTextField contactPhone)
    {
        this.contactPhone = contactPhone;
    }

    public void setContactEmail(JTextField contactEmail)
    {
        this.contactEmail = contactEmail;
    }

    public void setContactRelationship(JTextField contactRelationship)
    {
        this.contactRelationship = contactRelationship;
    }

    public void setMedicalDescription(JTextField medicalDescription)
    {
        this.medicalDescription = medicalDescription;
    }

    public JPanel getFormPanel()
    {
        return formPanel;
    }

    public void setFormPanel(JPanel formPanel) 
    {
        this.formPanel = formPanel;
    }
    
    
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        Object src  =   e.getSource();
        
        if(src == nextStep)
            nextStep();
        
        else if(src == prevStep)
            prevStep();
    }

    @Override
    protected void initResources() 
    {
        try
        {
            backgroundImage =   ImageIO.read(new File(Layout.getImage("blurredbackground20.jpg")));
        }
        
        catch(IOException e)
        {
            ExceptionOutput.output("Failed to load resources: " + e.getMessage(), ExceptionOutput.OutputType.DEBUG);
        }
    }
    
    private JPanel createFormField(JTextField field, String description)
    {
        return null;
    }
    
}
