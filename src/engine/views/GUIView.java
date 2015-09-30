
package engine.views;

import engine.controllers.ControllerMessage;
import engine.core.Agent;
import javax.swing.JPanel;


public abstract class GUIView extends AbstractView
{
    protected JPanel panel;
    
    public GUIView()
    {
        super();
        initResources();
        initComponents();
            
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
    protected String getCommandsFile()
    {
        return null;
    }
    
    protected void initPanel()
    {
        if(panel != null)
            panel.setPreferredSize(Agent.getWindow().getAppLayout().getViewPane().getPreferredSize());
    }
    
    protected abstract void initComponents();
    
    protected abstract void initResources();
    
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
