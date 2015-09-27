
package engine.views.gui.layout;

import engine.core.Agent;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
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
            (int) (Window.getWindowDim().y * 0.1)
        ));
        
        viewNavigationPanel =   new JPanel(new GridLayout(1, 3));
        viewLocationPanel   =   new JPanel(new BorderLayout());
        userPanel           =   new JPanel(new GridLayout(1, 3));
        
        viewNavigationPanel.setPreferredSize(new Dimension(200, 25));
        viewLocationPanel.setPreferredSize(new Dimension(250, 40));
        
        //View navigation buttons
        prevViewButton      =   new JButton("Prev");
        nextViewButton      =   new JButton("Next");
        refreshViewButton   =   new JButton("Ref");
        
        prevViewButton.addActionListener(this);
        nextViewButton.addActionListener(this);
        refreshViewButton.addActionListener(this);
        
        viewNavigationPanel.add(prevViewButton);
        viewNavigationPanel.add(nextViewButton);
        viewNavigationPanel.add(refreshViewButton);
        
        //View location components
        JPanel innerAPanel  =   new JPanel(new BorderLayout());
        addressBar          =   new JTextField();
        addressSearchButton =   new JButton("GO");
        addressSearchButton.addActionListener(this);
        
        addressBar.setPreferredSize(new Dimension(350, 35));
        viewLocationPanel.add(addressBar, BorderLayout.CENTER);
        viewLocationPanel.add(addressSearchButton, BorderLayout.EAST);
        
        
        innerAPanel.add(Box.createRigidArea(new Dimension(40, 0)), BorderLayout.WEST);
        innerAPanel.add(viewLocationPanel, BorderLayout.CENTER);
        innerAPanel.add(Box.createRigidArea(new Dimension(40, 0)), BorderLayout.EAST);
        
        //User components
        userProfileButton       =   new JButton("1");
        userNotificationsButton =   new JButton("3");
        userLogoutButton        =   new JButton("4");
        
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
    }
    
    public void setViewAddress(String location)
    {
        System.out.println("sdasd");
        addressBar.setText(location);
    }
    
    public void changeViews()
    {
        
        String address  =   addressBar.getText();
        System.out.println("change views: " + address);
        Agent.setView(address);
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
