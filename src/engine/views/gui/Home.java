
package engine.views.gui;

import engine.core.ExceptionOutput;
import engine.views.GUIView;
import engine.views.gui.layout.Layout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JPanel;


public class Home extends GUIView implements ActionListener
{
    private BufferedImage backgroundImage;
    
    private JPanel homePanel;
    private JPanel homeControls;
    
    private JPanel userPanel;
    
    
    @Override
    protected void initComponents() 
    {
        panel   =   new JPanel()
        {
            @Override
            public void paintComponent(Graphics g)
            {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, null);
            }
        };
        
        panel.setBackground(Color.WHITE);
        
        homePanel   =   new JPanel();
        homePanel.setBackground(Color.WHITE);
        homePanel.setPreferredSize(new Dimension(600, 350));
   //     homePanel.setBorder(BorderFactory.createEmptyBorder(100, 0, 0, 0));
        
        homeControls    =   new JPanel();
        homeControls.setBackground(Color.WHITE);
        homeControls.setPreferredSize(new Dimension(600, 35));
        
        panel.add(Box.createRigidArea(new Dimension(600, 30)));
        panel.add(homeControls);
        panel.add(Box.createRigidArea(new Dimension(600, 15)));
        panel.add(homePanel);
    }

    @Override
    protected void initResources() 
    {
        try
        {
            backgroundImage  =   ImageIO.read(new File(Layout.getImage("blurredbackground25.jpg")));
        }
        
        catch(IOException e)
        {
            ExceptionOutput.output("Failed to load resources: " + e.getMessage(), ExceptionOutput.OutputType.DEBUG);
        }
    }
    
    @Override
    protected void initListeners()
    {
        
    }
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        Object src  =   e.getSource();
    }
    
}
