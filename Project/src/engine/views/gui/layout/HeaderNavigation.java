//====================================
//	Kyle Russell
//	StudentCore
//	HeaderNavigation
//====================================

package engine.views.gui.layout;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import engine.config.AppConfig;
import engine.core.Agent;
import engine.core.authentication.Session;
import engine.models.NotificationModel;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

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
    private BufferedImage notificationImage;
    private BufferedImage backButtonImage;
    private BufferedImage notificationReadImage;
    private boolean notificationModalOpen;
    

    private final JTextField addressBar;
    private final JButton addressSearchButton;
    private int unreadNotifications;
    private NotificationUpdater notificationUpdater;
    
    public HeaderNavigation()
    {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension
        (
            Window.getWindowDim().x,
            (int) (Window.getWindowDim().y * 0.10)
        ));
        
        initResources();
        unreadNotifications =   0;
        viewNavigationPanel =   new JPanel(new GridLayout(1, 3));
        viewLocationPanel   =   new JPanel(new BorderLayout());
        userPanel           =   new JPanel(new GridLayout(1, 3));
        notificationUpdater =   new NotificationUpdater();
        
        viewNavigationPanel.setPreferredSize(new Dimension(150, 25));
        viewLocationPanel.setPreferredSize(new Dimension(250, 40));
        userPanel.setPreferredSize(new Dimension(150, 25));
        
        //View navigation buttons
        prevViewButton      =   new JButton();
        nextViewButton      =   new JButton();
        refreshViewButton   =   new JButton();
        
        String prevButtonImage  =    Layout.getImage("prevbutton.png");
        String nextButtonImage  =    Layout.getImage("nextbutton.png");
        String refButtonImage   =    Layout.getImage("refresh.png");
        
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
        
        String searchButtonImage =  Layout.getImage("gobutton.png");
        addressSearchButton.setIcon(new ImageIcon(searchButtonImage));
        Layout.makeTransparent(addressSearchButton);
        addressSearchButton.setPreferredSize(new Dimension(45, 35));
        
        JPanel searchBarWrapper   =   new JPanel();
        addressBar.setPreferredSize(new Dimension(300, 50));
        addressBar.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        searchBarWrapper.add(addressBar);
        viewLocationPanel.add(searchBarWrapper, BorderLayout.CENTER);
        viewLocationPanel.add(addressSearchButton, BorderLayout.EAST);
        
        
        innerAPanel.add(Box.createRigidArea(new Dimension(40, 0)), BorderLayout.WEST);
        innerAPanel.add(viewLocationPanel, BorderLayout.CENTER);
        innerAPanel.add(Box.createRigidArea(new Dimension(40, 0)), BorderLayout.EAST);
        
        //User components
        notificationModalOpen   =   false;
        userProfileButton       =   new JButton();
        userNotificationsButton =   new JButton();
        userLogoutButton        =   new JButton();
        
        userProfileButton.addActionListener(this);
        userNotificationsButton.addActionListener(this);
        userLogoutButton.addActionListener(this);
        
        String profileButtonImage       =    Layout.getImage("profilebutton.png");
        String notificationButtonImage  =    Layout.getImage("nonotificationsbutton.png");
        String logoutButtonImage        =    Layout.getImage("logoutbutton.png");
        
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
        notificationUpdater.start();
    }
    
    private void initResources()
    {
        try
        {
            notificationImage       =   ImageIO.read(new File(Layout.getImage("notification_icon.png")));
            backButtonImage         =   ImageIO.read(new File(Layout.getImage("back_icon.png")));
            notificationReadImage   =   ImageIO.read(new File(Layout.getImage("notification_icon_read.png")));
        }
        
        catch(IOException e)
        {
            JOptionPane.showMessageDialog(null, "Failed to load header resources");
        }
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
            numNotifications    =   NotificationModel.getNumUnreadNotifications(username);
            
            
            if(numNotifications > 0)
                userNotificationsButton.setIcon(new ImageIcon(Layout.getImage("hasnotificationsbutton.png")));
            else
                userNotificationsButton.setIcon(new ImageIcon(Layout.getImage("nonotificationsbutton.png")));
        }
    }
    
    private class NotificationUpdater extends Thread
    {
        @Override
        public void run()
        {
            while(true)
            {
                try
                {
                    sleep(AppConfig.NOTIFICATION_TIME);
                    updateUserPanel();
                }
                
                catch(InterruptedException e) {}
            }
        }
    }
    
    private void checkUnreadNotifications(JsonArray results)
    {
        int tempUnreadNotifications     =   0;
        for(int i = 1; i < results.size(); i++)
        {
            JsonObject current  =   results.get(i).getAsJsonObject();
            if(current.get("UNREAD").getAsBoolean())
                tempUnreadNotifications++;
        }

        unreadNotifications =   tempUnreadNotifications;
    }
    
    public void showNotificationWindow()
    {
        if(notificationModalOpen) return;
        else notificationModalOpen   = true;
        
        SwingUtilities.invokeLater(() ->
        {
            final String TABLE_VIEW         =   "table_v";
            final String SINGLE_VIEW        =   "single_v";

            JsonArray notifications         =   NotificationModel.getUserNotifications(Agent.getActiveSession().getUser()
                                                .get("username").getNonLiteralValue().toString());
            int numNotifications            =   (notifications.size() > 0)? notifications.size() - 1 : 0;
            checkUnreadNotifications(notifications);
            

            JPanel notificationPanel        =   new JPanel(new BorderLayout());
            JButton backButton              =   new JButton();
            JTable notificationTable        =   new JTable(new DefaultTableModel());
            JPanel tableView                =   new JPanel();
            JPanel singleView               =   new JPanel();
            JTextArea singleViewContent     =   new JTextArea(10, 10);
            JPanel notificationDetailsPanel =   new JPanel();
            JPanel notificationViewPanel    =   new JPanel(new CardLayout());   
            JLabel notificationInfo         =   new JLabel("You have " + numNotifications + " notifications");
            notificationPanel.setPreferredSize(new Dimension(500, 300));

            backButton.setOpaque(false);
            backButton.setContentAreaFilled(true);
            backButton.setBorderPainted(false);
            backButton.setIcon(new ImageIcon(backButtonImage));

            notificationDetailsPanel.add(notificationInfo);
            JPanel singleContentWrapper =   new JPanel();
            singleViewContent.setMaximumSize(new Dimension(350, 150));
            singleViewContent.setPreferredSize(new Dimension(350, 150));
            singleViewContent.setLineWrap(true);
            singleViewContent.setOpaque(false);
            singleViewContent.setEditable(false);
            singleViewContent.setFocusable(false);
            singleViewContent.setBackground(Color.WHITE);
            singleViewContent.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            singleContentWrapper.setBorder(BorderFactory.createTitledBorder("Notification content"));
            singleContentWrapper.setMaximumSize(new Dimension(350, 150));

            singleContentWrapper.add(singleViewContent);
            singleView.add(backButton);
            singleView.add(new JLabel("Go back to notification table"));
            singleView.add(Box.createRigidArea(new Dimension(notificationPanel.getPreferredSize().width, 15)));
            singleView.add(singleContentWrapper);

            tableView.setBackground(Color.WHITE);
            singleView.setBackground(Color.WHITE);
            singleContentWrapper.setBackground(Color.WHITE);
            notificationTable.setBackground(Color.WHITE);
            notificationInfo.setForeground(Color.WHITE);

            tableView.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
            notificationTable.setPreferredScrollableViewportSize(new Dimension(400, 200));
            DefaultTableModel model             =   (DefaultTableModel) notificationTable.getModel();
            DefaultTableCellRenderer renderer   =   new DefaultTableCellRenderer();
            renderer.setHorizontalAlignment(JLabel.CENTER);
            model.addColumn("Unread");
            model.addColumn("ID");
            model.addColumn("Content");
            model.addColumn("Date sent");
            notificationTable.getColumnModel().getColumn(1).setCellRenderer(renderer);
            notificationTable.getColumnModel().getColumn(0).setCellRenderer(new NotificationCellRenderer());

            notificationTable.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> 
            {
                if(!e.getValueIsAdjusting() && notificationTable.getSelectionModel().getMinSelectionIndex() >= 0)
                {
                    int row =   notificationTable.getSelectedRow();

                    String content  =   (String) model.getValueAt(row, 2);
                    int id          =   (int) model.getValueAt(row, 1);
                    singleViewContent.setText(content);
                    CardLayout cLayout  = (CardLayout) notificationViewPanel.getLayout();
                    cLayout.show(notificationViewPanel, SINGLE_VIEW);
                    
                    NotificationModel.readNotificationMessage(id);
                    if((boolean) model.getValueAt(row, 0)) unreadNotifications--;
                    
                    model.setValueAt(false, row, 0);
                    model.fireTableCellUpdated(row, 0);
                }
            });

            backButton.addActionListener((ActionEvent e) ->
            {
                CardLayout cLayout  = (CardLayout) notificationViewPanel.getLayout();
                cLayout.show(notificationViewPanel, TABLE_VIEW);

                notificationTable.getSelectionModel().clearSelection();
            });

            for(int i = 1; i < notifications.size(); i++)
            {
                JsonObject jObj         =   notifications.get(i).getAsJsonObject();
                int notificationID      =   jObj.get("ID").getAsInt();
                boolean unread          =   jObj.get("UNREAD").getAsBoolean();
                String content          =   jObj.get("CONTENT").getAsString();
                String dateSent         =   jObj.get("SENT_DATE").getAsString();

                model.addRow(new Object[] { unread, notificationID, content, dateSent } );
            }

            tableView.add(new JScrollPane(notificationTable));
            notificationViewPanel.add(tableView, TABLE_VIEW);
            notificationViewPanel.add(singleView, SINGLE_VIEW);


            notificationPanel.setBackground(Color.WHITE);
            notificationDetailsPanel.setBackground(Color.BLACK);


            CardLayout cLayout = (CardLayout)notificationViewPanel.getLayout();
            cLayout.show(notificationViewPanel, TABLE_VIEW);

            notificationPanel.add(notificationDetailsPanel, BorderLayout.NORTH);
            notificationPanel.add(notificationViewPanel, BorderLayout.CENTER);

            JFrame frame    =   Agent.getWindow();
            JDialog modal   =   new JDialog(frame);
            modal.getContentPane().add(notificationPanel);
            modal.setLocation(frame.getWidth() - notificationPanel.getSize().width, frame.getHeight() / 2 - notificationPanel.getSize().height);
            modal.setTitle("Notifications");
            modal.pack();
            modal.setResizable(false);
            modal.addWindowListener(new WindowAdapter()
            {
                @Override
                public void windowClosing(WindowEvent e)
                {
                    notificationModalOpen = false;
                    if(unreadNotifications <= 0)
                        userNotificationsButton.setIcon(new ImageIcon(Layout.getImage("nonotificationsbutton.png")));
                    else
                        userNotificationsButton.setIcon(new ImageIcon(Layout.getImage("hasnotificationsbutton.png")));
                }
            });
            
            modal.setVisible(true);
        });
    }

    
    private class NotificationCellRenderer extends DefaultTableCellRenderer
    {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) 
        {
            boolean unread      =   (boolean) value;
            JLabel component    =   new JLabel();
            if(unread) component.setIcon(new ImageIcon(notificationImage));
            else component.setIcon(new ImageIcon(notificationReadImage));
            component.setHorizontalAlignment(JLabel.CENTER);
            return component;
        }
    }
    
    private void logoutUser()
    {
        Agent.sessionLogout();
        disableUserControls();
        disableNextButton();
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
        
        else if(src == userLogoutButton)
            logoutUser();
        
        else if(src == userProfileButton)
            Agent.setView("getHome");
        
        else if(src == userNotificationsButton)
            showNotificationWindow();
    }
}
