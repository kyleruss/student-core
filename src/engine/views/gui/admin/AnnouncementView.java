
package engine.views.gui.admin;

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
import java.awt.FlowLayout;
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
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import org.jdesktop.xswingx.PromptSupport;


public abstract class AnnouncementView extends GUIView implements ActionListener
{
    protected enum ManageContext
    {
        ADDING,
        REMOVING,
        EDITING
    }
    
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
    protected JButton submitButton, backButton;
    protected ManageContext context;
    protected JLabel operationLabel;
    
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
        JPanel announcementHeader   =   new JPanel(new GridLayout(2, 1));   
        JPanel announcementControls =   new JPanel(new GridLayout(1, 3));
        JPanel statusWrapper        =   new JPanel();
        operationLabel              =   new JLabel();
        addAnnouncementButton       =   new JButton("Add");
        removeAnnouncementButton    =   new JButton("Remove");
        editAnnouncementButton      =   new JButton("Edit");
        
        addAnnouncementButton.setIcon(new ImageIcon(addSmallImage));
        removeAnnouncementButton.setIcon(new ImageIcon(removeSmallImage));
        editAnnouncementButton.setIcon(new ImageIcon(editSmallImage));
        
        JPanel announcementsWrapper     =   new JPanel();
        JPanel announcementInnerWrapper =   new JPanel();   
        announcementPanel.setBackground(Color.WHITE);
        announcementHeader.setBackground(Color.WHITE);
        announcementsWrapper.setBackground(Color.WHITE);
        announcementControls.setBackground(Color.WHITE);
        announcementInnerWrapper.setBackground(Color.WHITE);
        statusWrapper.setBackground(Color.WHITE);
        modifyPanel.setBackground(Color.WHITE);
        operationLabel.setHorizontalAlignment(JLabel.CENTER);
        operationLabel.setVisible(false);
        
        statusWrapper.add(operationLabel);
        announcementControls.add(addAnnouncementButton);
        announcementControls.add(removeAnnouncementButton);
        announcementControls.add(editAnnouncementButton);

        announcementModel   =   new UpdateListModel();
        announcementList    =   new JList(announcementModel);
        announcementList.setCellRenderer(new AnnouncementCellRenderer());
        announcementList.setPreferredSize(new Dimension(150, 150));
        announcementInnerWrapper.setPreferredSize(new Dimension(150, 150));
        announcementsWrapper.setPreferredSize(new Dimension(150, 150));
        
        JScrollPane announcementScroller    =   new JScrollPane(announcementList);
        announcementScroller.setPreferredSize(new Dimension(390, 280));
        announcementInnerWrapper.add(announcementScroller);
        announcementsWrapper.add(announcementScroller);
        
        
        
        announcementHeader.add(announcementControls);
        announcementHeader.add(statusWrapper);
        announcementHeader.setPreferredSize(new Dimension(1, 90));
        announcementHeader.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 30));
        announcementPanel.add(announcementHeader, BorderLayout.NORTH);
        announcementPanel.add(announcementsWrapper, BorderLayout.CENTER);
        
        announcementViewPanel.add(announcementPanel, ANNOUNCEMENT_VIEW);
        announcementViewPanel.add(modifyPanel, MODIFY_VIEW);
        
        initData();
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
        backButton.addActionListener(this);
        submitButton.addActionListener(this);
    }
    
    protected abstract void showAddAnnouncement();
    
    protected abstract void showRemoveAnnouncement();
    
    protected abstract void showEditAnnouncement();
    
    protected abstract void submitAddAnnouncement();
    
    protected abstract void submitRemoveAnnouncement();
    
    protected abstract void submitEditAnnouncement();
     
    protected abstract JsonArray getData();
    
    protected void initData()
    {
        JsonArray announcements =   getData();
        SwingUtilities.invokeLater(()->
        {
            announcementModel.removeAllElements();

            if(announcements.size() > 0) 
            {
                for(int i = 1; i < announcements.size(); i++)
                    announcementModel.addElement(announcements.get(i).getAsJsonObject());

                announcementList.setSelectedIndex(0);
            }
        });
    }
    
    protected void submit()
    {
        if(context == null) return;
        
        switch(context)
        {
            case ADDING: 
                submitAddAnnouncement();
                break;
            case REMOVING:
                submitRemoveAnnouncement();
                break;
            case EDITING:
                submitEditAnnouncement();
                break;
        }
    }
    
    protected void back()
    {
        showAnnouncementView(ANNOUNCEMENT_VIEW);
    }
    
    
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
    
    protected void showStatusLabel(String message, boolean success)
    {
        operationLabel.setText(message);
        if(success)
            operationLabel.setIcon(new ImageIcon(successImage));
        else
            operationLabel.setIcon(new ImageIcon(failImage));
        
        operationLabel.setVisible(true);
        Timer statusTimer   =   new Timer(2000, (ActionEvent e)->
        {
            operationLabel.setVisible(false);
        });
        
        statusTimer.setRepeats(false);
        statusTimer.start();
    }
    
    protected void showProcessing()
    {
        submitButton.setIcon(spinnerSmall);
        Timer processTimer  =   new Timer(2500, (ActionEvent e)->
        {
            submitButton.setIcon(null);
        });
        
        processTimer.setRepeats(false);
        processTimer.start();
    }
    
    @Override
    public void actionPerformed(ActionEvent e) 
    {
        Object src = e.getSource();
        
        if(src == addAnnouncementButton)
            showAddAnnouncement();
        
        else if(src == editAnnouncementButton)
            showEditAnnouncement();
        
        else if(src == removeAnnouncementButton)
            showRemoveAnnouncement();
        
        else if(src == backButton)
            back();
        
        else if(src == submitButton)
            submit();
    }
    
    protected class AnnouncementCellRenderer implements ListCellRenderer
    {
        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) 
        {
            JsonObject jObj             =   (JsonObject) value;
            JPanel announcementPanel    =   new JPanel(new BorderLayout());
            JPanel announceHeaderPanel  =   new JPanel();
            JPanel announceInfoPanel    =   new JPanel(new GridLayout(1, 3));
            JPanel contentWrapper       =   new JPanel();
            JLabel announcerLabel       =   new JLabel(jObj.get("ANNOUNCER").getAsString());
            JLabel announceDateLabel    =   new JLabel(jObj.get("ANNOUNCE_DATE").getAsString());
            JLabel announcementTitle    =   new JLabel(jObj.get("TITLE").getAsString());
            JTextArea content           =   new JTextArea(jObj.get("CONTENT").getAsString());
            
            announcementPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
            announcementPanel.setBackground(Color.WHITE);
            announceHeaderPanel.setBackground(Color.BLACK);
            content.setBackground(Color.WHITE);
            contentWrapper.setBackground(Color.WHITE);
            announcerLabel.setForeground(Color.WHITE);
            announceDateLabel.setForeground(Color.WHITE);
            announceInfoPanel.setBackground(Color.BLACK);
            announcementTitle.setForeground(Color.WHITE);
            
            announceInfoPanel.add(announcerLabel);
            announceInfoPanel.add(announcementTitle);
            announceInfoPanel.add(announceDateLabel);
            announceHeaderPanel.add(announceInfoPanel, BorderLayout.WEST);
            announcerLabel.setHorizontalAlignment(JLabel.CENTER);
            announcementTitle.setHorizontalAlignment(JLabel.CENTER);
            announceDateLabel.setHorizontalAlignment(JLabel.CENTER);
            
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
    
    protected JsonObject getSelectedValue()
    {
        return (JsonObject) announcementList.getSelectedValue();
    }
    
    protected class ModifyAnnouncementView extends JPanel
    {
        protected JTextField announcementTitle;
        protected JTextArea announcementContent;
        protected JLabel header;
        
        public ModifyAnnouncementView()
        {
            setLayout(new BorderLayout());
            setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
            JPanel titleWrapper     =   new JPanel();
            JPanel innerWrapper     =   new JPanel(new BorderLayout());
            JPanel headerWrapper    =   new JPanel(new FlowLayout(FlowLayout.LEFT));
            JPanel footerWrapper    =   new JPanel();
            announcementTitle       =   new JTextField();
            announcementContent     =   new JTextArea();
            header                  =   new JLabel("Add announcement");
            submitButton            =   new JButton("Submit");
            backButton                    =   new JButton("Back");
            header.setIcon(new ImageIcon(addSmallImage));
            
            headerWrapper.add(header);
            footerWrapper.add(backButton);
            footerWrapper.add(submitButton);
            
            announcementTitle.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.LIGHT_GRAY));
            announcementContent.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.LIGHT_GRAY));
            titleWrapper.setPreferredSize(new Dimension(1, 45));
            titleWrapper.add(announcementTitle);
            announcementTitle.setPreferredSize(new Dimension(404, 35));
            
            setBackground(Color.WHITE);
            announcementContent.setBackground(Color.WHITE);
            titleWrapper.setBackground(Color.WHITE);
            headerWrapper.setBackground(Color.WHITE);
            footerWrapper.setBackground(Color.WHITE);
            
            PromptSupport.setPrompt(" Enter your announcement title", announcementTitle);
            
            innerWrapper.add(titleWrapper, BorderLayout.NORTH);
            innerWrapper.add(announcementContent, BorderLayout.CENTER);
            add(headerWrapper, BorderLayout.NORTH);
            add(innerWrapper, BorderLayout.CENTER);
            add(footerWrapper, BorderLayout.SOUTH);
        }
        
        protected void fill(String title, String content)
        {
            announcementTitle.setText(title);
            announcementContent.setText(content);
        }
        
        protected void clear()
        {
            announcementTitle.setText("");
            announcementContent.setText("");
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
