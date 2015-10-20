
package engine.views.gui;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import engine.controllers.ControllerMessage;
import engine.core.ExceptionOutput;
import engine.models.AdminAnnouncementsModel;
import engine.views.GUIView;
import engine.views.gui.layout.Layout;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
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
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListCellRenderer;
import javax.swing.table.DefaultTableModel;


public class AdminControlPanelView extends GUIView implements ActionListener
{
    private BufferedImage backgroundImage;
    
    private JPanel adminPanel;
    private JPanel leftPanel;
    private JPanel rightPanel;
    private JPanel adminControls;
    private JPanel adminPaneView;
    
    private final String CLASS_VIEW     =   "classes_v";
    private final String USERS_VIEW     =   "users_v";
    private final String DEPT_VIEW      =   "dept_v";
    private final String ANNOUNCE_VIEW  =   "annouce_v";  
    
    private JPanel announcementsView;
    private JList announcementList;
    private DefaultListModel announcementModel;
    
    private JPanel classesView;
    private JTable usersTable;
    
    private JPanel usersView;
    private JPanel departmentView;
    
    private JButton showUsersButton;
    private JButton showClassesButton;
    private JButton showDepartmentButton;
    private JButton showAnnouncementsButton;
    
    public AdminControlPanelView()
    {
        super();
    }
    
    public AdminControlPanelView(ControllerMessage data)
    {
        super(data);
    }

    @Override
    protected void initComponents() 
    {
        panel   =   new JPanel()
        {
            @Override
            public void paintComponent(Graphics g)
            {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
            }
        };
        
        adminPanel      =   new JPanel(new BorderLayout());
        leftPanel       =   new JPanel();
        rightPanel      =   new JPanel();
        adminControls   =   new JPanel(new GridLayout(4, 1));
        adminPaneView   =   new JPanel(new CardLayout());
        
        adminPanel.setPreferredSize(new Dimension(600, 400));
        leftPanel.setPreferredSize(new Dimension(150, adminPanel.getPreferredSize().height));
        adminControls.setPreferredSize(new Dimension(leftPanel.getPreferredSize().width, 250));
        
        adminPanel.setBackground(Color.WHITE);
        leftPanel.setBackground(new Color(50, 50, 62));
        rightPanel.setBackground(Color.WHITE);
        adminControls.setBackground(new Color(50, 50, 62));
        panel.setBackground(Color.WHITE);
        
        leftPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 2, Color.LIGHT_GRAY));
        

        initClassesView();
        initDepartmentsView();
        initUsersView();
        initAnnouncementsView();
        
        adminPaneView.add(classesView, CLASS_VIEW);
        adminPaneView.add(departmentView, DEPT_VIEW);
        adminPaneView.add(usersView, USERS_VIEW);
        adminPaneView.add(announcementsView, ANNOUNCE_VIEW);
        
        
        showClassesButton       =   new JButton("Classes");
        showDepartmentButton    =   new JButton("Departments");
        showUsersButton         =   new JButton("Users");
        showAnnouncementsButton =   new JButton("Announcements");
        showClassesButton.setForeground(Color.WHITE);
        showDepartmentButton.setForeground(Color.WHITE);
        showUsersButton.setForeground(Color.WHITE);
        showAnnouncementsButton.setForeground(Color.WHITE);
        
        
        adminControls.add(showAnnouncementsButton);
        adminControls.add(showClassesButton);
        adminControls.add(showDepartmentButton);
        adminControls.add(showUsersButton);
        
        leftPanel.add(adminControls);
        rightPanel.add(adminPaneView);
        
        adminPanel.add(leftPanel, BorderLayout.WEST);
        adminPanel.add(rightPanel, BorderLayout.CENTER);
        
        panel.add(Box.createRigidArea(new Dimension(panel.getPreferredSize().width, 500)));
        panel.add(adminPanel);
        
        showAdminView(ANNOUNCE_VIEW);
    }

    private void initDepartmentsView()
    {
        departmentView  =   new JPanel();
        departmentView.setBackground(Color.WHITE);
    }
    
    private void initClassesView()
    {
        classesView         =   new JPanel();
        classesView.setBackground(Color.WHITE);
    }
    
    private void initUsersView()
    {
        usersView   =   new JPanel();
        usersView.setBackground(Color.WHITE);
        
        usersTable  =   new JTable(new DefaultTableModel());
        usersView.add(new JScrollPane(usersTable));
    }
    
    private void initAnnouncementsView()
    {
        announcementsView   =   new JPanel();
        announcementsView.setBackground(Color.WHITE);

        announcementModel   =   new DefaultListModel();
        announcementList    =   new JList(announcementModel);
        announcementList.setCellRenderer(new AnnouncementCellRenderer());
        
        JsonArray announcements =   AdminAnnouncementsModel.getAllAnnouncements();
        System.out.println(announcements);
        
        if(announcements.size() > 0) 
        {
            announcementsView.add(announcementList);
            for(int i = 1; i < announcements.size(); i++)
                announcementModel.addElement(announcements.get(i).getAsJsonObject());
        }
        
       
        announcementsView.add(new JScrollPane(announcementList));
        
    }
    
    @Override
    protected void initResources() 
    {
        try
        {
            backgroundImage =   ImageIO.read(new File(Layout.getImage("background2.jpg")));
        }
        
        catch(IOException e)
        {
            ExceptionOutput.output("Failed to load resources: " + e.getMessage(), ExceptionOutput.OutputType.MESSAGE);
        }
    }

    @Override
    protected void initListeners()
    {
        showClassesButton.addActionListener(this);
        showUsersButton.addActionListener(this);
        showDepartmentButton.addActionListener(this);
        showAnnouncementsButton.addActionListener(this);
    }
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        Object src  =   e.getSource();
        
        if(src == showClassesButton)
            showAdminView(CLASS_VIEW);
        
        else if(src == showUsersButton)
            showAdminView(USERS_VIEW);
        
        else if(src == showDepartmentButton)
            showAdminView(DEPT_VIEW);
        
        else if(src == showAnnouncementsButton)
            showAdminView(ANNOUNCE_VIEW);
    }
    
    private void showAdminView(String viewName)
    {
        CardLayout cLayout  =   (CardLayout) adminPaneView.getLayout();
        cLayout.show(adminPaneView, viewName);
    }
    
    private class AnnouncementCellRenderer implements ListCellRenderer
    {
        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) 
        {
            JsonObject jObj             =   (JsonObject) value;
            JPanel announcementPanel    =   new JPanel(new BorderLayout());
            JPanel announceHeaderPanel  =   new JPanel();
            JPanel contentWrapper       =   new JPanel();
            JLabel announcerLabel       =   new JLabel(jObj.get("ANNOUNCER").getAsString());
            JLabel announceDateLabel    =   new JLabel(jObj.get("ANNOUNCE_DATE").getAsString());
            JTextArea content           =   new JTextArea(jObj.get("CONTENT").getAsString());
    
            announcementPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
            announcementPanel.setBackground(Color.WHITE);
            announceHeaderPanel.setBackground(Color.WHITE);
            content.setBackground(Color.WHITE);
            contentWrapper.setBackground(Color.WHITE);
            
            announceHeaderPanel.add(announcerLabel);
            announceHeaderPanel.add(announceDateLabel);
            
            JScrollPane contentScrollPane   =   new JScrollPane(content);
            contentScrollPane.setBorder(null);
            contentWrapper.add(contentScrollPane);
            announcementPanel.add(announceHeaderPanel, BorderLayout.NORTH);
            announcementPanel.add(contentWrapper, BorderLayout.CENTER);
            return announcementPanel;
        }
    }
    
}
