
package engine.views.gui;

import engine.core.ExceptionOutput;
import engine.views.GUIView;
import engine.views.gui.layout.Layout;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import org.jdesktop.xswingx.PromptSupport;


public class RegisterView extends GUIView implements ActionListener
{
    private JPanel userFormPanel;
    private JPanel medicalFormPanel;
    private JPanel formPanel;
    private JPanel formWrapperPanel;
    private JPanel formControls;
    private JPanel confirmPanel;
    private JPanel registerPanel;
    private JPanel headerPanel;
    
    private final String FIRST_FORM     = "user";
    private final String SECOND_FORM    = "medical";
    private final String THIRD_FORM     = "confirm";
    
    private JButton nextStep;
    private JButton prevStep;
    private JLabel stepLabel;
    
    private BufferedImage backgroundImage;
    
    //----------------------------------------
    //           USER DETAILS
    //----------------------------------------
    private JTextField usernameField, passwordField, firstnameField, lastnameField;
    private JTextField phoneField, emailField;
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
        medicalFormPanel    =   new JPanel(new GridLayout(6, 1));
        userFormPanel       =   new JPanel(new GridLayout(15, 1));
        confirmPanel        =   new JPanel();
        headerPanel         =   new JPanel(new GridLayout(2, 1));
        
        registerPanel.setBackground(Color.WHITE);
        registerPanel.setPreferredSize(new Dimension(350, 450));
        formWrapperPanel.setPreferredSize(new Dimension(350, 330));
        formControls.setPreferredSize(new Dimension(350, 50));
        formPanel.setPreferredSize(new Dimension(250, 500));
        
        formWrapperPanel.setBackground(Color.WHITE);
        formPanel.setBackground(Color.WHITE);
        userFormPanel.setBackground(Color.WHITE);
        medicalFormPanel.setBackground(Color.WHITE);
        confirmPanel.setBackground(Color.WHITE);
        formControls.setBackground(Color.WHITE);
        headerPanel.setBackground(Color.WHITE);
        
        stepLabel   =   new JLabel("Step 1 - Account details");
        headerPanel.add(new JLabel("Registration"));
        headerPanel.add(stepLabel);
        
        //CONTROLS
        prevStep    =   new JButton("Prev");
        nextStep    =   new JButton("Next");
        
        prevStep.addActionListener(this);
        nextStep.addActionListener(this);
        
        formControls.add(prevStep);
        formControls.add(nextStep);
        
        // USER COMPONENTS
        usernameField       =   new JTextField();
        passwordField       =   new JPasswordField();
        firstnameField      =   new JTextField();
        lastnameField       =   new JTextField();
        phoneField          =   new JTextField();
        emailField          =   new JTextField();
        genderField         =   new JComboBox();
        ethnicityField      =   new JComboBox();
        
        usernameField.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY));
        passwordField.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY));
        firstnameField.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY));
        lastnameField.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY));
        phoneField.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY));
        emailField.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY));
        
        PromptSupport.setPrompt("Username", usernameField);
        PromptSupport.setPrompt("Password", passwordField);
        PromptSupport.setPrompt("First name", firstnameField);
        PromptSupport.setPrompt("Last name", lastnameField);
        PromptSupport.setPrompt("Telephone number", phoneField);
        PromptSupport.setPrompt("Email address", emailField);
        
        
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
        
        medicalFormPanel.add(contactFirstname);
        medicalFormPanel.add(contactLastname);
        medicalFormPanel.add(contactPhone);
        medicalFormPanel.add(contactEmail);
        medicalFormPanel.add(contactRelationship);
        medicalFormPanel.add(medicalDescription);
        
        formPanel.add(userFormPanel, FIRST_FORM);
        formPanel.add(medicalFormPanel, SECOND_FORM);
        formPanel.add(confirmPanel, THIRD_FORM);
        
        formWrapperPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        formPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        JScrollPane scrollPane  =   new JScrollPane(formPanel);
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        formWrapperPanel.add(scrollPane, BorderLayout.CENTER);
        
        registerPanel.add(headerPanel, BorderLayout.NORTH);
        registerPanel.add(formWrapperPanel, BorderLayout.CENTER);
        registerPanel.add(formControls, BorderLayout.SOUTH);
        
        panel.add(Box.createRigidArea(new Dimension(0, 400)));
        panel.add(registerPanel);
    }
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        Object src  =   e.getSource();
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
