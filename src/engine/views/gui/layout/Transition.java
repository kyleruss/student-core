
package engine.views.gui.layout;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Transition extends JPanel
{
    private ImageIcon spinnerImage;
    private JLabel spinnerLabel;
    
    public Transition()
    {
        this(Color.WHITE);
    }
    
    public Transition(Color background)
    {
        super(new BorderLayout());
        spinnerImage    =   new ImageIcon(Layout.getImage("loadspinner.gif"));
        spinnerLabel    =   new JLabel(spinnerImage);
        setBackground(background);
        add(spinnerLabel, BorderLayout.CENTER);
    }
    
    public ImageIcon getSpinner()
    {
        return spinnerImage;
    }
}
