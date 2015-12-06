//====================================
//	Kyle Russell
//	StudentCore
//	UsersView
//====================================

package engine.views.gui.admin.modules;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import engine.controllers.ControllerMessage;
import engine.core.RouteHandler;
import engine.models.User;
import engine.views.GUIView;
import engine.views.ResponseDataView;
import engine.views.gui.RegisterView;
import engine.views.gui.layout.Layout;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import org.jdesktop.xswingx.PromptSupport;

public class UsersView extends GUIView implements ActionListener
{
    private final String ADD_VIEW       =   "add_v";
    private final String SEARCH_VIEW    =   "search_v"; 
    private final String DATA_VIEW      =   "data_v";
    private final String ASIDE_VIEW     =   "aside_v";
    private final String EDIT_VIEW      =   "edit_v";
    private BufferedImage backImage;
    private BufferedImage saveImage;
    private JPanel addView;
    private JPanel searchView;
    private JPanel asideView;
    private JPanel subView;
    private SearchView searchContent;
    private AddView addContent;
    private String currentSubView;
    
    private UserDataView dataView;
    private JLabel viewLabel;
    private JPanel innerHeader;
    private JButton backButton;
    private JButton searchButton;
    private static final String[] userColHeaders   =   new String[] { "Username", "Firstname", "Lastname", "Role", "Gender", "Birthdate" };
    private static final String[] userColNames     =   new String[] { "username", "firstname", "lastname", "role_name", "gender", "birthdate" };
    
    @Override
    protected void initComponents()
    {
        panel           =   new JPanel(new CardLayout());
        backButton      =   new JButton();
        searchButton    =   new JButton("Search");
        innerHeader     =   new JPanel(new FlowLayout(FlowLayout.LEFT));
        viewLabel       =   new JLabel();
        searchContent   =   new SearchView();
        subView         =   new JPanel(new CardLayout());
        asideView       =   new JPanel(new BorderLayout());
        addContent      =   new AddView();
        addView         =   new JPanel();
        searchView      =   new JPanel();
        dataView        =   new UserDataView();

        Layout.makeTransparent(backButton);
        backButton.setIcon(new ImageIcon(backImage));
        searchButton.setIcon(new ImageIcon(searchSmallImage));
        innerHeader.add(backButton);
        innerHeader.add(viewLabel);
        
        addView.add(addContent);
        searchView.add(searchContent);
        
        subView.add(addView, ADD_VIEW);
        subView.add(searchView, SEARCH_VIEW);
        
        asideView.add(innerHeader, BorderLayout.NORTH);
        asideView.add(subView, BorderLayout.CENTER);
        
        panel.add(dataView.getPanel(), DATA_VIEW);
        panel.add(asideView, ASIDE_VIEW);
        
        
        panel.setBackground(Color.WHITE);
        addView.setBackground(Color.WHITE);
        searchView.setBackground(Color.WHITE);
        innerHeader.setBackground(Color.WHITE);
        subView.setBackground(Color.WHITE);
        asideView.setBackground(Color.WHITE);
        
        showSubViews(false);
    }

    @Override
    protected void initResources()
    {
        try
        {
            backImage   =   ImageIO.read(new File(Layout.getImage("back_icon.png")));
            saveImage   =   ImageIO.read(new File(Layout.getImage("save_icon.png")));
        }
        
        catch(IOException e)
        {
            JOptionPane.showMessageDialog(null, "Failed to load resources: " + e.getMessage());
        }
    }

    @Override
    protected void initListeners()
    {
        backButton.addActionListener(this);
        searchButton.addActionListener(this);
    }

    private void showUserView(String viewName)
    {
        showSubViews(true);
        CardLayout cLayout  =   (CardLayout) subView.getLayout();
        
        if(viewName.equals(ADD_VIEW))
            viewLabel.setText("Add user");
        
        else if(viewName.equals(SEARCH_VIEW))
            viewLabel.setText("Search for user");
        
        else
            viewLabel.setText("Edit user");
        
        currentSubView  =   viewName;
        cLayout.show(subView, viewName);
    }
    
    private void showSubViews(boolean show)
    {
        CardLayout cLayout  =   (CardLayout) panel.getLayout();
        
        if(show)
            cLayout.show(panel, ASIDE_VIEW);
        else
            cLayout.show(panel, DATA_VIEW);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        Object src  =   e.getSource();
        
        if(src == backButton)
            showSubViews(false);
        
        else if(src == searchButton)
            showUserView(SEARCH_VIEW);
    }
    
    private class UserDataView extends DataModuleView
    {
        @Override
        protected void initComponents()
        {
            super.initComponents();
            dataControls.add(searchButton);
        } 
        
        @Override
        protected JsonArray getData()
        {
            return User.getAllUsers();
        }

        @Override
        protected void add()
        {
            showUserView(ADD_VIEW);
        }
        
        protected void add(ControllerMessage data)
        {
            if(data == null) return;
            
            ResponseDataView response   =   (ResponseDataView) RouteHandler.go("postRegister", data);
            showSubViews(false);
            showResponseLabel(response.getRawResponseMessage(), response.getResponseStatus());
                
            if(response.getResponseStatus())
            {
                loadData();
                addContent.clearForms();
            }
        }
        
        protected void edit(ControllerMessage data)
        {
            if(data == null) return;
            
            ResponseDataView response   =   (ResponseDataView) RouteHandler.go("postEditUser", data);
            showSubViews(false);
            showResponseLabel(response.getRawResponseMessage(), response.getResponseStatus());
            
            if(response.getResponseStatus())
            {
                loadData();
                addContent.clearForms();
            }
        }

        @Override
        protected void remove() 
        {
            int selectedRow =   dataTable.getSelectedRow();
            if(selectedRow == -1)
                JOptionPane.showMessageDialog(null, "Please select a user to remove");
            else
            {
                String userID               =   (String) dataTable.getValueAt(selectedRow, 0);
                ControllerMessage postData  =   new ControllerMessage();
                postData.add("removeUsername", userID);
                
                ResponseDataView response   =   (ResponseDataView) RouteHandler.go("postRemoveStudent", postData);
                showResponseLabel(response.getRawResponseMessage(), response.getResponseStatus());

                if(response.getResponseStatus())
                    loadData();
            }
        }

        @Override
        protected void edit() 
        {
            int selectedRow =   dataTable.getSelectedRow();
            if(selectedRow == -1)
                JOptionPane.showMessageDialog(null, "Please select a to edit");
            else
            {
                JsonArray results   =   User.getUserWithJoins((String) dataTable.getValueAt(selectedRow, 0));
                if(results == null || results.size() < 1)
                    JOptionPane.showMessageDialog(null, "User was not found");
                else
                {
                    addContent.fill(results);
                    showUserView(EDIT_VIEW);
                }
            }
        }

        @Override
        protected void initColumns()
        {
            columnHeaders   =   userColHeaders;
            columnNames     =   userColNames;
        }
    }
    
    private class AddView extends JPanel implements ActionListener
    {
        private JButton saveButton;
        private RegisterView form;
        private JPanel formPanel;
        private JPanel footer;
        
        public AddView()
        {
            form            =   new RegisterView();
            footer          =   new JPanel();
            saveButton      =   new JButton("Save");
            formPanel       =   new JPanel(new GridLayout(15, 1, 0, 10));
            footer.add(saveButton);
            form.getMedicalFormPanel().setVisible(true);
            
            saveButton.addActionListener(this);
            saveButton.setIcon(new ImageIcon(saveImage));
            
            formPanel.add(form.getUsernameField());
            formPanel.add(form.getPasswordField());
            formPanel.add(form.getFirstnameField());
            formPanel.add(form.getLastnameField());
            formPanel.add(form.getPhoneField());
            formPanel.add(form.getEmailField());
            formPanel.add(form.getBirthdateField());
            formPanel.add(form.getGenderField());
            formPanel.add(form.getEthnicityField());
            formPanel.add(form.getContactFirstname());
            formPanel.add(form.getContactLastname());
            formPanel.add(form.getContactPhone());
            formPanel.add(form.getContactEmail());
            formPanel.add(form.getContactRelationship());
            formPanel.add(form.getMedicalDescription());
            
            JPanel wrapper  =   new JPanel(new BorderLayout());
            
            wrapper.setBackground(Color.WHITE);
            setBackground(Color.WHITE);
            footer.setBackground(Color.WHITE);
            formPanel.setBackground(Color.WHITE);
            
            formPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
            JScrollPane scroller    =   new JScrollPane(formPanel);
            scroller.setPreferredSize(new Dimension(390, 1200));
            wrapper.setPreferredSize(new Dimension(390, 330));
            scroller.setBackground(Color.WHITE);
            
            wrapper.add(scroller, BorderLayout.CENTER);
            wrapper.add(footer, BorderLayout.SOUTH);
            add(wrapper);
        }
        
        private void save()
        {
            ControllerMessage postData  =   new ControllerMessage().addAll(form.getAccDetails()).addAll(form.getContactDetails());
            
            if(currentSubView.equals(EDIT_VIEW))
                dataView.edit(postData);
            
            else if(currentSubView.equals(ADD_VIEW))
                dataView.add(postData);
        }
        
        protected void clearForms()
        {
            Component[] components = formPanel.getComponents();
            for(Component component : components)
            {
                if(component instanceof JTextField)
                    ((JTextField) component).setText("");
                
                else if(component instanceof JComboBox)
                    ((JComboBox) component).setSelectedIndex(0);
            }
        }
        
        protected void fill(JsonArray data)
        {
            SwingUtilities.invokeLater(()->
            {
                JsonObject current  =   data.get(1).getAsJsonObject();
                form.getUsernameField().setText(current.get("USERNAME").getAsString());
                form.getPasswordField().setText(current.get("PASSWORD").getAsString());
                form.getFirstnameField().setText(current.get("FIRSTNAME").getAsString());
                form.getLastnameField().setText(current.get("LASTNAME").getAsString());
                form.getPhoneField().setText(current.get("CONTACT_PH").getAsString());
                form.getEmailField().setText(current.get("CONTACT_EMAIL").getAsString());
                form.getBirthdateField().setText(current.get("BIRTHDATE").getAsString());
                form.getGenderField().setSelectedItem(current.get("GENDER").getAsString());
                form.getEthnicityField().setSelectedItem(current.get("ETHNICITY").getAsString());
                form.getContactFirstname().setText(current.get("CONTACT_FIRSTNAME").getAsString());
                form.getContactLastname().setText(current.get("CONTACT_LASTNAME").getAsString());
                form.getContactPhone().setText(current.get("E_CONTACT_PH").getAsString());
                form.getContactEmail().setText(current.get("E_CONTACT_EMAIL").getAsString());
                form.getContactRelationship().setText(current.get("E_CONTACT_RELATIONSHIP").getAsString());
                form.getMedicalDescription().setText(current.get("MEDICAL_DESC").getAsString()); 
            });
        }
        
        @Override
        public void actionPerformed(ActionEvent e) 
        {
            Object src  =   e.getSource();
            if(src == saveButton)
                save();
                
        }
    }
    
    private class SearchView extends JPanel implements ActionListener
    {
        private JTextField searchBar;
        private JButton searchButton;
        private JTable searchTable;
        private DefaultTableModel searchModel;
        private JComboBox columnFilter;
        private JComboBox operatorFilter;
        private JLabel resultsLabel;
        
        public SearchView()
        {
            setLayout(new BorderLayout());
            setBackground(Color.WHITE);
            searchBar       =   new JTextField();
            searchButton    =   new JButton();
            searchModel     =   new DefaultTableModel();
            searchTable     =   new JTable(searchModel);
            columnFilter    =   new JComboBox();
            operatorFilter  =   new JComboBox();
            resultsLabel    =   new JLabel();
            
            searchButton.addActionListener(this);
            PromptSupport.setPrompt("Search...", searchBar);
            
            JPanel searchWrapper        =   new JPanel(new BorderLayout());
            JPanel searchFilterWrapper  =   new JPanel(new GridLayout(1, 2));   
            JPanel searchPanel          =   new JPanel(new GridLayout(2, 1));
            JPanel searchResultsWrapper =   new JPanel();
            JPanel searchBarWrapper     =   new JPanel(new BorderLayout());
            
            Layout.makeTransparent(searchButton);
            searchButton.setIcon(new ImageIcon(searchSmallImage));
            searchButton.setPreferredSize(new Dimension(25, 1));
            
            searchWrapper.setBackground(Color.WHITE);
            searchFilterWrapper.setBackground(Color.WHITE);
            searchResultsWrapper.setBackground(Color.WHITE);
            searchPanel.setBackground(Color.WHITE);
            searchBar.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.LIGHT_GRAY));
            searchBarWrapper.setPreferredSize(new Dimension(200, 1));
            searchBarWrapper.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
            
            initSearchFilters();
            initModel();
            searchBarWrapper.add(searchBar, BorderLayout.CENTER);
            searchBarWrapper.add(searchButton, BorderLayout.EAST);
            searchFilterWrapper.add(operatorFilter);
            searchFilterWrapper.add(columnFilter);
            searchWrapper.add(searchBarWrapper, BorderLayout.EAST);
            searchWrapper.add(searchFilterWrapper, BorderLayout.CENTER);
            
            searchResultsWrapper.add(resultsLabel);
            searchPanel.add(searchWrapper);
            searchPanel.add(searchResultsWrapper);
            
            JPanel tableWrapper     =   new JPanel();
            tableWrapper.setBackground(Color.WHITE);
            searchTable.setBackground(Color.WHITE);
            searchBarWrapper.setBackground(Color.WHITE);
            JScrollPane scroller    =   new JScrollPane(searchTable);
            tableWrapper.add(scroller);
            tableWrapper.setPreferredSize(new Dimension(390, 280));
            scroller.setPreferredSize(new Dimension(390, 280));
                    
            add(searchPanel, BorderLayout.NORTH);
            add(tableWrapper, BorderLayout.CENTER);
        }
        
        private void initSearchFilters()
        {
            for(String col : userColNames)
                columnFilter.addItem(col);
            
            operatorFilter.addItem("=");
            operatorFilter.addItem(">");
            operatorFilter.addItem("<");
        }
        
        private void initModel()
        {
            DataModuleView.setColumns(userColHeaders, searchTable, searchModel);
        }
        
        private void searchUsers()
        {
            String searchTerm   =   searchBar.getText();
            if(searchTerm.equals(""))
            {
                JOptionPane.showMessageDialog(null, "Please enter a search term");
                return;
            }
            
            String operator     =   (String) operatorFilter.getSelectedItem();
            String col          =   userColNames[columnFilter.getSelectedIndex()];
            
            JsonArray results   =   User.searchUser(col, operator, searchTerm);
            
            SwingUtilities.invokeLater(()->
            {
                searchModel.setRowCount(0);
                for(int i = 1; i < results.size(); i++)
                {
                    JsonObject current  =   results.get(i).getAsJsonObject();
                    Object[] row        =   DataModuleView.getDataFromResults(current, userColNames);
                    searchModel.addRow(row);
                }
                
                if(results.size() > 0)
                    resultsLabel.setText("Found " + (results.size() - 1) + " results");
            });
        }

        @Override
        public void actionPerformed(ActionEvent e) 
        {
            Object src  =   e.getSource();
            
            if(src == searchButton)
                searchUsers();
        }
        
    }
}
