package engine.views.gui;

import engine.controllers.ControllerMessage;
import engine.core.Agent;
import engine.core.ExceptionOutput;
import engine.core.RouteHandler;
import engine.core.authentication.Credentials;
import engine.core.authentication.StoredCredentials;
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
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.MatteBorder;
import org.jdesktop.xswingx.BuddySupport;
import org.jdesktop.xswingx.PromptSupport;

public class LoginView extends GUIView implements ActionListener, KeyListener
{
    
    private final String PROCESSING_KEY =   "proc";
    private final String BUTTON_KEY     =   "button";
    
    private BufferedImage backgroundImage;
    private ImageIcon loginButtonImage, registerButtonImage, usernameLabelImage, passLabelImage;
    private JPanel loginPanel, loginButtonsPanel;
    private JButton loginButton, registerButton;
    private JTextField usernameField, passwordField;
    private JLabel headerTitle, headerDescription;
    private JPanel headerPanel;
    private JCheckBox rememberCredentials;
    
    public LoginView()
    {
       super();
    }
    
    public LoginView(ControllerMessage data)
    {
        super(data);
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
    }
    
    @Override
    protected void initListeners()
    {   
        passwordField.addKeyListener(this);
        usernameField.addKeyListener(this);
        
        loginButton.addActionListener(this);
        registerButton.addActionListener(this);
    }
    
    @Override
    protected void initComponents() 
    {
        panel   =   new JPanel()
        {
             @Override
             public void paintComponent(Graphics g)
             {
                 if(backgroundImage != null)
                     g.drawImage(backgroundImage, 0, 0, null);
             }
        };
        
        panel.setBackground(Color.GREEN);
        
        loginPanel  =   new JPanel(new BorderLayout());
        loginPanel.setPreferredSize(new Dimension(350, 350));
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
        passwordField               =   new JPasswordField();
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
        
        rememberCredentials  =   new JCheckBox("Remember password?");
        rememberCredentials.setHorizontalTextPosition(SwingConstants.LEFT);
        
        loginFields.add(usernameField);
        loginFields.add(passwordField);
        loginFields.add(rememberCredentials);
        loginFieldsWrapper.add(loginFields, BorderLayout.CENTER);
        
        
        JPanel loginPanelButtons    =   new JPanel(new GridLayout(2, 1));
        JPanel loginPanelButtonWrap =   new JPanel();
        JPanel loginProcessPanel    =   new JPanel(new BorderLayout());
        loginButtonsPanel           =   new JPanel(new CardLayout());
        loginButtonsPanel.setBackground(Color.WHITE);
        
        loginButton                 =   new JButton();
        registerButton              =   new JButton();
        JLabel processText          =   new JLabel("Processing...");
        JLabel processSpinner       =   new JLabel(Transition.getSmallSpinner());
        
        loginProcessPanel.setBackground(Color.WHITE);
        loginProcessPanel.add(processSpinner, BorderLayout.WEST);
        loginProcessPanel.add(processText, BorderLayout.CENTER);
        
        loginButtonsPanel.add(loginButton, BUTTON_KEY);
        loginButtonsPanel.add(loginProcessPanel, PROCESSING_KEY);
        
        loginPanelButtons.add(loginButtonsPanel);
        loginPanelButtons.add(registerButton);
        
        loginPanelButtons.setPreferredSize(new Dimension(183, 90));
        loginPanelButtonWrap.setBackground(Color.WHITE);
        loginPanelButtons.setBackground(Color.WHITE);
        
        loginButton.setIcon(loginButtonImage);
        registerButton.setIcon(registerButtonImage);
        Layout.makeTransparent(loginButton);
        Layout.makeTransparent(registerButton);
        
        loginPanelButtonWrap.add(loginPanelButtons);
        
        headerPanel         =   new JPanel(new GridLayout(2, 1));
        headerTitle         =   new JLabel("Login");
        headerDescription   =   new JLabel("Enter your StudentCore credentials");
        headerTitle.setFont(helveticaThin.deriveFont(Font.BOLD, 18f));
        headerDescription.setFont(helveticaThin.deriveFont(16f));
        headerPanel.add(headerTitle);
        headerPanel.add(headerDescription);
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 30, 5, 30));
        
        loginPanel.add(headerPanel, BorderLayout.NORTH);
        loginPanel.add(loginFieldsWrapper, BorderLayout.CENTER);
        loginPanel.add(loginPanelButtonWrap, BorderLayout.SOUTH);
        
        
        panel.setBorder(BorderFactory.createEmptyBorder(50, 0, 0, 0));//.add(Box.createRigidArea(new Dimension(0, 350)));
        panel.add(loginPanel);
        
        fillStoredCredentials();
    }
    
    private void fillStoredCredentials()
    {
        StoredCredentials credentials   =   Agent.getStoredCredentials();
        String lastAccessed             =   credentials.getLastAccessed();
        
        if(lastAccessed == null) return;
        
        Credentials user                =   credentials.getUserCredentials(lastAccessed);
        if(user == null) return;
        
        String username =   user.getUsername();
        String passHash =   user.getPassword();
        
        usernameField.setText(username);
        passwordField.setText(passHash);
        rememberCredentials.setSelected(true);
    }
    
    
    private void attemptLogin()
    {
        String username =   usernameField.getText();
        String password =   passwordField.getText();
        
        if(username.equals("") || password.equals(""))
            ExceptionOutput.output("Please enter both your username and password", ExceptionOutput.OutputType.MESSAGE);
        else
        {
            
            CardLayout cLayout  =   (CardLayout) loginButtonsPanel.getLayout();
            cLayout.show(loginButtonsPanel, PROCESSING_KEY);

            Timer loginTimer    =   new Timer(100, (ActionEvent e) -> 
            {
                ControllerMessage postData   =   new ControllerMessage();
                postData.add("loginUsername", username);
                postData.add("loginPassword", password); 
                postData.add("storeCredentials", rememberCredentials.isSelected());

                ResponseDataView response   =   (ResponseDataView) RouteHandler.go("postLogin", postData); 
                cLayout.show(loginButtonsPanel, BUTTON_KEY);
                
                if(!response.getResponseStatus())
                     ExceptionOutput.output(response.getRawResponseMessage(), ExceptionOutput.OutputType.MESSAGE);
                else 
                {
                    Agent.getWindow().getAppLayout().getHeadNav().enableUserControls();
                    Agent.getWindow().getAppLayout().getHeadNav().enablePrevButton();
                    Agent.setView("getHome");
                }
            });

            loginTimer.setRepeats(false);
            loginTimer.start(); 
        }
    }
    
    private void goRegister()
    {
        Agent.setView("getRegister");
    }
   
    @Override
    public void actionPerformed(ActionEvent e)
    {
        Object src = e.getSource();
        
        if(src == loginButton)
            attemptLogin();
        
        else if(src == registerButton)
            goRegister();
    }
    
    @Override
    public void keyPressed(KeyEvent e)
    {
        int keyCode =   e.getKeyCode();
        if(keyCode == KeyEvent.VK_ENTER)
        {
            attemptLogin();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}
}