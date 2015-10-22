
package engine.views.gui.admin.modules;

import com.google.gson.JsonArray;
import engine.controllers.ControllerMessage;
import engine.core.RouteHandler;
import engine.models.Role;
import engine.views.ResponseDataView;
import engine.views.gui.admin.AdminControlPanelView;
import engine.views.gui.layout.Layout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class RolesView extends DataModuleView 
{
    private JButton assignRoleButton;
    private BufferedImage assignRoleImage;
    
    @Override
    protected void initComponents()
    {
        super.initComponents();
        assignRoleButton    =   new JButton("Assign");
        
        assignRoleButton.setIcon(new ImageIcon(assignRoleImage));
        dataControls.add(assignRoleButton);
    }
    
    @Override
    protected void initColumns()
    {
        columnHeaders   =   new String[] { "ID", "Name", "Description", "Permission" };
        columnNames     =   new String[] { "ID", "Name", "Description", "Permission_level" };   
    }

    @Override
    protected JsonArray getData() 
    {
        return Role.getRoles();
    }

    @Override
    protected void add()
    {
        AddRoleDialog roleDialog =   new AddRoleDialog();
        int option = JOptionPane.showConfirmDialog(null, roleDialog, "Add role", JOptionPane.OK_CANCEL_OPTION);
        
        if(option == JOptionPane.OK_OPTION)
        {
            String roleName =   roleDialog.roleName.getText();
            String roleDesc =   roleDialog.roleDesc.getText();
            int permLevel   =   (int) roleDialog.permLevel.getSelectedItem();
            
            ControllerMessage postData  =   new ControllerMessage();
            postData.add("roleName", roleName);
            postData.add("roleDesc", roleDesc);
            postData.add("permLevel", permLevel);
           
            ResponseDataView response   =   (ResponseDataView) RouteHandler.go("postAddRole", postData);
            showResponseLabel(response.getRawResponseMessage(), response.getResponseStatus());
            
            if(response.getResponseStatus())
                loadData();
        }
    }

    @Override
    protected void remove() 
    {
        int row = dataTable.getSelectedRow();
        if(row == -1) 
            JOptionPane.showMessageDialog(null, "Please select a role to remove");
        else
        {
            int roleID  =   (int) dataModel.getValueAt(row, 0);
            String role =   (String) dataModel.getValueAt(row, 1);
            int option  =   JOptionPane.showConfirmDialog(null, "Are you sure you want to delete the " + role + " role?", "Remove role", JOptionPane.YES_NO_OPTION);
            
            if(option == JOptionPane.YES_OPTION)
            {
                ControllerMessage postData  =   new ControllerMessage();
                postData.add("roleID", roleID);
                ResponseDataView response   =   (ResponseDataView) RouteHandler.go("postRemoveRole", postData);
                showResponseLabel(response.getRawResponseMessage(), response.getResponseStatus());
                
                if(response.getResponseStatus())
                    dataModel.removeRow(row);
            }
        }
    }

    @Override
    protected void edit() 
    {
        AddRoleDialog roleDialog    =   new AddRoleDialog();
        int selectedRow             =   dataTable.getSelectedRow();
        if(selectedRow == -1)
            JOptionPane.showMessageDialog(null, "Please select a role to edit");
        else
        {
            String currentName          =   (String) dataTable.getValueAt(selectedRow, 1);
            String currentDesc          =   (String) dataTable.getValueAt(selectedRow, 2);
            int currentPerm             =   (int) dataTable.getValueAt(selectedRow, 3);
            roleDialog.roleName.setText(currentName);
            roleDialog.roleDesc.setText(currentDesc);
            roleDialog.permLevel.setSelectedItem(currentPerm);
            
            int option = JOptionPane.showConfirmDialog(null, roleDialog, "Edit role", JOptionPane.OK_CANCEL_OPTION);

            if(option == JOptionPane.OK_OPTION)
            {
                String roleName =   roleDialog.roleName.getText();
                String roleDesc =   roleDialog.roleDesc.getText();
                int permLevel   =   (int) roleDialog.permLevel.getSelectedItem();
                int roleID      =   (int) dataTable.getValueAt(selectedRow, 0);

                ControllerMessage postData  =   new ControllerMessage();
                postData.add("roleName", roleName);
                postData.add("roleDesc", roleDesc);
                postData.add("permLevel", permLevel);
                postData.add("roleID", roleID);
                
                ResponseDataView response   =   (ResponseDataView) RouteHandler.go("postEditRole", postData);
                showResponseLabel(response.getRawResponseMessage(), response.getResponseStatus());
                
                if(response.getResponseStatus())
                    loadData();
            }
        }
    }
    
    private void assignRole()
    {
        AssignRoleDialog assignDialog   =   new AssignRoleDialog();
        int selectedRow                 =   dataTable.getSelectedRow();
        if(selectedRow != -1)
        {
            String selectedRoleName     =   (String) dataTable.getValueAt(selectedRow, 1);
            assignDialog.rolesSelect.setSelectedItem(selectedRoleName);
        }
        
        int option  =   JOptionPane.showConfirmDialog(null, assignDialog, "Assign role to user", JOptionPane.OK_CANCEL_OPTION);
        if(option == JOptionPane.OK_OPTION)
        {
            String username     =   assignDialog.userField.getText();
            int roleID          =   (int) dataTable.getValueAt(assignDialog.rolesSelect.getSelectedIndex(), 0);
            
            ControllerMessage postData  =   new ControllerMessage();
            postData.add("assignTO", username);
            postData.add("roleID", roleID);
            
            ResponseDataView response   =   (ResponseDataView) RouteHandler.go("postAssignRole", postData);
            showResponseLabel(response.getRawResponseMessage(), response.getResponseStatus());
        }
    }
    
    
    @Override
    protected void initListeners()
    {
        super.initListeners();
        assignRoleButton.addActionListener(this);
    }
    
    @Override
    protected void initResources()
    {
        super.initResources();
        try
        {
            assignRoleImage =   ImageIO.read(new File(Layout.getImage("assign_role_icon.png")));
        }
        
        catch(IOException e)
        {
            
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        super.actionPerformed(e);
        Object src = e.getSource();
        
        if(src == assignRoleButton)
            assignRole();
    }
    
    private class AssignRoleDialog extends JPanel
    {
        protected JTextField userField;
        protected JComboBox rolesSelect;
        
        public AssignRoleDialog()
        {
            setLayout(new GridLayout(2, 2));
            userField   =   new JTextField();
            rolesSelect =   new JComboBox();
            
            for(int i = 0; i < dataModel.getRowCount(); i++)
                rolesSelect.addItem(dataModel.getValueAt(i, 1));
            
            add(new JLabel("Username "));
            add(userField);
            add(new JLabel("Role "));
            add(rolesSelect);
        }
    }
    
    private class AddRoleDialog extends JPanel
    {
        protected JTextField roleName;
        protected JTextField roleDesc;
        protected JComboBox permLevel;
        
        public AddRoleDialog()
        {
            setLayout(new GridLayout(3, 2));
            roleName    =   new JTextField();
            roleDesc    =   new JTextField();
            permLevel   =   new JComboBox();
            
            add(new JLabel("Role name "));
            add(roleName);
            add(new JLabel("Role description "));
            add(roleDesc);
            add(new JLabel("PermissionLevel "));
            add(permLevel);
            
            for(int i = 0; i < 10; i++)
                permLevel.addItem(i);
        }
    }
        
}
