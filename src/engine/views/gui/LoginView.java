package engine.views.gui;

import engine.core.ExceptionOutput;
import engine.views.GUIView;
import engine.views.gui.layout.Layout;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.MatteBorder;
import org.jdesktop.xswingx.BuddySupport;
import org.jdesktop.xswingx.PromptSupport;

public class LoginView extends GUIView
{
    private BufferedImage backgroundImage;
    private ImageIcon loginButtonImage, registerButtonImage, usernameLabelImage, passLabelImage;
    private JPanel loginPanel;
    private JButton loginButton, registerButton;
    private JTextField usernameField, passwordField;
    
    public LoginView()
    {
       super();
    }
    
    @Override
    protected void initResources()
    {
        try
        {
             backgroundImage     =   ImageIO.read(new File(Layout.getImage("loginbackground.jpg")));
             loginButtonImage    =   new ImageIcon(Layout.getImage("loginbutton.png"));
             registerButtonImage =   new ImageIcon(Layout.getImage("registerbutton.png"));
             usernameLabelImage  =   new ImageIcon(Layout.getImage("user.png"));
             passLabelImage      =   new ImageIcon(Layout.getImage("key.png"));
        }

        catch(IOException e)
        {
            ExceptionOutput.output("[Error] " + e.getMessage(), ExceptionOutput.OutputType.MESSAGE);
        }

        panel   =   new JPanel()
        {
             @Override
             public void paintComponent(Graphics g)
             {
                 if(backgroundImage != null)
                     g.drawImage(backgroundImage, 0, 0, null);
             }
        };
    }

    @Override
    protected void initComponents() 
    {
       
        panel.setBackground(Color.GREEN);
        
        loginPanel  =   new JPanel(new BorderLayout());
        loginPanel.setPreferredSize(new Dimension(350, 300));
        loginPanel.setBackground(Color.WHITE);
        
        JPanel loginFields          =   new JPanel(new GridLayout(3, 1));
        JPanel loginFieldsWrapper   =   new JPanel(new BorderLayout());
        
        loginFieldsWrapper.add(Box.createRigidArea(new Dimension(0, 30)), BorderLayout.NORTH);
        loginFieldsWrapper.add(Box.createRigidArea(new Dimension(0, 30)), BorderLayout.SOUTH);
        loginFieldsWrapper.add(Box.createRigidArea(new Dimension(30, 0)), BorderLayout.EAST);
        loginFieldsWrapper.add(Box.createRigidArea(new Dimension(30, 0)), BorderLayout.WEST);
        
        loginFields.setBackground(Color.WHITE);
        loginFieldsWrapper.setBackground(Color.WHITE);
        
        usernameField               =   new JTextField();
        passwordField               =   new JTextField();
        PromptSupport.setPrompt(" Username", usernameField);
        PromptSupport.setPrompt(" Password", passwordField);
        usernameField.setBorder(new MatteBorder(0, 0, 1, 0, Color.GRAY));
        passwordField.setBorder(new MatteBorder(0, 0, 1, 0, Color.GRAY));
        
        JLabel userLabel = new JLabel();
        JLabel passLabel = new JLabel();
         
        userLabel.setIcon(usernameLabelImage);
        BuddySupport.addLeft(userLabel, usernameField);
        
        passLabel.setIcon(passLabelImage);
        BuddySupport.addLeft(passLabel, passwordField);
        
        JCheckBox rememberBox  =   new JCheckBox("Remember password?");
        rememberBox.setHorizontalTextPosition(SwingConstants.LEFT);
        
        loginFields.add(usernameField);
        loginFields.add(passwordField);
        loginFields.add(rememberBox);
        loginFieldsWrapper.add(loginFields, BorderLayout.CENTER);
        
        
        JPanel loginPanelButtons    =   new JPanel(new GridLayout(2, 1));
        JPanel loginPanelButtonWrap =   new JPanel();
        
        loginButton                 =   new JButton();
        registerButton              =   new JButton();
        loginPanelButtons.add(loginButton);
        loginPanelButtons.add(registerButton);
        
        loginPanelButtons.setPreferredSize(new Dimension(183, 90));
        loginPanelButtonWrap.setBackground(Color.WHITE);
        loginPanelButtons.setBackground(Color.WHITE);
        
        loginButton.setIcon(loginButtonImage);
        registerButton.setIcon(registerButtonImage);
        Layout.makeTransparent(loginButton);
        Layout.makeTransparent(registerButton);
        
        loginPanelButtonWrap.add(loginPanelButtons);
        
        loginPanel.add(loginFieldsWrapper, BorderLayout.CENTER);
        loginPanel.add(loginPanelButtonWrap, BorderLayout.SOUTH);
        
        
        panel.add(Box.createRigidArea(new Dimension(0, 350)));
        panel.add(loginPanel);
        
        
    }
    
   
    
    
}