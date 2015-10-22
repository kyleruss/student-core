
package engine.views.gui.admin;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import engine.controllers.ControllerMessage;
import engine.core.Agent;
import engine.core.ExceptionOutput;
import engine.core.RouteHandler;
import engine.models.AdminAnnouncementsModel;
import engine.models.Role;
import engine.views.GUIView;
import engine.views.ResponseDataView;
import engine.views.gui.admin.modules.RolesView;
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
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;


public class AdminControlPanelView extends GUIView implements ActionListener
{
    protected BufferedImage backgroundImage;
    private BufferedImage classesMenuImage;
    private BufferedImage usersMenuImage;
    private BufferedImage deptMenuImage;
    private BufferedImage announceMenuImage;
    private BufferedImage rolesMenuImage;
    private BufferedImage assignRoleImage;
    
    private JPanel adminPanel;
    private JPanel leftPanel;
    private JPanel rightPanel;
    private JPanel adminControls;
    private JPanel adminPaneView;
    
    private final String CLASS_VIEW     =   "classes_v";
    private final String USERS_VIEW     =   "users_v";
    private final String DEPT_VIEW      =   "dept_v";
    private final String ANNOUNCE_VIEW  =   "annouce_v";  
    private final String ROLES_VIEW     =   "roles_v";
    
    private AdminAnnouncementView announcementsView;
    
    private JPanel classesView;
    private JTable usersTable;
    
    private JPanel usersView;
    
    
    private JPanel departmentView;
    private JPanel departmentControls;
    private JButton addDeptButton, removeDeptButton;
    private JButton editDeptButton, assignDeptHeadButton;
    private JTable deptTable;
    private DefaultTableModel deptModel;
    private JLabel deptStatusLabel;
    
    private JPanel rolesView;
    private JPanel roleControls;
    private JButton addRoleButton, removeRoleButton;
    private JButton editRoleButton, assignRoleButton;
    private JTable rolesTable;
    private DefaultTableModel rolesModel;
    private JLabel roleStatusLabel;
    
    private JButton showUsersButton;
    private JButton showClassesButton;
    private JButton showDepartmentButton;
    private JButton showAnnouncementsButton;
    private JButton showRolesButton;
    
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
        adminControls   =   new JPanel(new GridLayout(5, 1));
        adminPaneView   =   new JPanel(new CardLayout());
        
        adminPanel.setPreferredSize(new Dimension(600, 400));
        leftPanel.setPreferredSize(new Dimension(150, adminPanel.getPreferredSize().height));
        adminControls.setPreferredSize(new Dimension(leftPanel.getPreferredSize().width, 300));
        
        adminPanel.setBackground(Color.WHITE);
        leftPanel.setBackground(new Color(50, 50, 62));
        rightPanel.setBackground(Color.WHITE);
        adminControls.setBackground(new Color(50, 50, 62));
        panel.setBackground(Color.WHITE);
        
        leftPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 2, Color.LIGHT_GRAY));
        

        initClassesView();
        initDepartmentsView();
        initUsersView();
        initRolesView();
        
        announcementsView   =   new AdminAnnouncementView();
        adminPaneView.add(classesView, CLASS_VIEW);
        adminPaneView.add(departmentView, DEPT_VIEW);
        adminPaneView.add(usersView, USERS_VIEW);
        adminPaneView.add(announcementsView.getAnnouncementViewPanel(), ANNOUNCE_VIEW);
        adminPaneView.add(rolesView, ROLES_VIEW);
        
        showClassesButton       =   new JButton("Classes");
        showDepartmentButton    =   new JButton("Departments");
        showUsersButton         =   new JButton("Users");
        showAnnouncementsButton =   new JButton("Notices");
        showRolesButton         =   new JButton("Roles");
        
        showClassesButton.setIcon(new ImageIcon(classesMenuImage));
        showDepartmentButton.setIcon(new ImageIcon(deptMenuImage));
        showUsersButton.setIcon(new ImageIcon(usersMenuImage));
        showAnnouncementsButton.setIcon(new ImageIcon(announceMenuImage));
        showRolesButton.setIcon(new ImageIcon(rolesMenuImage));
        
        showClassesButton.setForeground(Color.WHITE);
        showDepartmentButton.setForeground(Color.WHITE);
        showUsersButton.setForeground(Color.WHITE);
        showAnnouncementsButton.setForeground(Color.WHITE);
        showRolesButton.setForeground(Color.WHITE);
        showClassesButton.setHorizontalAlignment(SwingConstants.LEFT);
        showDepartmentButton.setHorizontalAlignment(SwingConstants.LEFT);
        showUsersButton.setHorizontalAlignment(SwingConstants.LEFT);
        showAnnouncementsButton.setHorizontalAlignment(SwingConstants.LEFT);
        showRolesButton.setHorizontalAlignment(SwingConstants.LEFT);
        
        adminControls.add(showAnnouncementsButton);
        adminControls.add(showClassesButton);
        adminControls.add(showDepartmentButton);
        adminControls.add(showUsersButton);
        adminControls.add(showRolesButton);
        
        leftPanel.add(adminControls);
        JScrollPane adminPaneScroller   =   new JScrollPane(adminPaneView);
        adminPaneScroller.setBorder(null);
        rightPanel.add(adminPaneScroller);
        
        adminPanel.add(leftPanel, BorderLayout.WEST);
        adminPanel.add(rightPanel, BorderLayout.CENTER);
        
        panel.add(Box.createRigidArea(new Dimension(panel.getPreferredSize().width, 500)));
        panel.add(adminPanel);
        
        showAdminView(ANNOUNCE_VIEW);
    }

    private void initDepartmentsView()
    {
        departmentView          =   new JPanel(new BorderLayout());
        departmentControls      =   new JPanel();
        deptModel               =   new DefaultTableModel();
        deptTable               =   new JTable(deptModel);
        addDeptButton           =   new JButton("Add");
        removeDeptButton        =   new JButton("Remove");
        editDeptButton          =   new JButton("Edit");
        assignDeptHeadButton    =   new JButton("Assign Dept. head");
        deptStatusLabel         =   new JLabel();
        JPanel header           =   new JPanel(new GridLayout(2, 1));
        JPanel tableWrapper     =   new JPanel();   
        JPanel statusWrapper    =   new JPanel();
        
        
        addDeptButton.setIcon(new ImageIcon(addSmallImage));
        removeDeptButton.setIcon(new ImageIcon(removeSmallImage));
        editDeptButton.setIcon(new ImageIcon(editSmallImage));
        assignDeptHeadButton.setIcon(new ImageIcon(assignRoleImage));
        
        departmentControls.add(addDeptButton);
        departmentControls.add(removeDeptButton);
        departmentControls.add(editDeptButton);
        departmentControls.add(assignDeptHeadButton);
        
        deptStatusLabel.setHorizontalAlignment(JLabel.CENTER);
        statusWrapper.add(deptStatusLabel);
        header.add(departmentControls);
        header.add(statusWrapper);
        
        deptModel.addColumn("ID");
        deptModel.addColumn("Name");
        deptModel.addColumn("Description");
        deptModel.addColumn("Head");
        
        DefaultTableCellRenderer roleRenderer   =   new DefaultTableCellRenderer();
        roleRenderer.setHorizontalAlignment(JLabel.CENTER);
        for(int i = 0; i < deptTable.getColumnCount(); i++)
            deptTable.getColumnModel().getColumn(i).setCellRenderer(roleRenderer);
        
        JScrollPane scroller =  new JScrollPane(deptTable);
        tableWrapper.add(scroller);
        tableWrapper.setPreferredSize(new Dimension(390, 280));
        scroller.setPreferredSize(new Dimension(390, 280));
        header.setPreferredSize(new Dimension(1, 100));
        
        departmentControls.setBackground(Color.WHITE);
        departmentView.setBackground(Color.WHITE);
        deptTable.setBackground(Color.WHITE);
        scroller.setBackground(Color.WHITE);
        header.setBackground(Color.WHITE);
        statusWrapper.setBackground(Color.WHITE);
        tableWrapper.setBackground(Color.WHITE);
        
        departmentView.add(header, BorderLayout.NORTH);
        departmentView.add(tableWrapper, BorderLayout.CENTER);
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
    
    private void initRolesView()
    {
        rolesView               =   new RolesView().getPanel();
    }
    
    @Override
    protected void initResources() 
    {
        try
        {
            backgroundImage     =   ImageIO.read(new File(Layout.getImage("background2.jpg")));
            announceMenuImage   =   ImageIO.read(new File(Layout.getImage("notes_icon.png")));
            classesMenuImage    =   ImageIO.read(new File(Layout.getImage("books_icon.png")));
            deptMenuImage       =   ImageIO.read(new File(Layout.getImage("department_large_icon.png")));
            usersMenuImage      =   ImageIO.read(new File(Layout.getImage("users_icon.png")));
            rolesMenuImage      =   ImageIO.read(new File(Layout.getImage("roles_icon.png")));
            assignRoleImage     =   ImageIO.read(new File(Layout.getImage("assign_role_icon.png")));
        }
        
        catch(IOException e)
        {
            ExceptionOutput.output("Failed to load resources: " + e.getMessage(), ExceptionOutput.OutputType.MESSAGE);
        }
    }
    
    private void showResponseLabel(JLabel label, String message, boolean result)
    {
        label.setText(message);
        if(result)
            label.setIcon(new ImageIcon(successImage));
        else
            label.setIcon(new ImageIcon(failImage));
        
        label.setVisible(true);
        Timer responseTimer =   new Timer(2000, (ActionEvent e)->
        {
            label.setVisible(false);
        });
        
        responseTimer.setRepeats(false);
        responseTimer.start();
    }

    @Override
    protected void initListeners()
    {
        showClassesButton.addActionListener(this);
        showUsersButton.addActionListener(this);
        showDepartmentButton.addActionListener(this);
        showAnnouncementsButton.addActionListener(this);
        showRolesButton.addActionListener(this);
      /*  addRoleButton.addActionListener(this);
        removeRoleButton.addActionListener(this);
        editRoleButton.addActionListener(this);
        assignRoleButton.addActionListener(this);*/
        addDeptButton.addActionListener(this);
        removeDeptButton.addActionListener(this);
        editDeptButton.addActionListener(this);
        assignDeptHeadButton.addActionListener(this);
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
        
        else if(src == showRolesButton)
            showAdminView(ROLES_VIEW);
        
      /*  else if(src == removeRoleButton)
            removeRole();
        
        else if(src == addRoleButton)
            addRole();
        
        else if(src == editRoleButton)
            editRole();
        
        else if(src == assignRoleButton)
            assignRole(); */
    }
    
    private void showAdminView(String viewName)
    {
        CardLayout cLayout  =   (CardLayout) adminPaneView.getLayout();
        cLayout.show(adminPaneView, viewName);
    }
    
    
    
    private class AdminAnnouncementView extends AnnouncementView
    {
        @Override
        protected void showAddAnnouncement() 
        {
            announcementsView.modifyPanel.clear();
            announcementsView.modifyPanel.header.setText("Add announcement");
            announcementsView.modifyPanel.header.setIcon(new ImageIcon(addSmallImage));
            announcementsView.showAnnouncementView(AnnouncementView.MODIFY_VIEW);
            announcementsView.context = AnnouncementView.ManageContext.ADDING;
        }

        @Override
        protected void showRemoveAnnouncement() 
        {
            if(announcementList.getSelectedIndex() == -1)
                JOptionPane.showMessageDialog(null, "Please select an announcement in remove");
            
            int option      =   JOptionPane.showConfirmDialog(null, "Are you sure you want to remove this announcement?", "Remove announcement", JOptionPane.YES_NO_OPTION);
            if(option == JOptionPane.YES_OPTION)
            {
                submitRemoveAnnouncement();
            }
        }

        @Override
        protected void showEditAnnouncement() 
        {
            if(announcementList.getSelectedIndex() == -1)
                JOptionPane.showMessageDialog(null, "Please select an announcement to edit");
            
            JsonObject value = (JsonObject) announcementList.getSelectedValue();
            String title    =   value.get("TITLE").getAsString();
            String content  =   value.get("CONTENT").getAsString();
            announcementsView.modifyPanel.header.setText("Edit announcement");
            announcementsView.modifyPanel.header.setIcon(new ImageIcon(editSmallImage));
            announcementsView.modifyPanel.fill(title, content);
            announcementsView.showAnnouncementView(AnnouncementView.MODIFY_VIEW);
            announcementsView.context = AnnouncementView.ManageContext.EDITING;
        }

        @Override
        protected JsonArray getData()
        {
            return AdminAnnouncementsModel.getAllAnnouncements();
        }

        @Override
        protected void submitAddAnnouncement() 
        {
            String title    =   announcementsView.modifyPanel.getTitle();
            String content  =   announcementsView.modifyPanel.getContent();
            
            if(title.equals("") || content.equals(""))
                JOptionPane.showMessageDialog(null, "Please fill in all the fields");
            else
            {
                String username =   Agent.getActiveSession().getUser().get("username").getNonLiteralValue().toString();
                announcementsView.showProcessing();
                ControllerMessage postData  =   new ControllerMessage();
                postData.add("announcePoster", username);
                postData.add("announceContent", content);
                postData.add("announceTitle", title);
                
                ResponseDataView response   =   (ResponseDataView) RouteHandler.go("postAddAdminAnnouncement", postData);
                Timer responseTimer =   new Timer(2000, (ActionEvent e)->
                {
                    announcementsView.showStatusLabel(response.getRawResponseMessage(), response.getResponseStatus());
                    announcementsView.back();
                });
                
                responseTimer.setRepeats(false);
                responseTimer.start();
                
                if(response.getResponseStatus())
                    announcementsView.initData();
            }
        }

        @Override
        protected void submitRemoveAnnouncement()  
        {
            JsonObject value            =   (JsonObject) announcementList.getSelectedValue();
            int announceID              =   value.get("ID").getAsInt();
            ControllerMessage postData  =   new ControllerMessage();
            postData.add("announceID", announceID);
            ResponseDataView response   =   (ResponseDataView) RouteHandler.go("postRemoveAdminAnnouncement", postData);
            announcementsView.showStatusLabel(response.getRawResponseMessage(), response.getResponseStatus());
            
            if(response.getResponseStatus())
            {
                announcementModel.remove(announcementList.getSelectedIndex());
                if(announcementModel.size() > 0)
                    announcementList.setSelectedIndex(0);
            }
        }

        @Override
        protected void submitEditAnnouncement() 
        {
            String title    =   announcementsView.modifyPanel.getTitle();
            String content  =   announcementsView.modifyPanel.getContent();
            int id          =   announcementsView.getSelectedValue().get("ID").getAsInt();
            
            if(title.equals("") || content.equals(""))
                JOptionPane.showMessageDialog(null, "Please fill in all the fields");
            else
            {
                announcementsView.showProcessing();
                ControllerMessage postData  =   new ControllerMessage();
                postData.add("announceID", id);
                postData.add("announceContent", content);
                postData.add("announceTitle", title);
                
                ResponseDataView response   =   (ResponseDataView) RouteHandler.go("postEditAdminAnnouncement", postData);
                Timer responseTimer =   new Timer(2000, (ActionEvent e)->
                {
                    announcementsView.showStatusLabel(response.getRawResponseMessage(), response.getResponseStatus());
                    announcementsView.back();
                });
                
                responseTimer.setRepeats(false);
                responseTimer.start();
                
                if(response.getResponseStatus())
                    announcementsView.initData();
            }
        }
        
    }
    
    public static void main(String[] args)
    {
        AdminControlPanelView view  =   new AdminControlPanelView();
    }
}
