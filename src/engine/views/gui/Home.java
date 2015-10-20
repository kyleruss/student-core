
package engine.views.gui;

import engine.controllers.ControllerMessage;
import engine.core.Agent;
import engine.core.ExceptionOutput;
import engine.core.database.Conditional;
import engine.core.database.Join;
import engine.models.Role;
import engine.models.User;
import engine.views.GUIView;
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
import java.sql.SQLException;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;


public class Home extends GUIView implements ActionListener
{
    private BufferedImage backgroundImage;
    private BufferedImage userAvatarImage;
    private BufferedImage adminButtonImage;
    private BufferedImage deptButtonImage;
    private BufferedImage classesButtonImage;
    private BufferedImage homeLabelImage;
    
    private JPanel homePanel;
    private JPanel homeDisplayPanel;
    private JLabel homeLabel;
    
    private JPanel homeControls;
    private JButton adminGoButton, classesGoButton, deptGoButton;
    
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
        
        homePanel   =   new JPanel(new BorderLayout());
        homePanel.setBackground(Color.WHITE);
        homePanel.setPreferredSize(new Dimension(600, 350));
        
        
        homeControls        =   new JPanel(new GridLayout(1, 3));
        homeControls.setBackground(Color.WHITE);
        homeControls.setPreferredSize(new Dimension(600, 35));
        
        homeControls.setOpaque(false);
        
        adminGoButton       =   new JButton("Admin Panel");
        classesGoButton     =   new JButton("Classes");
        deptGoButton        =   new JButton("Department");
        
        adminGoButton.setIcon(new ImageIcon(adminButtonImage));
        classesGoButton.setIcon(new ImageIcon(classesButtonImage));
        deptGoButton.setIcon(new ImageIcon(deptButtonImage));
        
        homeControls.add(adminGoButton);
        homeControls.add(classesGoButton);
        homeControls.add(deptGoButton);
        
        homeDisplayPanel                =   new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel innerHomeDisplayPanel    =   new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        innerHomeDisplayPanel.setBackground(Color.WHITE);
        homeDisplayPanel.setBackground(Color.WHITE);
        innerHomeDisplayPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        homeLabel   =   new JLabel("Home");
        homeLabel.setIcon(new ImageIcon(homeLabelImage));
        innerHomeDisplayPanel.add(homeLabel);
        homeDisplayPanel.add(innerHomeDisplayPanel);
        
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
        JLabel usernameTitleLabel  =   new JLabel("Username", SwingConstants.CENTER);
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
            
            int gender                          =    Integer.parseInt(Agent.getActiveSession().getUser().get("GENDER").getColumnValue().toString());
            
            if(gender == 1) userAvatarImage     =   ImageIO.read(new File(Layout.getImage("avatar_male.png")));
            else userAvatarImage                =   ImageIO.read(new File(Layout.getImage("avatar_female.png")));
        }
        
        catch(IOException e)
        {
            ExceptionOutput.output("Failed to load resources: " + e.getMessage(), ExceptionOutput.OutputType.DEBUG);
        }
    }
    
    @Override
    protected void initListeners()
    {
        adminGoButton.addActionListener(this);
        classesGoButton.addActionListener(this);
        deptGoButton.addActionListener(this);
    }
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        Object src  =   e.getSource();
        
        if(src == adminGoButton)
            Agent.setView("getAdmincp");
        
        else if(src == classesGoButton)
            Agent.setView("getMyClasses");
        
        else if(src == deptGoButton)
            Agent.setView("getMyDepartment");
    }
    
}
