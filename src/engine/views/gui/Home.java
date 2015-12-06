//====================================
//  KYLE RUSSELL
//  13831056
//  PDC Project
//====================================

package engine.views.gui;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import engine.controllers.ControllerMessage;
import engine.core.Agent;
import engine.core.ExceptionOutput;
import engine.core.RouteHandler;
import engine.models.ClassEnrolmentModel;
import engine.models.Role;
import engine.models.User;
import engine.views.GUIView;
import engine.views.View;
import engine.views.gui.admin.modules.DataModuleView;
import engine.views.gui.layout.Layout;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
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
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;


public class Home extends GUIView implements ActionListener
{
    private BufferedImage backgroundImage;
    private BufferedImage userAvatarImage;
    private BufferedImage adminButtonImage;
    private BufferedImage deptButtonImage;
    private BufferedImage classesButtonImage;
    private BufferedImage homeLabelImage;
    private BufferedImage notificationImage;
    
    private JPanel homePanel;
    private JPanel homeDisplayPanel;
    private JLabel homeLabel;
    private ClassesPanel classPanel;
    
    private JPanel homeControls;
    private JButton adminGoButton, notificationsGoButton, deptGoButton;
    
    private JPanel userPanel;
    private JPanel userDetailsPanel;
    private JLabel avatar;
    private JLabel nameLabel;
    private JLabel roleLabel;
    private JLabel usernameLabel;
    
    public Home()
    {
        super();
    }
    
    public Home(ControllerMessage data)
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
        
        panel.setBackground(Color.WHITE);
        classPanel  =   new ClassesPanel();
        homePanel   =   new JPanel(new BorderLayout());
        homePanel.setBackground(Color.WHITE);
        homePanel.setPreferredSize(new Dimension(600, 350));
        
        
        homeControls        =   new JPanel(new GridLayout(1, 3));
        homeControls.setBackground(Color.WHITE);
        homeControls.setPreferredSize(new Dimension(600, 35));
        
        homeControls.setOpaque(false);
        
        adminGoButton               =   new JButton("Admin Panel");
        notificationsGoButton       =   new JButton("Notifications");
        deptGoButton                =   new JButton("Department");
        
        adminGoButton.setIcon(new ImageIcon(adminButtonImage));
        notificationsGoButton.setIcon(new ImageIcon(notificationImage));
        deptGoButton.setIcon(new ImageIcon(deptButtonImage));
        
        homeControls.add(adminGoButton);
        homeControls.add(notificationsGoButton);
        homeControls.add(deptGoButton);
        
        homeDisplayPanel                =   new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel innerHomeDisplayPanel    =   new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        innerHomeDisplayPanel.setBackground(Color.WHITE);
        homeDisplayPanel.setBackground(Color.WHITE);
        innerHomeDisplayPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        homeLabel   =   new JLabel("Home");
        homeLabel.setIcon(new ImageIcon(homeLabelImage));
        innerHomeDisplayPanel.add(homeLabel);
        
        homeDisplayPanel.add(classPanel);
        
        userPanel           =   new JPanel();
        userPanel.setPreferredSize(new Dimension(200, homePanel.getPreferredSize().height));
        userPanel.setBackground(Color.WHITE);
        userPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 2, Color.GRAY));
        
        userDetailsPanel    =   new JPanel(new GridLayout(3, 1));
        userDetailsPanel.setPreferredSize(new Dimension(180, 100));
        userDetailsPanel.setBackground(Color.WHITE);
        userDetailsPanel.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.LIGHT_GRAY));
        
        User user       =   Agent.getActiveSession().getUser();
        String username =   (String) user.get("username").getNonLiteralValue();
        String role     =   Role.getUserRole(username);
        String fName    =   (String) user.get("firstname").getNonLiteralValue();
        String lName    =   (String) user.get("lastname").getNonLiteralValue();
        usernameLabel   =   new JLabel(username, SwingConstants.CENTER);
        roleLabel       =   new JLabel(role, SwingConstants.CENTER);
        nameLabel       =   new JLabel(fName + " " + lName, SwingConstants.CENTER);
        
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        roleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        
        JLabel nameTitleLabel       =   new JLabel("Name", SwingConstants.CENTER);
        JLabel usernameTitleLabel   =   new JLabel("Username", SwingConstants.CENTER);
        JLabel roleTitleLabel       =   new JLabel("Role", SwingConstants.CENTER);
        nameTitleLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        usernameTitleLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        roleTitleLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        
        userDetailsPanel.add(nameLabel);
        userDetailsPanel.add(usernameLabel);
        userDetailsPanel.add(roleLabel);
        
        avatar      =   new JLabel(new ImageIcon(userAvatarImage));
        userPanel.add(avatar);
        userPanel.add(Box.createRigidArea(new Dimension(100, 20)));
        userPanel.add(userDetailsPanel);
        
        homePanel.add(userPanel, BorderLayout.WEST);
        homePanel.add(homeDisplayPanel, BorderLayout.CENTER);
        
        panel.add(Box.createRigidArea(new Dimension(600, 30)));
        panel.add(homeControls);
        panel.add(Box.createRigidArea(new Dimension(600, 15)));
        panel.add(homePanel);
    }

    @Override
    protected void initResources() 
    {
        try
        {
            backgroundImage                     =   ImageIO.read(new File(Layout.getImage("blurredbackground19.jpg")));
            adminButtonImage                    =   ImageIO.read(new File(Layout.getImage("user_admin.png")));
            deptButtonImage                     =   ImageIO.read(new File(Layout.getImage("department_icon.png")));
            classesButtonImage                  =   ImageIO.read(new File(Layout.getImage("classes_icon.png")));
            homeLabelImage                      =   ImageIO.read(new File(Layout.getImage("home_icon.png")));
            notificationImage                   =   ImageIO.read(new File(Layout.getImage("notifications_small_icon.png")));
            
            int gender                          =   Integer.parseInt(Agent.getActiveSession().getUser().get("GENDER").getColumnValue().toString());
            if(gender == 1) userAvatarImage     =   ImageIO.read(new File(Layout.getImage("avatar_male.png")));
            else userAvatarImage                =   ImageIO.read(new File(Layout.getImage("avatar_female.png")));
        }
        
        catch(IOException e)
        {
            ExceptionOutput.output("Failed to load resources: " + e.getMessage(), ExceptionOutput.OutputType.MESSAGE);
        }
    }
    
    @Override
    protected void initListeners()
    {
        adminGoButton.addActionListener(this);
        notificationsGoButton.addActionListener(this);
        deptGoButton.addActionListener(this);
    }
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        Object src  =   e.getSource();
        
        if(src == adminGoButton)
            Agent.setView("getAdmincp");
        
        else if(src == notificationsGoButton)
            Agent.getWindow().getAppLayout().getHeadNav().showNotificationWindow();
        
        else if(src == deptGoButton)
            Agent.setView("getMyDepartment");
    }
    
    private class ClassesPanel extends JPanel implements ListSelectionListener
    {
        private JTable classTable;
        private DefaultTableModel classModel;
        private JLabel classesLabel;
        
        public ClassesPanel()
        {
            setLayout(new BorderLayout());
            setBackground(Color.WHITE);
            setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            
            classModel          =   new DefaultTableModel();
            classTable          =   new JTable(classModel);
            classesLabel        =   new JLabel("My classes");
            JPanel tableWrapper =   new JPanel();
            classTable.getSelectionModel().addListSelectionListener(this);
            
            classTable.setBackground(Color.WHITE);
            tableWrapper.setBackground(Color.WHITE);
            classesLabel.setIcon(new ImageIcon(classesButtonImage));
            classesLabel.setFont(new Font("Arial", Font.BOLD, 14));
            
            JScrollPane scroller    =   new JScrollPane(classTable);
            scroller.setPreferredSize(new Dimension(350, 250));
            tableWrapper.add(scroller);
            add(classesLabel, BorderLayout.NORTH);
            add(tableWrapper, BorderLayout.CENTER);
            
            initData();
        }
        
        private void initData()
        {
            DataModuleView.setColumns(new String[] { "ID", "Name", "Description", "Semester" }, classTable, classModel);
            JsonArray results   =   ClassEnrolmentModel.getStudentEnrolments((String) Agent.getActiveSession().getUser().get("username").getNonLiteralValue());
            
            if(results != null && results.size() > 1)
            {
                SwingUtilities.invokeLater(()->
                {
                    for(int i = 1; i < results.size(); i++)
                    {
                        JsonObject current  =   results.get(i).getAsJsonObject();
                        Object[] row        =   DataModuleView.getDataFromResults(current, 
                                            new String[] { "class_id", "class_name", "class_desc", "semester_num" });
                        classModel.addRow(row);
                    }
                });
            }
        }

        @Override
        public void valueChanged(ListSelectionEvent e) 
        {
            if(!e.getValueIsAdjusting())
            {
                int selectedRow =   classTable.getSelectedRow();
                int classID     =   Integer.parseInt(classTable.getValueAt(selectedRow, 0).toString());
                View classPage  =   RouteHandler.go("getClassPage", new Object[] { classID }, new Class<?>[] { Integer.class }, null);
                Agent.setView(classPage);
            }
        }
    }
    
}
