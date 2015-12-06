//====================================
//	Kyle Russell
//	StudentCore
//	Menu
//====================================

package engine.views.gui.layout;

import engine.core.Agent;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Menu implements ActionListener
{
    private JMenuBar menuBar;
    private JMenu file, about, view, user;
    private JMenuItem exit, minimize, author;
    private JMenuItem profile, notifications, logout;
    private JMenuItem refresh, next, prev;
    private JMenuItem login, register;
   
    public Menu()
    {
        menuBar =   new JMenuBar();
        
        file    =   new JMenu("File");
        about   =   new JMenu("About");
        view    =   new JMenu("View");
        user    =   new JMenu("User");
        
        menuBar.add(file);
        menuBar.add(view);
        menuBar.add(user);
        menuBar.add(about);
        
        exit            =   new JMenuItem("Exit");
        minimize        =   new JMenuItem("Minimize");
        author          =   new JMenuItem("Author");
        profile         =   new JMenuItem("Profile");
        notifications   =   new JMenuItem("Notifications");
        logout          =   new JMenuItem("Logout");
        refresh         =   new JMenuItem("Refresh");
        next            =   new JMenuItem("Go forward");
        prev            =   new JMenuItem("Go back");
        login           =   new JMenuItem("Login");
        register        =   new JMenuItem("Register");
        
        user.add(profile);
        user.add(notifications);
        user.add(logout);
        user.add(login);
        user.add(register);
        
        view.add(refresh);
        view.add(next);
        view.add(prev);
        
        file.add(exit);
        file.add(minimize);
        
        about.add(author);
        
        profile.addActionListener(this);
        notifications.addActionListener(this);
        logout.addActionListener(this);
        login.addActionListener(this);
        register.addActionListener(this);
        refresh.addActionListener(this);
        next.addActionListener(this);
        prev.addActionListener(this);
        exit.addActionListener(this);
        minimize.addActionListener(this);
        author.addActionListener(this);
        
        prev.setEnabled(false);
        next.setEnabled(false);
        setEnableUserControls(false);
    }
    
    public void attachTo(JFrame frame)
    {
        frame.setJMenuBar(menuBar);
    }
    
    private void showAuthorDetails()
    {
        JPanel dialog           =   new JPanel();
        JLabel nameLabel        =   new JLabel("Kyle Russell");
        JLabel uniLabel         =   new JLabel("Auckland University of Technology");
        JLabel contactLabel     =   new JLabel("kyleruss2030.jn@gmail.com");
        
        nameLabel.setHorizontalAlignment(JLabel.CENTER);
        uniLabel.setHorizontalAlignment(JLabel.CENTER);
        contactLabel.setHorizontalAlignment(JLabel.CENTER);
        dialog.setLayout(new GridLayout(3, 1));
        
        dialog.add(nameLabel);
        dialog.add(uniLabel);
        dialog.add(contactLabel);
        
        JOptionPane.showMessageDialog(null, dialog, "Author", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void setEnableUserControls(boolean enable)
    {
        profile.setEnabled(enable);
        notifications.setEnabled(enable);
        logout.setEnabled(enable);
    }
    
    public void setEnableNext(boolean enable)
    {
        next.setEnabled(enable);
    }
    
    public void setEnablePrev(boolean eanble)
    {
        prev.setEnabled(eanble);
    }
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        Object src  =   e.getSource();
        
        if(src == next)
            Agent.setNextView();
        
        else if(src == prev)
            Agent.setPrevView();
        
        else if(src == refresh)
            Agent.refreshView();
        
        else if(src == logout)
            Agent.sessionLogout();
        
        else if(src == login)
            Agent.setView("getLogin");
        
        else if(src == register)
            Agent.setView("getRegister");
        
        else if(src == exit)
            System.exit(0);
        
        else if(src == minimize)
            Agent.getWindow().setState(Frame.ICONIFIED);
        
        else if(src == author)
            showAuthorDetails();
        
        else if(src == profile)
            Agent.setView("getHome");
        
        else if(src == notifications)
            Agent.getWindow().getAppLayout().getHeadNav().showNotificationWindow();
    }
}
