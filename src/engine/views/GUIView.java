
package engine.views;

import engine.controllers.ControllerMessage;
import engine.core.Agent;
import engine.core.ExceptionOutput;
import engine.views.gui.layout.Layout;
import java.awt.Font;
import java.awt.FontFormatException;
import java.io.FileInputStream;
import java.io.IOException;
import javax.swing.JPanel;


public abstract class GUIView extends AbstractView
{
    protected JPanel panel;
    protected Font helveticaThin;
    
    public GUIView()
    {
        super();
        initAppResources();
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
    
    protected void initAppResources()
    {
        try
        {
            helveticaThin   =   Font.createFont(Font.TRUETYPE_FONT, new FileInputStream(Layout.getImage("HelveticaThin.ttf")));
        }
        
        catch(IOException | FontFormatException e)
        {
            ExceptionOutput.output("Loading resource error: " + e.getMessage(), ExceptionOutput.OutputType.MESSAGE);
        }
    }
    
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
