//====================================
//	Kyle Russell
//	StudentCore
//	Transition
//====================================

package engine.views.gui.layout;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Transition extends JPanel
{
    private static ImageIcon spinnerImage;
    private static ImageIcon smallSpinnerImage;
    private JLabel spinnerLabel;
    
    public Transition()
    {
        this(Color.WHITE);
    }
    
    public Transition(Color background)
    {
        super(new BorderLayout());
        spinnerImage        =   new ImageIcon(Layout.getImage("loadspinner.gif"));
        smallSpinnerImage   =   new ImageIcon(Layout.getImage("spinner.gif"));
        spinnerLabel        =   new JLabel(spinnerImage);
        setBackground(background);
        add(spinnerLabel, BorderLayout.CENTER);
    }
    
    public static ImageIcon getSpinner()
    {
        return spinnerImage;
    }
    
    public static ImageIcon getSmallSpinner()
    {
        return smallSpinnerImage;
    }
}
