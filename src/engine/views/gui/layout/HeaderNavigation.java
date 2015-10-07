
package engine.views.gui.layout;

import engine.config.AppConfig;
import engine.config.ConfigFactory;
import engine.core.Agent;
import engine.core.authentication.Session;
import engine.models.NotificationModel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class HeaderNavigation extends JPanel implements ActionListener
{
    private final JPanel viewNavigationPanel;
    private final JPanel viewLocationPanel;
    private final JPanel userPanel;
    
    private final JButton prevViewButton;
    private final JButton nextViewButton;
    private final JButton refreshViewButton;
    private final JButton userProfileButton;
    private final JButton userNotificationsButton;
    private final JButton userLogoutButton;
    

    private final JTextField addressBar;
    private final JButton addressSearchButton;
    
    public HeaderNavigation()
    {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension
        (
            Window.getWindowDim().x,
            (int) (Window.getWindowDim().y * 0.10)
        ));
        
        
        
        viewNavigationPanel =   new JPanel(new GridLayout(1, 3));
        viewLocationPanel   =   new JPanel(new BorderLayout());
        userPanel           =   new JPanel(new GridLayout(1, 3));
        
        viewNavigationPanel.setPreferredSize(new Dimension(150, 25));
        viewLocationPanel.setPreferredSize(new Dimension(250, 40));
        userPanel.setPreferredSize(new Dimension(150, 25));
        
        //View navigation buttons
        prevViewButton      =   new JButton();
        nextViewButton      =   new JButton();
        refreshViewButton   =   new JButton();
        
        String prevButtonImage  =   (String) ConfigFactory.get(ConfigFactory.APP_CONFIG, AppConfig.RESOURCE_DIR) + "prevbutton.png";
        String nextButtonImage  =   (String) ConfigFactory.get(ConfigFactory.APP_CONFIG, AppConfig.RESOURCE_DIR) + "nextbutton.png";
        String refButtonImage   =   (String) ConfigFactory.get(ConfigFactory.APP_CONFIG, AppConfig.RESOURCE_DIR) + "refresh.png";
        
        prevViewButton.setIcon(new ImageIcon(prevButtonImage));
        nextViewButton.setIcon(new ImageIcon(nextButtonImage));
        refreshViewButton.setIcon(new ImageIcon(refButtonImage));
        
        Layout.makeTransparent(prevViewButton);
        Layout.makeTransparent(nextViewButton);
        Layout.makeTransparent(refreshViewButton);
        
        prevViewButton.addActionListener(this);
        nextViewButton.addActionListener(this);
        refreshViewButton.addActionListener(this);
        
        viewNavigationPanel.add(prevViewButton);
        viewNavigationPanel.add(nextViewButton);
        viewNavigationPanel.add(refreshViewButton);
        
        //View location components
        JPanel innerAPanel  =   new JPanel(new BorderLayout());
        addressBar          =   new JTextField();
        addressSearchButton =   new JButton();
        addressSearchButton.addActionListener(this);
        
        String searchButtonImage =  (String) ConfigFactory.get(ConfigFactory.APP_CONFIG, AppConfig.RESOURCE_DIR) + "gobutton.png";
        addressSearchButton.setIcon(new ImageIcon(searchButtonImage));
        Layout.makeTransparent(addressSearchButton);
        addressSearchButton.setPreferredSize(new Dimension(45, 35));
        
        JPanel panel2   =   new JPanel();
        addressBar.setPreferredSize(new Dimension(300, 35));
        panel2.add(addressBar);
        viewLocationPanel.add(panel2, BorderLayout.CENTER);
        viewLocationPanel.add(addressSearchButton, BorderLayout.EAST);
        
        
        innerAPanel.add(Box.createRigidArea(new Dimension(40, 0)), BorderLayout.WEST);
        innerAPanel.add(viewLocationPanel, BorderLayout.CENTER);
        innerAPanel.add(Box.createRigidArea(new Dimension(40, 0)), BorderLayout.EAST);
        
        //User components
        userProfileButton       =   new JButton();
        userNotificationsButton =   new JButton();
        userLogoutButton        =   new JButton();
        
        String profileButtonImage       =   (String) ConfigFactory.get(ConfigFactory.APP_CONFIG, AppConfig.RESOURCE_DIR) + "profilebutton.png";
        String notificationButtonImage  =   (String) ConfigFactory.get(ConfigFactory.APP_CONFIG, AppConfig.RESOURCE_DIR) + "nonotificationsbutton.png";
        String logoutButtonImage        =   (String) ConfigFactory.get(ConfigFactory.APP_CONFIG, AppConfig.RESOURCE_DIR) + "logoutbutton.png";
        
        userProfileButton.setIcon(new ImageIcon(profileButtonImage));
        userNotificationsButton.setIcon(new ImageIcon(notificationButtonImage));
        userLogoutButton.setIcon(new ImageIcon(logoutButtonImage));
        
        Layout.makeTransparent(userProfileButton);
        Layout.makeTransparent(userNotificationsButton);
        Layout.makeTransparent(userLogoutButton);
        
        userProfileButton.addActionListener(this);
        userNotificationsButton.addActionListener(this);
        userLogoutButton.addActionListener(this);
        
        userPanel.add(userProfileButton);
        userPanel.add(userNotificationsButton);
        userPanel.add(userLogoutButton);
        
        //App settings
        add(viewNavigationPanel, BorderLayout.WEST);
        add(innerAPanel, BorderLayout.CENTER);
        add(userPanel, BorderLayout.EAST);
        
        disablePrevButton();
        disableNextButton();
        disableUserControls();
    }
    
    public void setViewAddress(String location)
    {
        addressBar.setText(location);
    }
    
    public void changeViews()
    {
        String address  =   addressBar.getText();
        Agent.setView(address);
    }
    
    
    public void enablePrevButton()
    {
        prevViewButton.setEnabled(true);
    }
    
    public void enableNextButton()
    {
        nextViewButton.setEnabled(true);
    }
    
    public void disablePrevButton()
    {
        prevViewButton.setEnabled(false);
    }
    
    public void disableNextButton()
    {
        nextViewButton.setEnabled(false);
    }
    
    public void enableUserControls()
    {
        userProfileButton.setEnabled(true);
        userNotificationsButton.setEnabled(true);
        userLogoutButton.setEnabled(true);
    }
    
    public void disableUserControls()
    {
        userProfileButton.setEnabled(false);
        userNotificationsButton.setEnabled(false);
        userLogoutButton.setEnabled(false);
    }
    
    public void updateUserPanel()
    {
        if(Agent.getActiveSession() != null)
        {
            enableUserControls();
            Session session         =   Agent.getActiveSession();
            String username         =   session.getUser().get("username").getNonLiteralValue().toString();
            int numNotifications;
            try
            {
                numNotifications    =   NotificationModel.getNumUnreadNotifications(username);
            }
            
            catch(SQLException e)
            {
                numNotifications = 0;
            }
            
            if(numNotifications > 0)
                userNotificationsButton.setIcon(new ImageIcon(Layout.getImage("hasnotificationsbutton.png")));
            else
                userNotificationsButton.setIcon(new ImageIcon(Layout.getImage("nonotificationsbutton.png")));
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        Object src  =   e.getSource();
        
        if(src == prevViewButton)
            Agent.setPrevView();
        
        else if(src == nextViewButton)
            Agent.setNextView();
        
        else if(src == refreshViewButton)
            Agent.refreshView();
        
        else if(src == addressSearchButton)
            changeViews();
        
    }
}
