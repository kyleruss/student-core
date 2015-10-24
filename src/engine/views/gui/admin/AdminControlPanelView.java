
package engine.views.gui.admin;

import engine.controllers.ControllerMessage;
import engine.core.ExceptionOutput;
import engine.views.GUIView;
import engine.views.gui.admin.modules.ClassesView;
import engine.views.gui.admin.modules.DeptView;
import engine.views.gui.admin.modules.NoticesView;
import engine.views.gui.admin.modules.RolesView;
import engine.views.gui.admin.modules.UsersView;
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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
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
    
    private NoticesView announcementsView;
    
    private JPanel classesView;
    private JPanel usersView;
    
    private JPanel departmentView;
    
    private JPanel rolesView;
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
        
        departmentView  =   new DeptView().getPanel();
        classesView     =   new ClassesView().getPanel();
        usersView       =   new UsersView().getPanel();
        rolesView       =   new RolesView().getPanel();
        
        announcementsView   =   new NoticesView();
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

    @Override
    protected void initListeners()
    {
        showClassesButton.addActionListener(this);
        showUsersButton.addActionListener(this);
        showDepartmentButton.addActionListener(this);
        showAnnouncementsButton.addActionListener(this);
        showRolesButton.addActionListener(this);
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
    }
    
    private void showAdminView(String viewName)
    {
        CardLayout cLayout  =   (CardLayout) adminPaneView.getLayout();
        cLayout.show(adminPaneView, viewName);
    }
}
