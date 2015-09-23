
package engine.views;

import engine.controllers.ControllerMessage;
import javax.swing.JPanel;


public abstract class GUIView extends AbstractView
{
    protected JPanel panel;
    
    public GUIView()
    {
        super();
    }
    
    //Creates a view with name, description and address
    public GUIView(String viewTitle, String viewDescription)
    {
        super(new ControllerMessage(), viewTitle, viewDescription);
    }
    
    //Create a general view with messages passed
    public GUIView(ControllerMessage messages)
    {
        super(messages, "Layout", "Layout view");
    } 
    
    public GUIView(ControllerMessage messages, String viewTitle, String viewDescription)
    {
        super(messages, viewTitle, viewDescription);
    }
    
    @Override
    protected abstract String getCommandsFile();
    
    protected abstract void initComponents();
    
    @Override
    public void display()
    {
        panel.revalidate();
        panel.repaint();
    }
    
    public JPanel getPanel()
    {
        return panel;
    }
}
