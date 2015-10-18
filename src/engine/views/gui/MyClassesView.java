package engine.views.gui;

import engine.controllers.ControllerMessage;
import engine.views.GUIView;
import javax.swing.JPanel;


public class MyClassesView extends GUIView
{
    public MyClassesView()
    {
        super();
    }
    
    public MyClassesView(ControllerMessage data)
    {
        super(data);
    }

    @Override
    protected void initComponents() 
    {
        panel   =   new JPanel();
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
