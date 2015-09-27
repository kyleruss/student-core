
package engine.views.gui.layout;

import engine.views.GUIView;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JPanel;


public class ViewPane extends JPanel
{
    private GUIView activeView;
    public ViewPane()
    {
        setPreferredSize(new Dimension
        (
            Window.getWindowDim().x,
            (int) (Window.getWindowDim().y * 0.9)
        ));
        
        setBackground(Color.WHITE);
    }
    
    public GUIView getActiveView()
    {
        return activeView;
    }
    
    public void setActiveView(GUIView view)
    {
        remove(activeView.getPanel());
        activeView  =   view;
        add(activeView.getPanel());
        revalidate();
    }
}
