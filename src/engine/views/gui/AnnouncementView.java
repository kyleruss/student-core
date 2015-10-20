
package engine.views.gui;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import engine.controllers.ControllerMessage;
import engine.models.AdminAnnouncementsModel;
import engine.views.GUIView;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import org.jdesktop.xswingx.PromptSupport;


public abstract class AnnouncementView extends GUIView implements ActionListener
{
    protected static final String ANNOUNCEMENT_VIEW =   "announce_v";
    protected static final String MODIFY_VIEW       =   "modify_v";
    protected JPanel announcementPanel;
    protected JPanel announcementViewPanel;
    protected ModifyAnnouncementView modifyPanel;
    protected JList announcementList;
    protected UpdateListModel announcementModel;
    protected JButton addAnnouncementButton;
    protected JButton removeAnnouncementButton;
    protected JButton editAnnouncementButton;
    
    public AnnouncementView()
    {
        super();
    }
    
    public AnnouncementView(ControllerMessage data)
    {
        super(data);
    }
    
    @Override
    protected void initComponents()
    {
        announcementViewPanel       =   new JPanel(new CardLayout());
        announcementPanel           =   new JPanel(new BorderLayout());
        modifyPanel                 =   new ModifyAnnouncementView();
        JPanel announcementHeader   =   new JPanel();   
        JPanel announcementControls =   new JPanel(new GridLayout(1, 3));
        addAnnouncementButton       =   new JButton("Add");
        removeAnnouncementButton    =   new JButton("Remove");
        editAnnouncementButton      =   new JButton("Edit");
        
        addAnnouncementButton.setIcon(new ImageIcon(addSmallImage));
        removeAnnouncementButton.setIcon(new ImageIcon(removeSmallImage));
        editAnnouncementButton.setIcon(new ImageIcon(editSmallImage));
        
        JPanel announcementsWrapper =   new JPanel();
        announcementPanel.setBackground(Color.WHITE);
        announcementHeader.setBackground(Color.WHITE);
        announcementsWrapper.setBackground(Color.WHITE);
        announcementControls.setBackground(Color.WHITE);
        modifyPanel.setBackground(Color.WHITE);
        
        announcementControls.add(addAnnouncementButton);
        announcementControls.add(removeAnnouncementButton);
        announcementControls.add(editAnnouncementButton);

        announcementModel   =   new UpdateListModel();
        announcementList    =   new JList(announcementModel);
        announcementList.setCellRenderer(new AnnouncementCellRenderer());
        announcementList.setPreferredSize(new Dimension(380, 150));
        announcementsWrapper.setPreferredSize(new Dimension(400, 150));
        announcementControls.setBorder(BorderFactory.createTitledBorder("Controls"));
        
        
        JsonArray announcements =   getData();
        
        if(announcements.size() > 0) 
        {
            announcementPanel.add(announcementList);
            for(int i = 1; i < announcements.size(); i++)
                announcementModel.addElement(announcements.get(i).getAsJsonObject());
            
            announcementList.setSelectedIndex(0);
        }
        
        JScrollPane announcementScroller    =   new JScrollPane(announcementList);
        announcementScroller.setPreferredSize(new Dimension(390, 300));
        announcementsWrapper.add(announcementScroller);
        
        
        
        announcementHeader.add(announcementControls, BorderLayout.EAST);
        announcementHeader.setPreferredSize(new Dimension(1, 75));
        announcementHeader.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 30));
        
        announcementPanel.add(announcementHeader, BorderLayout.NORTH);
        announcementPanel.add(announcementsWrapper, BorderLayout.CENTER);
        
        announcementViewPanel.add(announcementPanel, ANNOUNCEMENT_VIEW);
        announcementViewPanel.add(modifyPanel, MODIFY_VIEW);
        
        showAnnouncementView(ANNOUNCEMENT_VIEW);
    }
    

    @Override
    protected void initResources()
    {
    }

    @Override
    protected void initListeners()
    {
        addAnnouncementButton.addActionListener(this);
        removeAnnouncementButton.addActionListener(this);
        editAnnouncementButton.addActionListener(this);
    }
    
    protected abstract void addAnnouncement();
    
    protected abstract void removeAnnouncement();
    
    protected abstract void editAnnouncement();
     
    protected abstract JsonArray getData();
    
    public JPanel getAnnouncementViewPanel()
    {
        return announcementViewPanel;
    }
    
    public JList getList()
    {
        return announcementList;
    }
    
    public UpdateListModel getModel()
    {
        return announcementModel;
    }

    public void showAnnouncementView(String viewName)
    {
        CardLayout cLayout  =   (CardLayout) announcementViewPanel.getLayout();
        cLayout.show(announcementViewPanel, viewName);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) 
    {
        Object src = e.getSource();
        
        if(src == addAnnouncementButton)
            addAnnouncement();
        
        else if(src == editAnnouncementButton)
            editAnnouncement();
        
        else if(src == removeAnnouncementButton)
            removeAnnouncement();
    }
    
    protected class AnnouncementCellRenderer implements ListCellRenderer
    {
        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) 
        {
            JsonObject jObj             =   (JsonObject) value;
            JPanel announcementPanel    =   new JPanel(new BorderLayout());
            JPanel announceHeaderPanel  =   new JPanel();
            JPanel announceInfoPanel    =   new JPanel();
            JPanel contentWrapper       =   new JPanel();
            JLabel announcerLabel       =   new JLabel(jObj.get("ANNOUNCER").getAsString());
            JLabel announceDateLabel    =   new JLabel(jObj.get("ANNOUNCE_DATE").getAsString());
            JTextArea content           =   new JTextArea(jObj.get("CONTENT").getAsString());
            
            announcementPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
            announcementPanel.setBackground(Color.WHITE);
            announceHeaderPanel.setBackground(Color.BLACK);
            content.setBackground(Color.WHITE);
            contentWrapper.setBackground(Color.WHITE);
            announcerLabel.setForeground(Color.WHITE);
            announceDateLabel.setForeground(Color.WHITE);
            announceInfoPanel.setBackground(Color.BLACK);
            
            announceInfoPanel.add(announcerLabel);
            announceInfoPanel.add(announceDateLabel);
            announceHeaderPanel.add(announceInfoPanel, BorderLayout.WEST);
            
            JScrollPane contentScrollPane   =   new JScrollPane(content);
            contentScrollPane.setBorder(null);
            contentWrapper.add(contentScrollPane);
            
            announcementPanel.add(announceHeaderPanel, BorderLayout.NORTH);
            announcementPanel.add(contentWrapper, BorderLayout.CENTER);
            if(index == announcementList.getSelectedIndex()) 
            {
                contentWrapper.setVisible(true);
                announcementModel.update(index);
                announceHeaderPanel.setBackground(new Color(67, 133, 224));
                announceInfoPanel.setBackground(new Color(67, 133, 224));
            }
            else 
            {
                contentWrapper.setVisible(false);
                announcementModel.update(index);

            }
           
            return announcementPanel;
        }
    }

    protected class ModifyAnnouncementView extends JPanel
    {
        protected JTextField announcementTitle;
        protected JTextArea announcementContent;
        
        public ModifyAnnouncementView()
        {
            setLayout(new BorderLayout());
            announcementTitle   =   new JTextField();
            announcementContent =   new JTextArea();
            
            modifyPanel.setBackground(Color.WHITE);
            announcementContent.setBackground(Color.WHITE);
            
            PromptSupport.setPrompt("Enter your announcement content", announcementContent);
            PromptSupport.setPrompt("Enter your announcement title", announcementTitle);
            
            add(announcementTitle, BorderLayout.NORTH);
            add(announcementContent, BorderLayout.CENTER);
        }
        
        protected void fill(String title, String content)
        {
            announcementTitle.setText(title);
            announcementContent.setText(content);
        }
        
        protected String getTitle()
        {
            return announcementTitle.getText();
        }
        
        protected String getContent()
        {
            return announcementContent.getText();
        }
    }
    
    protected class UpdateListModel extends DefaultListModel
    {
        public void update(int index)
        {
            fireContentsChanged(this, index, index);
        }
    }
}
