package engine.views.gui;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import engine.config.AppConfig;
import engine.controllers.ControllerMessage;
import engine.core.Agent;
import engine.core.database.Column;
import engine.models.AdminAnnouncementsModel;
import engine.models.DepartmentModel;
import engine.models.DeptAnnouncementsModel;
import engine.views.GUIView;
import engine.views.gui.admin.modules.AnnouncementView;
import engine.views.gui.admin.modules.NoticesView;
import engine.views.gui.layout.Layout;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
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
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class DepartmentView extends GUIView implements ActionListener
{
    private final String NOTICE_VIEW    =   "notice_v";
    private final String USERS_VIEW     =   "in_dept_v";
    private final String CONTACT_VIEW   =   "dept_head_v";
    private BufferedImage backgroundImage;
    private DepartmentAnnouncementsView announcementView;
    private DepartmentModel deptData;
    private JPanel deptPanel;
    private JPanel deptControls;
    private JPanel deptViewPanel;
    private JPanel deptUsersView;
    private JPanel contactView;
    private JPanel deptHeader;
    private JLabel deptLabel;
    private JButton showNoticesButton, showUsersButton, showContactButton;
    
    
    public DepartmentView()
    {
        super();
    }
    
    public DepartmentView(ControllerMessage data)
    {
        super(data);
    }

    @Override
    protected void initComponents() 
    {
        panel               =   new JPanel()
        {
            @Override
            public void paintComponent(Graphics g)
            {
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
            }
        };
        announcementView    =   new DepartmentAnnouncementsView();
        deptPanel           =   new JPanel(new BorderLayout());
        deptControls        =   new JPanel();
        deptViewPanel       =   new JPanel(new CardLayout());
        deptUsersView       =   new JPanel();
        contactView         =   new JPanel();
        deptHeader          =   new JPanel(new GridLayout(2, 1));
        deptLabel           =   new JLabel("HR Department");
        showNoticesButton   =   new JButton("Notices");
        showUsersButton     =   new JButton("Users");
        showContactButton   =   new JButton("Contact");
        
        
        JPanel userWrapper      =   new JPanel();
        JPanel contactWrapper   =   new JPanel();
        JPanel noticeWrapper    =   new JPanel(new BorderLayout());
        
        userWrapper.add(deptUsersView);
        contactWrapper.add(contactView);
        noticeWrapper.add(announcementView.getAnnouncementViewPanel());
        
        initData();
        deptLabel.setHorizontalAlignment(JLabel.CENTER);
        deptControls.add(showNoticesButton);
        deptControls.add(showUsersButton);
        deptControls.add(showContactButton);
        
        deptHeader.add(deptLabel);
        deptHeader.add(deptControls);
        
        deptHeader.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        deptPanel.setPreferredSize(new Dimension(500, 450));
        deptHeader.setPreferredSize(new Dimension(1, 75));
        deptPanel.add(deptHeader, BorderLayout.NORTH);
        deptPanel.add(deptViewPanel, BorderLayout.CENTER);
        
        deptViewPanel.add(userWrapper, USERS_VIEW);
        deptViewPanel.add(contactWrapper, CONTACT_VIEW);
        deptViewPanel.add(noticeWrapper, NOTICE_VIEW);
        
        userWrapper.setBackground(Color.WHITE);
        contactWrapper.setBackground(Color.WHITE);
        noticeWrapper.setBackground(Color.WHITE);
        showNoticesButton.setForeground(Color.WHITE);
        showUsersButton.setForeground(Color.WHITE);
        showContactButton.setForeground(Color.WHITE);
        deptLabel.setForeground(Color.WHITE);
        deptPanel.setBackground(Color.WHITE);
        deptControls.setBackground(new Color(50, 50, 62));
        deptHeader.setBackground(new Color(50, 50, 62));
        deptViewPanel.setBackground(Color.WHITE);
        deptUsersView.setBackground(Color.WHITE);
        contactView.setBackground(Color.WHITE);
        panel.setBackground(Color.WHITE);
        
        panel.add(Box.createRigidArea(new Dimension(AppConfig.WINDOW_WIDTH, 50)));
        panel.add(deptPanel);
        
        showDeptView(NOTICE_VIEW);
    }
    
    
    private void initData()
    {
        Column dept_col =   Agent.getActiveSession().getUser().get("DEPT_ID");
        if(dept_col == null) 
        {
            deptData    =   null;
            return;
        }

        else
        {
            deptData    =   new DepartmentModel(dept_col.getNonLiteralValue());
        }

        if(deptData == null || !deptData.exists())
        {
            deptData = null;
            return;
        }

        deptLabel.setText(deptData.get("name").getNonLiteralValue().toString() + " department");
    }
    

    @Override
    protected void initResources() 
    {
        try
        {
            backgroundImage =   ImageIO.read(new File(Layout.getImage("blurredbackground30.jpg")));
        }
        
        catch(IOException e)
        {
            JOptionPane.showMessageDialog(null, "Failed to load resources: " + e.getMessage());
        }
    }

    @Override
    protected void initListeners() 
    {
        showContactButton.addActionListener(this);
        showNoticesButton.addActionListener(this);
        showUsersButton.addActionListener(this);
    }
    
    private void showDeptView(String viewName)
    {
        CardLayout cLayout  =   (CardLayout) deptViewPanel.getLayout();
        cLayout.show(deptViewPanel, viewName);
    }

    @Override
    public void actionPerformed(ActionEvent e) 
    {
        Object src  =   e.getSource();
        
        if(src == showContactButton)
            showDeptView(CONTACT_VIEW);
        
        else if(src == showNoticesButton)
            showDeptView(NOTICE_VIEW);
        
        else if(src == showUsersButton)
            showDeptView(USERS_VIEW);
    }
    
    private class DepartmentAnnouncementsView extends NoticesView
    {
        public DepartmentAnnouncementsView()
        {
            super();
            announcementCode    =   "DEPT";
        }
        
        @Override
        protected void initComponents()
        {
            super.initComponents();
            announcementControls.setLayout(new FlowLayout());
            announcementScroller.setPreferredSize(new Dimension(390, 250));
        }
        
        private ControllerMessage addDeptToPost(ControllerMessage postData)
        {
            if(postData == null) return null;
            else
            {
                if(deptData == null) return null;
                else
                {
                    System.out.println("DEPT HERE: " + deptData.get("id").getNonLiteralValue());
                    postData.add("deptID", deptData.get("id").getNonLiteralValue());
                    return postData;
                }
            }
        }
        
        @Override
        protected ControllerMessage prepareEditPost()
        {
            ControllerMessage postData  =   super.prepareEditPost();
            return addDeptToPost(postData);
        }
        
        @Override
        protected ControllerMessage prepareAddPost()
        {
            ControllerMessage postData  =   super.prepareAddPost();
            return addDeptToPost(postData);
        }
        
        @Override
        protected ControllerMessage prepareRemovePost()
        {
            ControllerMessage postData  =   super.prepareRemovePost();
            return addDeptToPost(postData);
        }

        @Override
        protected JsonArray getData() 
        {
            Column dept_col =   Agent.getActiveSession().getUser().get("DEPT_ID");
            if(dept_col == null)   return new JsonArray();
            else return DeptAnnouncementsModel.getDeptAnnouncementsFor(Integer.parseInt(dept_col.getNonLiteralValue().toString()));
        }
                
    }
    
        public static void main(String[] args)
        {
            DepartmentView view =   new DepartmentView();
        }
}
