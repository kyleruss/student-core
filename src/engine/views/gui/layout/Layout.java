package engine.views.gui.layout;

import engine.config.AppConfig;
import engine.config.ConfigFactory;
import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;


public class Layout extends JPanel
{
    private final HeaderNavigation headNav;
    private final ViewPane viewPane;
    private final Menu menu;
    
    public Layout()
    {
        initLookAndFeel();
        setLayout(new BorderLayout());
        
        headNav     =   new HeaderNavigation();
        viewPane    =   new ViewPane();
        menu        =   new Menu();
        
        
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
    
    public Menu getMenu()
    {
        return menu;
    }
    
    public static String getImage(String name)
    {
        String dir  =   (String) ConfigFactory.get(ConfigFactory.APP_CONFIG, AppConfig.RESOURCE_DIR);
        System.out.println(dir + name);
        return dir + name;
    }
    
    public static void makeTransparent(JButton button)
    {
        button.setOpaque(false);
        button.setContentAreaFilled(true);
        button.setBorderPainted(false);
    }
}
