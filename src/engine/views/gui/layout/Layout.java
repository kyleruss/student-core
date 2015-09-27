package engine.views.gui.layout;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;


public class Layout extends JPanel
{
    private final HeaderNavigation headNav;
    private final ViewPane viewPane;
    
    public Layout()
    {
        initLookAndFeel();
        setLayout(new BorderLayout());
        
        headNav     =   new HeaderNavigation();
        viewPane    =   new ViewPane();
        
        add(headNav, BorderLayout.NORTH);
        add(viewPane, BorderLayout.CENTER);
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
    
    public ViewPane getViewPane()
    {
        return viewPane;
    }
}
