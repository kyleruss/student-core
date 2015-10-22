
package engine.views.gui.admin.modules;

import com.google.gson.JsonArray;
import engine.controllers.ControllerMessage;
import engine.core.RouteHandler;
import engine.models.DepartmentModel;
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


public class DeptView extends DataModuleView 
{
    private JButton assignDeptHeadButton;
    private BufferedImage assignDeptImage;
    
    @Override
    protected void initComponents()
    {
        super.initComponents();
        assignDeptHeadButton    =   new JButton("Assign HOD");
        
        assignDeptHeadButton.setIcon(new ImageIcon(assignDeptImage));
        dataControls.add(assignDeptHeadButton);
    }
    
    @Override
    protected void initColumns()
    {
        columnHeaders   =   new String[] { "ID", "Name", "Description", "HOD" };
        columnNames     =   new String[] { "ID", "Name", "Description", "Dept_head" };   
    }

    @Override
    protected JsonArray getData() 
    {
        return DepartmentModel.getAllDepartments();
    }

    @Override
    protected void add()
    {
        AddDeptDialog dialog    =   new AddDeptDialog();
        int option              =   JOptionPane.showConfirmDialog(null, dialog, "Add department", JOptionPane.OK_CANCEL_OPTION);
        
        if(option == JOptionPane.OK_OPTION)
        {
            String deptName =   dialog.deptName.getText();
            String deptDesc =   dialog.deptDesc.getText();
            String deptHOD  =   dialog.deptHead.getText();
            
            ControllerMessage postData  =   new ControllerMessage();
            postData.add("deptName", deptName);
            postData.add("deptDesc", deptDesc);
            postData.add("deptHOD", deptHOD);
           
            ResponseDataView response   =   (ResponseDataView) RouteHandler.go("postAddDepartment", postData);
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
            JOptionPane.showMessageDialog(null, "Please select a department to remove");
        else
        {
            Object deptID   =   dataModel.getValueAt(row, 0);
            Object dept     =   dataModel.getValueAt(row, 1);
            int option  =   JOptionPane.showConfirmDialog(null, "Are you sure you want to delete the " + dept + " department?", 
                            "Remove department", JOptionPane.YES_NO_OPTION);
            
            if(option == JOptionPane.YES_OPTION)
            {
                ControllerMessage postData  =   new ControllerMessage();
                postData.add("deptID", deptID);
                ResponseDataView response   =   (ResponseDataView) RouteHandler.go("postRemoveDepartment", postData);
                showResponseLabel(response.getRawResponseMessage(), response.getResponseStatus());
                
                if(response.getResponseStatus())
                    dataModel.removeRow(row);
            }
        }
    }

    @Override
    protected void edit() 
    {
        AddDeptDialog dialog    =   new AddDeptDialog();
        int selectedRow         =   dataTable.getSelectedRow();
        if(selectedRow == -1)
            JOptionPane.showMessageDialog(null, "Please select a department to edit");
        else
        {
            String currentName          =   (String) dataTable.getValueAt(selectedRow, 1);
            String currentDesc          =   (String) dataTable.getValueAt(selectedRow, 2);
            String currentHOD           =   (String) dataTable.getValueAt(selectedRow, 3);
            
            dialog.deptName.setText(currentName);
            dialog.deptDesc.setText(currentDesc);
            dialog.deptHead.setText(currentHOD);
            
            int option = JOptionPane.showConfirmDialog(null, dialog, "Edit department", JOptionPane.OK_CANCEL_OPTION);

            if(option == JOptionPane.OK_OPTION)
            {
                Object deptName     =   dialog.deptName.getText();
                Object deptDesc     =   dialog.deptDesc.getText();
                Object deptHOD      =   dialog.deptHead.getText();
                Object deptID       =   dataTable.getValueAt(selectedRow, 0);

                ControllerMessage postData  =   new ControllerMessage();
                postData.add("deptName", deptName);
                postData.add("deptDesc", deptDesc);
                postData.add("deptHOD", deptHOD);
                postData.add("deptID", deptID);
                
                ResponseDataView response   =   (ResponseDataView) RouteHandler.go("postEditDepartment", postData);
                showResponseLabel(response.getRawResponseMessage(), response.getResponseStatus());
                
                if(response.getResponseStatus())
                    loadData();
            }
        }
    }
    
    private void assignHOD()
    {
        AssignHODDialog dialog   =   new AssignHODDialog();
        int selectedRow          =   dataTable.getSelectedRow();
        if(selectedRow != -1)
        {
            String selectedDept     =   (String) dataTable.getValueAt(selectedRow, 1);
            dialog.deptSelect.setSelectedItem(selectedDept);
        }
        
        int option  =   JOptionPane.showConfirmDialog(null, dialog, "Assign HOD to department", JOptionPane.OK_CANCEL_OPTION);
        if(option == JOptionPane.OK_OPTION)
        {
            ControllerMessage postData  =   new ControllerMessage();
            postData.add("deptHOD", dialog.userField.getText());
            postData.add("deptName", dataTable.getValueAt(selectedRow, 1));
            postData.add("deptDesc", dataTable.getValueAt(selectedRow, 2));
            postData.add("deptID", dataTable.getValueAt(dialog.deptSelect.getSelectedIndex(), 0));
            
            ResponseDataView response   =   (ResponseDataView) RouteHandler.go("postEditDepartment", postData);
            showResponseLabel(response.getRawResponseMessage(), response.getResponseStatus());
            
            if(response.getResponseStatus())
                loadData();
        }
    }
    
    
    @Override
    protected void initListeners()
    {
        super.initListeners();
        assignDeptHeadButton.addActionListener(this);
    }
    
    @Override
    protected void initResources()
    {
        super.initResources();
        try
        {
            assignDeptImage =   ImageIO.read(new File(Layout.getImage("assign_role_icon.png")));
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
        
        if(src == assignDeptHeadButton)
            assignHOD();
    }
    
    private class AssignHODDialog extends JPanel
    {
        protected JTextField userField;
        protected JComboBox deptSelect;
        
        public AssignHODDialog()
        {
            setLayout(new GridLayout(2, 2));
            userField   =   new JTextField();
            deptSelect   =   new JComboBox();
            
            for(int i = 0; i < dataModel.getRowCount(); i++)
                deptSelect.addItem(dataModel.getValueAt(i, 1));
            
            add(new JLabel("Head of Department "));
            add(userField);
            add(new JLabel("Department "));
            add(deptSelect);
        }
    }
    
    private class AddDeptDialog extends JPanel
    {
        protected JTextField deptName;
        protected JTextField deptDesc;
        protected JTextField deptHead;
        
        public AddDeptDialog()
        {
            setLayout(new GridLayout(3, 2));
            deptName    =   new JTextField();
            deptDesc    =   new JTextField();
            deptHead    =   new JTextField();
            
            add(new JLabel("Department name "));
            add(deptName);
            add(new JLabel("Department description "));
            add(deptDesc);
            add(new JLabel("Head of Department "));
            add(deptHead);
        }
    }
        
}
