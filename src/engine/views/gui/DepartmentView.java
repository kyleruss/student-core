package engine.views.gui;

import engine.controllers.ControllerMessage;
import engine.views.GUIView;
import javax.swing.JPanel;

public class DepartmentView extends GUIView
{
    public DepartmentView()
    {
        super();
    }
    
    public DepartmentView(ControllerMessage data)
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
