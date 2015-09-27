package engine.views.gui.layout;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;


public class Layout extends JPanel
{
    private final HeaderNavigation headNav;
    
    public Layout()
    {
        initLookAndFeel();
        setLayout(new BorderLayout());
        
        headNav =   new HeaderNavigation();
        add(headNav, BorderLayout.NORTH);
    }
    
    private void initLookAndFeel()
    {
        try 
        {
            UIManager.setLookAndFeel("de.javasoft.plaf.synthetica.SyntheticaAluOxideLookAndFeel");
        }
        
        catch(ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e)
        {
            System.out.println("[Exception] Failed to load LookAndFeel: " + e.getMessage());
        }
    }
    
    public HeaderNavigation getHeadNav()
    {
        return headNav;
    }
}
