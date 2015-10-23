
package engine.views.gui;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import engine.config.AppConfig;
import engine.controllers.ControllerMessage;
import engine.core.Agent;
import engine.core.RouteHandler;
import engine.core.database.Column;
import engine.models.AssessmentModel;
import engine.models.AssessmentSubmissionsModel;
import engine.models.ClassAnnouncementsModel;
import engine.models.ClassEnrolmentModel;
import engine.models.ClassesModel;
import engine.models.DeptAnnouncementsModel;
import engine.views.GUIView;
import engine.views.ResponseDataView;
import engine.views.gui.admin.modules.DataModuleView;
import engine.views.gui.admin.modules.NoticesView;
import engine.views.gui.layout.Layout;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
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


public class ClassPageView extends GUIView implements ActionListener
{
    int classID;
    String className;
    String teacherUsername;
    
    private final String STUDENT_VIEW   =   "student_v";
    private final String DETAILS_VIEW   =   "details_v";
    private final String ASSESS_VIEW    =   "assessments_v";
    private final String NOTICE_VIEW    =   "notice_v";
    private BufferedImage backgroundImage;
    private BufferedImage backImage;
    private JPanel leftPane;
    private JPanel rightPane;
    private JPanel pageControls;
    private JPanel classPagePanel;
    private JPanel viewPanel;
    private DetailsView detailsView;
    private AssessmentsView assessmentsView;
    private StudentsView studentsView;
    private ClassNoticeView noticeView;
    private JButton goDetailsButton;
    private JButton goAssessButton;
    private JButton goStudentsButton;
    private JButton goNoticesButton;
    
    public ClassPageView()
    {
        super();
    }
    
    public ClassPageView(ControllerMessage data)
    {
        super(data);
    }
    
    private void initPassedData()
    {
        JsonArray jsonData  =   messages.getData();
        if(jsonData != null && jsonData.size() > 1)
        {
            JsonObject dataObj  =   jsonData.get(1).getAsJsonObject();
            classID         =   dataObj.get("Class ID").getAsInt();
            className       =   dataObj.get("Class name").getAsString();
            teacherUsername =   dataObj.get("Teacher ID").getAsString();
        }
    }
    
    @Override
    protected void initComponents() 
    {
        panel   =   new JPanel()
        {
            @Override
            public void paintComponent(Graphics g)
            {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
            }
        };
        
        initPassedData();
        try
        {
        classPagePanel      =   new JPanel(new BorderLayout());
        leftPane            =   new JPanel();
        rightPane           =   new JPanel();
        pageControls        =   new JPanel();
        viewPanel           =   new JPanel(new CardLayout());
        detailsView         =   new DetailsView();
        assessmentsView     =   new AssessmentsView();
        studentsView        =   new StudentsView();
        noticeView          =   new ClassNoticeView();
        goDetailsButton     =   new JButton("Details");
        goAssessButton      =   new JButton("Assessments");
        goStudentsButton    =   new JButton("Students");
        goNoticesButton     =   new JButton("Notices");
        
        viewPanel.add(detailsView, DETAILS_VIEW);
        viewPanel.add(assessmentsView, ASSESS_VIEW);
        viewPanel.add(studentsView, STUDENT_VIEW);
        viewPanel.add(noticeView.getAnnouncementViewPanel(), NOTICE_VIEW);
        }
        
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
        JPanel controlWrapper   =   new JPanel(new GridLayout(4, 1));
        controlWrapper.setPreferredSize(new Dimension(230, 200));
        controlWrapper.add(goNoticesButton);
        controlWrapper.add(goDetailsButton);
        controlWrapper.add(goStudentsButton);
        controlWrapper.add(goAssessButton);
        
        goNoticesButton.setForeground(Color.WHITE);
        goDetailsButton.setForeground(Color.WHITE);
        goAssessButton.setForeground(Color.WHITE);
        goStudentsButton.setForeground(Color.WHITE);
        classPagePanel.setBackground(Color.WHITE);
        leftPane.setBackground(new Color(50, 50, 62));
        rightPane.setBackground(Color.WHITE);
        pageControls.setBackground(new Color(50, 50, 62));
        viewPanel.setBackground(Color.WHITE);
        controlWrapper.setBackground(new Color(50, 50, 62));
        
        classPagePanel.setPreferredSize(new Dimension(600, 400));
        leftPane.setPreferredSize(new Dimension(120, 1));
        rightPane.add(viewPanel);
        leftPane.add(controlWrapper);
        
        classPagePanel.add(leftPane, BorderLayout.WEST);
        classPagePanel.add(rightPane, BorderLayout.CENTER);

        showView(NOTICE_VIEW);
        
        panel.add(Box.createRigidArea(new Dimension(AppConfig.WINDOW_WIDTH, 30)));
        panel.add(classPagePanel);
        
    }

    private void showView(String viewName)
    {
        CardLayout cLayout  =   (CardLayout) viewPanel.getLayout();
        cLayout.show(viewPanel, viewName);
    }
    
    @Override
    protected void initResources()
    {
        try
        {
            backgroundImage =   ImageIO.read(new File(Layout.getImage("blurredbackground25.jpg")));
            backImage       =   ImageIO.read(new File(Layout.getImage("back_icon.png")));
        }
        
        catch(IOException e)
        {
            JOptionPane.showMessageDialog(null, "Failed to load resources: " + e.getMessage());
        }
    }

    @Override
    protected void initListeners()
    {
        goDetailsButton.addActionListener(this);
        goStudentsButton.addActionListener(this);
        goAssessButton.addActionListener(this);
        goNoticesButton.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) 
    {
        Object src  =   e.getSource();
        
        if(src == goDetailsButton)
            showView(DETAILS_VIEW);
        
        else if(src == goStudentsButton)
            showView(STUDENT_VIEW);
        
        else if(src == goAssessButton)
            showView(ASSESS_VIEW);
        
        else if(src == goNoticesButton)
            showView(NOTICE_VIEW);
    }
    
    private class StudentsView extends JPanel
    {
        private JLabel numStudentsLabel;
        private JTable studentTable;
        private DefaultTableModel studentModel;
        
        public StudentsView()
        {
            setLayout(new BorderLayout());
            setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));
            studentModel            =   new DefaultTableModel();
            studentTable            =   new JTable(studentModel);
            numStudentsLabel        =   new JLabel();
            
            JPanel tableWrapper     =   new JPanel();
            JPanel headerWrapper    =   new JPanel();
            
            headerWrapper.add(numStudentsLabel);
            JScrollPane scroller    =   new JScrollPane(studentTable);
            scroller.setPreferredSize(new Dimension(450, 300));
            tableWrapper.add(scroller);
            
            numStudentsLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            numStudentsLabel.setForeground(Color.DARK_GRAY);
            tableWrapper.setBackground(Color.WHITE);
            scroller.setBackground(Color.WHITE);
            studentTable.setBackground(Color.WHITE);
            setBackground(Color.WHITE);
            headerWrapper.setBackground(Color.WHITE);
            
            
            add(headerWrapper, BorderLayout.NORTH);
            add(tableWrapper, BorderLayout.CENTER);
            initData();
        }
        
        private void initData()
        {
            JsonArray data  =   ClassEnrolmentModel.getStudentsEnrolledIn(classID);
            if(data != null && data.size() > 1)
            {
                String numStudentsMessage   =   MessageFormat.format("There is {0} student(s) in this class", (data.size() - 1));
                numStudentsLabel.setText(numStudentsMessage);
                DataModuleView.setColumns(new String[] { "Username", "Firstname", "Lastname", "Email" }, studentTable, studentModel);
                
                SwingUtilities.invokeLater(()->
                {
                    for(int i = 1; i < data.size(); i++)
                    {
                        JsonObject current  =   data.get(i).getAsJsonObject();
                        Object[] row        =   DataModuleView.getDataFromResults(current, new String[] { "User ID", "First name", "Last name", "Email" });
                        studentModel.addRow(row);
                    }
                });
            }
        }
    }
    
    private class AssessmentsView extends JPanel
    {
        private final String ASSESS_LIST_VIEW       =   "assess_list_v";
        private final String ASSESS_SUB_VIEW        =   "assess_sub_v";
        private JPanel assessmentViewPanel;
        private AssessmentListView assessmentListView;
        private SubmissionView submissionView;
        private JButton backButton;
        private JPanel subHeader;
        private JLabel submissionLabel;
        
        public AssessmentsView()
        {
            backButton                  =   new JButton();
            subHeader                   =   new JPanel();
            submissionLabel             =   new JLabel();
            assessmentViewPanel         =   new JPanel(new CardLayout());
            assessmentListView          =   new AssessmentListView();
            submissionView              =   new SubmissionView();
            JPanel submissionWrapper    =   new JPanel(new BorderLayout());   
            
            Layout.makeTransparent(backButton);
            backButton.setIcon(new ImageIcon(backImage));
            submissionLabel.setFont(new Font("Arial", Font.PLAIN, 12));
            submissionLabel.setForeground(Color.DARK_GRAY);
            
            subHeader.add(backButton);
            subHeader.add(submissionLabel);
            submissionWrapper.add(subHeader, BorderLayout.NORTH);
            submissionWrapper.add(submissionView.getPanel(), BorderLayout.CENTER);
            
            assessmentViewPanel.add(assessmentListView.getPanel(), ASSESS_LIST_VIEW);
            assessmentViewPanel.add(submissionWrapper, ASSESS_SUB_VIEW);
            
            subHeader.setBackground(Color.WHITE);
            submissionWrapper.setBackground(Color.WHITE);
            assessmentViewPanel.setBackground(Color.WHITE);
            setBackground(Color.WHITE);
            showAssessmentView(ASSESS_LIST_VIEW);
            add(assessmentViewPanel);
        }
        
        private void showAssessmentView(String viewName)
        {
            CardLayout cLayout  =   (CardLayout) assessmentViewPanel.getLayout();
            cLayout.show(assessmentViewPanel, viewName);
        }
        
        private class AssessmentListView extends DataModuleView
        {
            private JButton goSubmissionsButton;
            
            @Override
            protected JsonArray getData()
            {
                return AssessmentModel.getAssessmentsForClass(classID);
            }
            
            @Override
            protected void initComponents()
            {
                super.initComponents();
                goSubmissionsButton    =   new JButton("Submissions");
                dataControls.add(goSubmissionsButton);
                panel.setPreferredSize(new Dimension(400, 320)); 
                tableScroller.setPreferredSize(new Dimension(400, 250));
                header.setPreferredSize(new Dimension(1, 90));
                        
                        
            }
            
            @Override
            protected void initListeners()
            {
                super.initListeners();
                goSubmissionsButton.addActionListener(this);
            }
            
            private void showSubmissions()
            {
                int selectedRow =   dataTable.getSelectedRow();
                if(selectedRow == -1)
                    JOptionPane.showMessageDialog(null, "Please select an assessment");
                else
                {
                    int assessmentID        =   Integer.parseInt(dataTable.getValueAt(selectedRow, 0).toString());
                    String assessmentName   =   (String) dataTable.getValueAt(selectedRow, 1);
                    submissionView.setAssessID(assessmentID);
                    submissionLabel.setText(MessageFormat.format("{0} submissions", assessmentName));
                    showAssessmentView(ASSESS_SUB_VIEW);
                }
            }
            
            @Override
            public void actionPerformed(ActionEvent e)
            {
                super.actionPerformed(e);
                Object src = e.getSource();
                
                if(src == goSubmissionsButton)
                    showSubmissions();
            }
            
            @Override
            protected void add()
            {
                AddAssessmentDialog dialog  =   new AddAssessmentDialog();
                int option  =   JOptionPane.showConfirmDialog(null, dialog, "Add assessment", JOptionPane.OK_CANCEL_OPTION);
                if(option == JOptionPane.OK_OPTION)
                {
                    ControllerMessage postData  =   new ControllerMessage();
                    postData.add("assessName", dialog.assessName.getText());
                    postData.add("assessDesc", dialog.assessDesc.getText());
                    postData.add("assessWeight", dialog.assessWeight.getText());
                    postData.add("assessDue", dialog.assessDue.getText());
                    postData.add("assessClass", classID);
                    
                    ResponseDataView response   =   (ResponseDataView) RouteHandler.go("postCreateAssessment", postData);
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
                    JOptionPane.showMessageDialog(null, "Please select an assessment to remove");
                else
                {
                    int assessID                =   Integer.parseInt(dataTable.getValueAt(selectedRow, 0).toString());
                    ControllerMessage postData  =   new ControllerMessage();
                    postData.add("assessId", assessID);
                    
                    ResponseDataView response   =   (ResponseDataView) RouteHandler.go("postDeleteAssessment", postData);
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
                    JOptionPane.showMessageDialog(null, "Please select an assessment to edit");
                else
                {
                    AddAssessmentDialog dialog  =   new AddAssessmentDialog();
                    dialog.assessName.setText((String) dataTable.getValueAt(selectedRow, 1));
                    dialog.assessDesc.setText((String) dataTable.getValueAt(selectedRow, 2));
                    dialog.assessWeight.setText(dataTable.getValueAt(selectedRow, 3).toString());
                    dialog.assessDue.setText((String) dataTable.getValueAt(selectedRow, 4));
                    int option  =   JOptionPane.showConfirmDialog(null, dialog, "Add assessment", JOptionPane.OK_CANCEL_OPTION);
                    if(option == JOptionPane.OK_OPTION)
                    {
                        ControllerMessage postData  =   new ControllerMessage();
                        postData.add("assessId", dataTable.getValueAt(selectedRow, 0));
                        postData.add("assessName", dialog.assessName.getText());
                        postData.add("assessDesc", dialog.assessDesc.getText());
                        postData.add("assessWeight", dialog.assessWeight.getText());
                        postData.add("assessDue", dialog.assessDue.getText());
                        postData.add("assessClass", classID);

                        ResponseDataView response   =   (ResponseDataView) RouteHandler.go("postEditAssessment", postData);
                        showResponseLabel(response.getRawResponseMessage(), response.getResponseStatus());

                        if(response.getResponseStatus())
                            loadData();
                    }
                }
            }

            @Override
            protected void initColumns()
            {
                columnHeaders   =   new String[] { "ID", "Name", "Description", "Weight", "Due date" };
                columnNames     =   new String[] { "Assessment ID", "name", "description", "Grade weight %", "Due date"};    
            }
            
            private class AddAssessmentDialog extends JPanel
            {
                protected JTextField assessName, assessDesc, assessWeight, assessDue;
                
                public AddAssessmentDialog()
                {
                    setLayout(new GridLayout(4, 2, 0, 10));
                    assessName      =   new JTextField();
                    assessDesc      =   new JTextField();
                    assessWeight    =   new JTextField();
                    assessDue       =   new JTextField();
                    
                    add(new JLabel("Name"));
                    add(assessName);
                    add(new JLabel("Description"));
                    add(assessDesc);
                    add(new JLabel("Grade weight %"));
                    add(assessWeight);
                    add(new JLabel("Due date (yyyy-mm-dd)"));
                    add(assessDue);
                }
            }
        }
        
        private class SubmissionView extends DataModuleView
        {
            private int assessID;
            
            @Override
            protected void initComponents()
            {
                assessID    =   -1;
                super.initComponents();
                tableScroller.setPreferredSize(new Dimension(300, 150));
                panel.setPreferredSize(new Dimension(360, 320));
            }
            
            @Override
            protected JsonArray getData() 
            {
                if(assessID == -1) return new JsonArray();
                else return AssessmentSubmissionsModel.getSubmissionsForAssessment(assessID);
            }
            
            protected void setAssessID(int assessID)
            {
                this.assessID   =   assessID;
                loadData();
            }
            
            @Override
            protected void initListeners()
            {
                super.initListeners();
                backButton.addActionListener(this);
            }
            
            @Override
            public void actionPerformed(ActionEvent e)
            {
                super.actionPerformed(e);
                Object src  =   e.getSource();
                
                if(src == backButton)
                    showAssessmentView(ASSESS_LIST_VIEW);
            }
            

            @Override
            protected void add()
            {
                AddSubmissionDialog dialog  =   new AddSubmissionDialog();
                int option                  =   JOptionPane.showConfirmDialog(null, dialog, "Add submission", JOptionPane.OK_CANCEL_OPTION);
                if(option == JOptionPane.OK_OPTION)
                {
                    ControllerMessage postData  =   new ControllerMessage();
                    postData.add("subUser", dialog.subUser.getText());
                    postData.add("assessID", assessID);
                    postData.add("subGrade", dialog.subGrade.getSelectedItem());
                    postData.add("subMark", dialog.subMark.getText());
                    postData.add("subComments", dialog.comments.getText());
                    
                    ResponseDataView response   =   (ResponseDataView) RouteHandler.go("postAddSubmission", postData);
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
                    JOptionPane.showMessageDialog(null, "Please select a submission to remove");
                else
                {
                    int option = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this submission?", "Remove submission", JOptionPane.OK_CANCEL_OPTION);
                    if(option == JOptionPane.OK_OPTION)
                    {
                        ControllerMessage postData  =   new ControllerMessage();
                        postData.add("subId", dataTable.getValueAt(selectedRow, 0));

                        ResponseDataView response   =   (ResponseDataView) RouteHandler.go("postRemoveSubmission", postData);
                        showResponseLabel(response.getRawResponseMessage(), response.getResponseStatus());

                        if(response.getResponseStatus())
                            loadData(); 
                    }
                }
            }

            @Override
            protected void edit() 
            {
                int selectedRow =   dataTable.getSelectedRow();
                if(selectedRow == -1)
                    JOptionPane.showMessageDialog(null, "Please select a submission to edit");
                else
                {
                    AddSubmissionDialog dialog  =   new AddSubmissionDialog();
                    dialog.subUser.setText((String) dataTable.getValueAt(selectedRow, 1));
                    dialog.subGrade.setSelectedItem(dataTable.getValueAt(selectedRow, 2).toString());
                    dialog.subMark.setText(dataTable.getValueAt(selectedRow, 3).toString());
                    dialog.comments.setText((String) dataTable.getValueAt(selectedRow, 5));
                    
                    int option = JOptionPane.showConfirmDialog(null, dialog, "Edit submission", JOptionPane.OK_CANCEL_OPTION);
                    if(option == JOptionPane.OK_OPTION)
                    {
                        ControllerMessage postData  =   new ControllerMessage();
                        postData.add("subUser", dialog.subUser.getText());
                        postData.add("assessID", assessID);
                        postData.add("subGrade", dialog.subGrade.getSelectedItem());
                        postData.add("subMark", dialog.subMark.getText());
                        postData.add("subComments", dialog.comments.getText());
                        postData.add("subId", dataTable.getValueAt(selectedRow, 0));

                        ResponseDataView response   =   (ResponseDataView) RouteHandler.go("postEditSubmission", postData);
                        showResponseLabel(response.getRawResponseMessage(), response.getResponseStatus());

                        if(response.getResponseStatus())
                            loadData();
                    }
                }
            }

            @Override
            protected void initColumns() 
            {
                columnHeaders   =   new String[] { "ID", "Username", "Grade", "Mark", "Date submitted", "Comments" };
                columnNames     =   new String[] { "Submission ID", "Username", "Grade", "mark", "Date submitted", "comments" };
            }
            
            private class AddSubmissionDialog extends JPanel
            {
                private JTextField subUser;
                private JComboBox subGrade;
                private JTextField subMark;
                private JTextField comments;
                
                public AddSubmissionDialog()
                {
                    setLayout(new GridLayout(4, 2));
                    subUser     =   new JTextField();
                    subGrade    =   new JComboBox();
                    subMark     =   new JTextField();
                    comments    =   new JTextField();
                    
                    String[] gradeLetters   =   new String[] { "A", "B", "C", "D", "E", "F" };
                    for(String letter : gradeLetters)
                    {
                        if(!letter.equals(gradeLetters[5])) subGrade.addItem(letter + "+");
                        subGrade.addItem(letter);
                        if(!letter.equals(gradeLetters[5])) subGrade.addItem(letter + "-");
                    }
                    
                    
                    add(new JLabel("Username"));
                    add(subUser);
                    add(new JLabel("Alpha grade"));
                    add(subGrade);
                    add(new JLabel("Mark"));
                    add(subMark);
                    add(new JLabel("Comments"));
                    add(comments);
                }
            }
            
        }
        
    }
    
    private class DetailsView extends JPanel
    {
        private JPanel classDetails;
        private JPanel teacherDetails;
        private JLabel teacherLabel;
        private JLabel classLabel;
        
        public DetailsView()
        {
            setLayout(new GridLayout(2, 1, 0, 20));
            classDetails    =   new JPanel();
            teacherDetails  =   new JPanel();
            teacherLabel    =   new JLabel("Teacher details");
            classLabel      =   new JLabel("Class details");
            
            classDetails.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.LIGHT_GRAY));
            teacherDetails.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.LIGHT_GRAY));
            teacherLabel.setFont(new Font("Arial", Font.BOLD, 14));
            classLabel.setFont(new Font("Arial", Font.BOLD, 14));
            
            JsonArray details    =   ClassesModel.getClassDetails(classID);
            if(details != null && details.size() > 1)
            {
                JsonObject detailObj    =   details.get(1).getAsJsonObject();
                JPanel classWrapper     =   new JPanel(new GridLayout(4, 2));
                classWrapper.add(new JLabel("Class name"));
                classWrapper.add(new JLabel(detailObj.get("CLASS_NAME").getAsString()));
                classWrapper.add(new JLabel("Class description"));
                classWrapper.add(new JLabel(detailObj.get("CLASS_DESC").getAsString()));
                classWrapper.add(new JLabel("Department"));
                classWrapper.add(new JLabel(detailObj.get("DEPT_NAME").getAsString()));
                classWrapper.add(new JLabel("Date created"));
                classWrapper.add(new JLabel(detailObj.get("CLASS_CREATED").getAsString()));
                classWrapper.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                classDetails.add(classWrapper);


                JPanel teacherWrapper   =   new JPanel(new GridLayout(4, 2));
                teacherWrapper.add(new JLabel("Name"));
                teacherWrapper.add(new JLabel(detailObj.get("TEACHER_FIRSTNAME").getAsString() + " " + detailObj.get("TEACHER_LASTNAME").getAsString()));
                teacherWrapper.add(new JLabel("Contact email"));
                teacherWrapper.add(new JLabel(detailObj.get("TEACHER_EMAIL").getAsString()));
                teacherWrapper.add(new JLabel("Contact phone"));
                teacherWrapper.add(new JLabel(detailObj.get("TEACHER_PHONE").getAsString()));
                teacherWrapper.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                teacherDetails.add(teacherWrapper);

                JPanel teacherOuterWrapper  =   new JPanel(new BorderLayout());
                teacherOuterWrapper.add(teacherLabel, BorderLayout.NORTH);
                teacherOuterWrapper.add(teacherDetails, BorderLayout.CENTER);

                JPanel classOuterWrapper    =   new JPanel(new BorderLayout());
                classOuterWrapper.add(classLabel, BorderLayout.NORTH);
                classOuterWrapper.add(classDetails, BorderLayout.CENTER);

                setBackground(Color.WHITE);
                classWrapper.setBackground(Color.WHITE);
                teacherWrapper.setBackground(Color.WHITE);
                teacherDetails.setBackground(Color.WHITE);
                classDetails.setBackground(Color.WHITE);
                teacherOuterWrapper.setBackground(Color.WHITE);
                classOuterWrapper.setBackground(Color.WHITE);

                add(teacherOuterWrapper);
                add(classOuterWrapper);
            }
        }
    }
    
    private class ClassNoticeView extends NoticesView
    {
        public ClassNoticeView()
        {
            super();
            announcementCode    =   "CLASS";
        }
        
        private ControllerMessage addClassToPost(ControllerMessage data)
        {
            if(data == null) return null;
            else
            {
                data.add("classID", classID);
                return data;
            }
        }
        
        @Override
        protected ControllerMessage prepareEditPost()
        {
            ControllerMessage postData  =   super.prepareEditPost();
            return addClassToPost(postData);
        }
        
        @Override
        protected ControllerMessage prepareAddPost()
        {
            ControllerMessage postData  =   super.prepareAddPost();
            return addClassToPost(postData);
        }
        
        @Override
        protected ControllerMessage prepareRemovePost()
        {
            ControllerMessage postData  =   super.prepareRemovePost();
            return addClassToPost(postData);
        }
        
        @Override
        protected JsonArray getData() 
        {
            return ClassAnnouncementsModel.getClassAnnouncementsFor(classID);
        }
    }
    
}
