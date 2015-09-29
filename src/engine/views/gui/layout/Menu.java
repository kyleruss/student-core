
package engine.views.gui.layout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class Menu implements ActionListener
{
    private JMenuBar menuBar;
    private JMenu file, options, about;
    private JMenuItem exit, minimize, author, settings;
   
    public Menu()
    {
        menuBar =   new JMenuBar();
        
        file    =   new JMenu("File");
        options =   new JMenu("Options");
        about   =   new JMenu("About");
        
        menuBar.add(file);
        menuBar.add(options);
        menuBar.add(about);
        
        exit        =   new JMenuItem("Exit");
        minimize    =   new JMenuItem("Minimize");
        author      =   new JMenuItem("Author");
        settings    =   new JMenuItem("Settings");
        
        file.add(exit);
        file.add(minimize);
        
        options.add(settings);
        
        about.add(author);
    }
    
    public void attachTo(JFrame frame)
    {
        frame.setJMenuBar(menuBar);
    }
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        
    }
}
