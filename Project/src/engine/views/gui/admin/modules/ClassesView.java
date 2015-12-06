//====================================
//	Kyle Russell
//	StudentCore
//	ClassesView
//====================================

package engine.views.gui.admin.modules;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import engine.controllers.ControllerMessage;
import engine.core.RouteHandler;
import engine.models.ClassEnrolmentModel;
import engine.models.ClassesModel;
import engine.models.DepartmentModel;
import engine.views.GUIView;
import engine.views.ResponseDataView;
import engine.views.gui.layout.Layout;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

public class ClassesView extends GUIView implements ActionListener
{
    private final String ENROLMENT_VIEW =   "enrol_v";
    private final String DATA_VIEW      =   "data_v";
    private JPanel header;
    private JButton backButton;
    private ClassesDataView dataView;
    private ClassEnrolmentView enrolmentView;
    private JPanel enrolmentWrapper;
    private JLabel enrolmentLabel;
    private BufferedImage backImage, enrolmentImage;
    
    
    @Override
    protected void initComponents() 
    {
        panel               =   new JPanel(new CardLayout());
        header              =   new JPanel(new FlowLayout(FlowLayout.LEFT));
        backButton          =   new JButton("Back");
        enrolmentLabel      =   new JLabel();   
        dataView            =   new ClassesDataView();
        enrolmentView       =   new ClassEnrolmentView(-1);
        enrolmentWrapper    =   new JPanel(new BorderLayout());
        
        Layout.makeTransparent(backButton);
        backButton.setIcon(new ImageIcon(backImage));
        
        header.add(backButton);
        header.add(enrolmentLabel);
        
        enrolmentWrapper.add(header, BorderLayout.NORTH);
        enrolmentWrapper.add(enrolmentView.getPanel(), BorderLayout.CENTER);
        
        panel.add(dataView.getPanel(), DATA_VIEW);
        panel.add(enrolmentWrapper, ENROLMENT_VIEW);
        
        enrolmentWrapper.setBackground(Color.WHITE);
        panel.setBackground(Color.WHITE);
        header.setBackground(Color.WHITE);
    }

    @Override
    protected void initResources()
    {
        try
        {
            backImage       =   ImageIO.read(new File(Layout.getImage("back_icon.png")));
            enrolmentImage  =   ImageIO.read(new File(Layout.getImage("enrolment_icon.png")));
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
    }
    
    protected void showView(String viewName)
    {
        CardLayout cLayout  =   (CardLayout) panel.getLayout();
        cLayout.show(panel, viewName);
    }
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        Object src = e.getSource();
        if(src == backButton)
            showView(DATA_VIEW);
    }
    
    private class ClassEnrolmentView extends DataModuleView
    {
        private int classID;
        
        public ClassEnrolmentView(int classID)
        {
            super();
            this.classID    =   classID;
        }
        
        protected void setClassID(int classID)
        {
            this.classID    =   classID;
        }
        
        protected int getClassID()
        {
            return classID;
        }
        
        
        @Override
        protected void initComponents()
        {
            super.initComponents();
            edit.setVisible(false);
            add.setText("Enrol");
        }

        @Override
        protected JsonArray getData() 
        {
            if(classID == -1)
                return new JsonArray();
            else
            {
                JsonArray data = ClassEnrolmentModel.getStudentsEnrolledIn(classID);
                return data;
            }
        }

        @Override
        protected void add()
        {
            AddEnrolDialog dialog   =   new AddEnrolDialog();
            int option              =   JOptionPane.showConfirmDialog(null, dialog, "Enrol student", JOptionPane.OK_CANCEL_OPTION);
            if(option == JOptionPane.OK_OPTION)
            {
                ControllerMessage postData  =   new ControllerMessage();
                postData.add("userID", dialog.userField.getText());
                postData.add("classID", classID);
                postData.add("semester", dialog.semesterField.getSelectedItem());
                
                ResponseDataView response   =   (ResponseDataView) RouteHandler.go("postAddEnrolment", postData);
                if(response != null)
                {
                    showResponseLabel(response.getRawResponseMessage(), response.getResponseStatus());
                    
                    if(response.getResponseStatus())
                        loadData();
                }
            }
        }

        @Override
        protected void remove() 
        {
            int row =   dataTable.getSelectedRow();
            if(row == -1)
                JOptionPane.showMessageDialog(null, "Please select an enrolment to remove");
            else
            {
                ControllerMessage postData  =   new ControllerMessage();
                postData.add("enrolID", dataTable.getValueAt(row, 0));
                
                ResponseDataView response   =   (ResponseDataView) RouteHandler.go("postRemoveEnrolment", postData);
                showResponseLabel(response.getRawResponseMessage(), response.getResponseStatus());

                if(response.getResponseStatus())
                    loadData();
            }
        }

        @Override
        protected void edit() {}

        @Override
        protected void initColumns() 
        {
            columnHeaders   =   new String[] { "ID", "Username", "Class", "Semester" };   
            columnNames     =   new String[] { "enrol_id", "User ID", "class_name", "semester" };
        }
        
        private class AddEnrolDialog extends JPanel
        {
            protected JTextField userField;
            protected JComboBox semesterField;
            protected int[] classes;
            
            public AddEnrolDialog()
            {
                setLayout(new GridLayout(2, 2));
                userField       =   new JTextField();
                semesterField   =   new JComboBox();
                
                semesterField.addItem(1);
                semesterField.addItem(2);
                
                add(new JLabel("Student username"));
                add(userField);
                add(new JLabel("Semester"));
                add(semesterField);
            }
        }
        
    }
    
    private class ClassesDataView extends DataModuleView
    {
        private JButton enrolmentsButton;

        @Override
        protected void initComponents()
        {
            super.initComponents();
            enrolmentsButton    =   new JButton("Enrolments");
            enrolmentsButton.setIcon(new ImageIcon(enrolmentImage));
            dataControls.add(enrolmentsButton);
        }

        @Override
        protected JsonArray getData()
        {
            return ClassesModel.getAllClasses();
        }

        @Override
        protected void add() 
        {
            AddClassDialog dialog   =   new AddClassDialog();

            int option  =   JOptionPane.showConfirmDialog(null, dialog, "Add class", JOptionPane.OK_CANCEL_OPTION);
            if(option == JOptionPane.OK_OPTION)
            {
                ControllerMessage postData  =   new ControllerMessage();
                postData.add("className", dialog.className.getText());
                postData.add("classDescription", dialog.classDesc.getText());
                postData.add("teacherID", dialog.teacherID.getText());
                postData.add("deptID", ((DeptWrapper)dialog.departmentID.getSelectedItem()).getDeptID());

                ResponseDataView response   =   (ResponseDataView) RouteHandler.go("postAddClass", postData);
                showResponseLabel(response.getRawResponseMessage(), response.getResponseStatus());

                if(response.getResponseStatus())
                    loadData();
            }
        }

        @Override
        protected void remove()
        {
            int selectedRow =   dataTable.getSelectedRow();
            if(selectedRow == -1)
                JOptionPane.showMessageDialog(null, "Please select a class to remove");
            else
            {
                int option    =   JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this class?", 
                                  "Confirm class removal", JOptionPane.YES_NO_OPTION);
                if(option == JOptionPane.YES_OPTION)
                {
                    ControllerMessage postData  =   new ControllerMessage();
                    postData.add("classID", dataTable.getValueAt(selectedRow, 0));

                    ResponseDataView response   =   (ResponseDataView) RouteHandler.go("postRemoveClass", postData);
                    showResponseLabel(response.getRawResponseMessage(), response.getResponseStatus());

                    if(response.getResponseStatus())
                        loadData();
                }
            }
        }

        @Override
        protected void edit()
        {
            AddClassDialog dialog   =   new AddClassDialog();
            int selectedRow         =   dataTable.getSelectedRow();
            if(selectedRow == -1)
                JOptionPane.showMessageDialog(null, "Please select a class to edit");
            else
            {
                dialog.departmentID.setSelectedItem(dataTable.getValueAt(selectedRow, 4));
                dialog.classDesc.setText((String) dataTable.getValueAt(selectedRow, 2));
                dialog.className.setText((String) dataTable.getValueAt(selectedRow, 1));
                dialog.teacherID.setText((String) dataTable.getValueAt(selectedRow, 3));

                int option  =   JOptionPane.showConfirmDialog(null, dialog, "Add class", JOptionPane.OK_CANCEL_OPTION);
                if(option == JOptionPane.OK_OPTION)
                {
                    ControllerMessage postData  =   new ControllerMessage();
                    postData.add("classID", dataTable.getValueAt(selectedRow, 0));
                    postData.add("className", dialog.className.getText());
                    postData.add("classDescription", dialog.classDesc.getText());
                    postData.add("teacherID", dialog.teacherID.getText());
                    postData.add("deptID", ((DeptWrapper)dialog.departmentID.getSelectedItem()).getDeptID());

                    ResponseDataView response   =   (ResponseDataView) RouteHandler.go("postEditClass", postData);
                    showResponseLabel(response.getRawResponseMessage(), response.getResponseStatus());

                    if(response.getResponseStatus())
                        loadData();
                }
            }
        }
        
        private void enrol()
        {
            int selectedRow =   dataTable.getSelectedRow();
            if(selectedRow == -1)
                JOptionPane.showMessageDialog(null, "Please select a class to view enrolments");
            else
            {
                int classID         =   Integer.parseInt(dataTable.getValueAt(selectedRow, 0).toString());
                String className    =   (String) dataTable.getValueAt(selectedRow, 1);
                enrolmentView.setClassID(classID);
                enrolmentView.loadData();
                enrolmentLabel.setText(className + " enrolments");
                showView(ENROLMENT_VIEW);
            }
        }
        
        @Override
        protected void initListeners()
        {
            super.initListeners();
            enrolmentsButton.addActionListener(this);
        }
        
        @Override
        public void actionPerformed(ActionEvent e)
        {
            super.actionPerformed(e);
            Object src = e.getSource();
            
            if(src == enrolmentsButton)
                enrol();
        }

        @Override
        protected void initColumns()
        {
            columnHeaders   =   new String[] { "ID", "Name", "Description", "Teacher", "Dept." };   
            columnNames     =   new String[] { "ID", "Name", "Description", "Teacher_ID", "Dept_name" };
        }

        private class DeptWrapper 
        {
            private String deptName;
            private int deptID;

            public DeptWrapper(String deptName, int deptID)
            {
                this.deptName   =   deptName;
                this.deptID     =   deptID;
            }

            public String getDeptName()
            {
                return deptName;
            }

            public int getDeptID()
            {
                return deptID;
            }

            @Override
            public String toString()
            {
                return deptName;
            }
        }

        private class AddClassDialog extends JPanel
        {
            protected JTextField className;
            protected JTextField classDesc;
            protected JTextField teacherID;
            protected JComboBox departmentID;

            public AddClassDialog()
            {
                setLayout(new GridLayout(4, 2));
                className       =   new JTextField();
                classDesc       =   new JTextField();
                teacherID       =   new JTextField();
                departmentID    =   new JComboBox();

                JsonArray depts =   DepartmentModel.getAllDepartments();
                if(depts.size() > 0)
                {
                    for(int i = 1; i < depts.size(); i++)
                    {
                        JsonObject current  =   depts.get(i).getAsJsonObject();
                        String deptName     =   current.get("NAME").getAsString();
                        int deptID          =   current.get("ID").getAsInt();
                        DeptWrapper wrapper =   new DeptWrapper(deptName, deptID);
                        departmentID.addItem(wrapper);
                    }
                }

                add(new JLabel("Class name "));
                add(className);
                add(new JLabel("Class description "));
                add(classDesc);
                add(new JLabel("Teacher "));
                add(teacherID);
                add(new JLabel("Department "));
                add(departmentID);
            }
        }
    }   
}

