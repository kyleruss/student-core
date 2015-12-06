//====================================
//	Kyle Russell
//	StudentCore
//	ViewPane
//====================================
package engine.views.gui.layout;

import engine.views.GUIView;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JPanel;


public class ViewPane extends JPanel
{
    private GUIView activeView;
    private final Transition transitionView;
    
    private final String TRANSITION_VIEW    =   "transition";
    private final String ACTIVE_VIEW        =   "active";
    
    public ViewPane()
    {
        super(new CardLayout());
        setPreferredSize(new Dimension
        (
            Window.getWindowDim().x,
            (int) (Window.getWindowDim().y * 0.9)
        ));
        
        transitionView  =   new Transition();
        transitionView.setPreferredSize(getPreferredSize());
        add(transitionView, TRANSITION_VIEW);
        
        setBackground(Color.WHITE);
    }
    
    public GUIView getActiveView()
    {
        return activeView;
    }
    
    public void addPanel(JPanel panel)
    {
        add(panel);
        revalidate();
    }
    
    public void showTransition()
    {
        CardLayout cLayout  =   (CardLayout) getLayout();
        cLayout.show(this, TRANSITION_VIEW);
    }
    
    public void hideTransition()
    {
        if(activeView == null) return;
        
        CardLayout cLayout  =   (CardLayout) getLayout();
        cLayout.show(this, ACTIVE_VIEW);
    }
    
    public void setActiveView(GUIView view)
    {
        if(view == null) return;
        
        activeView  =   view;
        add(activeView.getPanel(), ACTIVE_VIEW);
    }
}
