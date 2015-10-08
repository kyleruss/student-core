
package engine.views.gui;

import engine.views.GUIView;
import java.awt.Color;
import javax.swing.JPanel;


public class Home extends GUIView
{

    @Override
    protected void initComponents() 
    {
        panel   =   new JPanel();
        panel.setBackground(Color.GREEN);
    }

    @Override
    protected void initResources() 
    {
        
    }
    
    @Override
    protected void initListeners()
    {
        
    }
    
}
