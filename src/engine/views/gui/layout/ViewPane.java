
package engine.views.gui.layout;

import engine.views.GUIView;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JPanel;


public class ViewPane extends JPanel
{
    private GUIView activeView;
    private final Transition transitionView;
    
    public ViewPane()
    {
        setPreferredSize(new Dimension
        (
            Window.getWindowDim().x,
            (int) (Window.getWindowDim().y * 0.9)
        ));
        
        transitionView  =   new Transition();
        transitionView.setPreferredSize(getPreferredSize());
        
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
    }
    
    public GUIView getActiveView()
    {
        return activeView;
    }
    
    public void addPanel(JPanel panel)
    {
        add(panel, BorderLayout.CENTER);
        revalidate();
    }
    
    public void showTransition()
    {
        if(activeView != null)
            remove(activeView.getPanel());
        
        addPanel(transitionView);
    }
    
    public void hideTransition()
    {
        remove(transitionView);
        
        if(activeView != null)
            add(activeView.getPanel());
    }
    
    public void setActiveView(GUIView view)
    {
        if(view == null) return;
        
        if(activeView != null) 
            remove(activeView.getPanel());
        
        activeView  =   view;
        addPanel(activeView.getPanel());
    }
}
