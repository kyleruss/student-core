
package engine.views;

import engine.controllers.ControllerMessage;
import engine.core.Agent;
import engine.core.ExceptionOutput;
import engine.views.gui.layout.Layout;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;


public abstract class GUIView extends AbstractView
{
    protected JPanel panel;
    protected Font helveticaThin;
    protected BufferedImage addSmallImage;
    protected BufferedImage removeSmallImage;
    protected BufferedImage editSmallImage;
    
    public GUIView()
    {
        super();
        initView();
    }
    
    //Creates a view with name, description and address
    public GUIView(String viewTitle, String viewDescription)
    {
        super(new ControllerMessage(), viewTitle, viewDescription);
        initView();
    }
    
    //Create a general view with messages passed
    public GUIView(ControllerMessage messages)
    {
        super(messages, "Layout", "Layout view");
        initView();
    } 
    
    public GUIView(ControllerMessage messages, String viewTitle, String viewDescription)
    {
        super(messages, viewTitle, viewDescription);
        initView();
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
    
    protected abstract void initListeners();
    
    protected void initAppResources()
    {
        try
        {
            helveticaThin       =   Font.createFont(Font.TRUETYPE_FONT, new FileInputStream(Layout.getImage("HelveticaThin.ttf")));
            addSmallImage       =   ImageIO.read(new File(Layout.getImage("addSmallIcon.png")));
            removeSmallImage    =   ImageIO.read(new File(Layout.getImage("removeSmallIcon.png")));
            editSmallImage      =   ImageIO.read(new File(Layout.getImage("edit_icon.png")));
        }
        
        catch(IOException | FontFormatException e)
        {
            ExceptionOutput.output("Loading resource error: " + e.getMessage(), ExceptionOutput.OutputType.MESSAGE);
        }
    }
    
    protected void initView()
    {
        initAppResources();
        initResources();
        initComponents();
        initListeners();
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
