
package engine.views.gui.layout;

import engine.config.AppConfig;
import engine.config.ConfigFactory;
import engine.views.GUIView;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class Window extends JFrame
{
    private Layout layout;
    
    public Window()
    {
        super((String) ConfigFactory.get(ConfigFactory.APP_CONFIG, AppConfig.APP_NAME));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        layout      =   new Layout();
        getContentPane().add(layout);
        layout.getMenu().attachTo(this);
        
        Point dim   =   getWindowDim();
        setSize(dim.x, dim.y);
        
        setResizable(false);
        setLocationRelativeTo(null);
    }
    
    public void display()
    {
        setVisible(true);
    }
    
    public static Point getWindowDim()
    {
        Dimension size  =   Toolkit.getDefaultToolkit().getScreenSize();
        double width       =   size.width *  (double) ConfigFactory.get(ConfigFactory.APP_CONFIG, AppConfig.GUI_WIDTH_MULTI);
        double height      =   size.height * (double) ConfigFactory.get(ConfigFactory.APP_CONFIG, AppConfig.GUI_HEIGHT_MULTI);
        
        return new Point((int) width, (int) height);
    }
    
    public Layout getAppLayout()
    {
        return layout;
    }
    
    public void setActiveView(GUIView view)
    {
        layout.getViewPane().setActiveView(view);
    }
}
