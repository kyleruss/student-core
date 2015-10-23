//====================================
//  KYLE RUSSELL
//  13831056
//  PDC Project
//====================================

package engine.controllers;

import com.google.gson.JsonArray;
import engine.core.Agent;
import engine.core.DataConnector;
import engine.core.Path;
import engine.core.security.Crypto;
import engine.models.AdminAnnouncementsModel;
import engine.models.AssessmentModel;
import engine.models.AssessmentSubmissionsModel;
import engine.models.ClassEnrolmentModel;
import engine.models.ClassesModel;
import engine.models.DepartmentModel;
import engine.models.DeptAnnouncementsModel;
import engine.models.EmergencyContactModel;
import engine.models.MedicalModel;
import engine.models.Model;
import engine.models.Role;
import engine.models.User;
import engine.views.cui.AdminControlPanelView;
import engine.views.View;
import engine.views.cui.AssessmentSubmissionsView;
import engine.views.ResponseDataView;
import engine.views.cui.StudentListView;
import java.sql.ResultSet;
import java.sql.SQLException;


public class AdminController extends Controller
{
    public AdminController(Path path)
    {
        super(path);
    }
    
    public AdminController(ControllerMessage postData, Path path)
    {
        super(postData, path);
    }
    
    public AdminController(ControllerMessage postData, RequestType requestType, Path path)
    {
        super(postData, requestType, path);
    }
    
        
    public View getAdmincp()
    {
        if(!Agent.isGUIMode()) return prepareView(new AdminControlPanelView());
        else return prepareView(new engine.views.gui.admin.AdminControlPanelView());
    }
    
    
    public View getStudents()
    {
        return prepareView(new StudentListView());
    }
    
    public View getStudentList(Integer page, Integer numResults)
    {
        try
        {
            JsonArray users         = new User().builder().setPage(page, numResults).get();
            ControllerMessage data  =   new ControllerMessage(users);
            return prepareView(new ResponseDataView("Users have been fetched successfully", true, data, 5));
        }
        
        catch(SQLException e)
        {
            return prepareView(new ResponseDataView("An error occured fetching students", false));
        }
    }
    
    public View postRemoveStudent()
    {
        final String invalidInputMesage         =   "Invalid information, please check your fields";
        final String failedMessage              =   "Failed to remove user";
        final String successMessage             =   "Successfully removed user";
        
        if(!validatePostData(new String[]{"removeUsername"})) 
            return prepareView(new ResponseDataView(invalidInputMesage, false)); 
        else
        {
            String username =   (String) postData.getMessage("removeUsername");
            User user       =   new User(username);
            
            if(user.delete()) return prepareView(new ResponseDataView(successMessage, true));
            else return prepareView(new ResponseDataView(failedMessage, false));
        }
    }
    
     
    public View postModifyStudent()
    {
        final String invalidInputMesage         =   "Invalid information, please check your fields";
        final String failedMessage              =   "Failed to modify user";
        final String successMessage             =   "Successfully modified user";
        
        if(!validatePostData(new String[]{"modifyUsername", "modifyAttribute", "modifyValue"}))
            return prepareView(new ResponseDataView(invalidInputMesage, false)); 
        else
        {
            String username =   (String) postData.getMessage("modifyUsername");
            String attr     =   (String) postData.getMessage("modifyAttribute");
            Object value    =   postData.getMessage("modifyValue");
            
            User user       =   new User(username);
            if(!user.hasColumn(attr))
                return prepareView(new ResponseDataView(invalidInputMesage, false));
            
            else
            {
                user.set(attr, value);
                if(user.update()) return prepareView(new ResponseDataView(successMessage, true)); 
                else return prepareView(new ResponseDataView(failedMessage, false)); 
            }
        }
    }
    
    
    public View postSearchStudent()
    {
        final String invalidInputMesage         =   "Invalid information, please check your fields";
        final String failedMessage              =   "Failed to search for user(s)";
        final String successMessage             =   "Successfully found";
        
       if(!validatePostData(new String[]{"searchAttribute", "searchValue", "searchOperator"})) 
           return prepareView(new ResponseDataView(invalidInputMesage, false));
       else
       {
           String searchAttribute   =  (String) postData.getMessage("searchAttribute");
           String searchValue       =  (String) postData.getMessage("searchValue");
           String searchOperator    =  (String) postData.getMessage("searchOperator");
           
           try
           {
                User user           =   new User();
                boolean isLiteral   =   user.getColumn(searchAttribute.toUpperCase()).isLiteral();
                searchValue         =   (isLiteral)? Model.makeLiteral(searchValue) : searchValue;
                
                JsonArray results   = user.builder().where(searchAttribute, searchOperator, searchValue).get();
                if(results == null) return 
                    prepareView(new ResponseDataView(failedMessage, false));
                
                ControllerMessage data  =   new ControllerMessage(results);

                int numResultsFound =   results.size() - 1;
                return 
                    prepareView(new ResponseDataView(successMessage + " " + numResultsFound + " user(s)", true, data, 5));
           }
           
           catch(SQLException e)
           {
          //     e.printStackTrace();
               return prepareView(new ResponseDataView(failedMessage, false));
           }
       }
    }
    
    public View postCreateAssessment()
    {
        final String invalidInputMesage         =   "Invalid input, please check your fields";
        final String failedMessage              =   "Failed to create assessment";
        final String successMessage             =   "Successfully created assessment";
        
        if(!validatePostData(new String[]{"assessName", "assessDesc", "assessWeight", "assessClass", "assessDue"})) 
            return prepareView(new ResponseDataView(invalidInputMesage, false));
        else
        {
            AssessmentModel assessment  =   new AssessmentModel();
            assessment.set("name", postData.getMessage("assessName"));
            assessment.set("description", postData.getMessage("assessDesc"));
            assessment.set("weight",  postData.getMessage("assessWeight"));
            assessment.set("class_id", postData.getMessage("assessClass"));
            assessment.set("due_date", postData.getMessage("assessDue"));
                    
            if(assessment.save()) 
                return prepareView(new ResponseDataView(successMessage, true));
            else 
                return prepareView(new ResponseDataView(failedMessage, false));
        }
    }
    
    public View postModifyAssessment()
    {
        final String invalidInputMesage         =   "Invalid input, please check your fields";
        final String failedMessage              =   "Failed to modify assessment";
        final String successMessage             =   "Successfully modified assessment";
        final String assessNotFound             =   "Assessment not found";
        
        if(!validatePostData(new String[]{"assessId", "assessAttr", "assessValue"}))
            return prepareView(new ResponseDataView(invalidInputMesage, false));
        else
        {
            AssessmentModel assessment   =   new AssessmentModel(postData.getMessage("assessId"));
            if(!assessment.exists())
                return prepareView(new ResponseDataView(assessNotFound, false));
            
            String columnModify          =   (String) postData.getMessage("assessAttr");
            Object value                 =   postData.getMessage("assessValue");
            assessment.set(columnModify, value);
            
            if(assessment.update()) 
                return prepareView(new ResponseDataView(successMessage, true));
            else 
                return prepareView(new ResponseDataView(failedMessage, false));
        }
    }
    
    public View postEditAssessment()
    {
        final String invalidInputMesage         =   "Invalid input, please check your fields";
        final String failedMessage              =   "Failed to modify assessment";
        final String successMessage             =   "Successfully modified assessment";
        final String assessNotFound             =   "Assessment not found";
        
        if(!validatePostData(new String[]{"assessId", "assessName", "assessDesc", "assessWeight", "assessClass", "assessDue"}))
            return prepareView(new ResponseDataView(assessNotFound, false));
        else
        {
            AssessmentModel assessment   =   new AssessmentModel(postData.getMessage("assessId"));
            if(!assessment.exists())
                return prepareView(new ResponseDataView(invalidInputMesage, false));
            
            assessment.set("name", postData.getMessage("assessName"));
            assessment.set("description", postData.getMessage("assessDesc"));
            assessment.set("weight",  postData.getMessage("assessWeight"));
            assessment.set("class_id", postData.getMessage("assessClass"));
            assessment.set("due_date", postData.getMessage("assessDue"));
            
            if(assessment.update()) 
                return prepareView(new ResponseDataView(successMessage, true));
            else 
                return prepareView(new ResponseDataView(failedMessage, false));
        }
    }
    
    public View postDeleteAssessment()
    {
        final String invalidInputMesage         =   "Invalid input, please check your fields";
        final String failedMessage              =   "Failed to delete assessment";
        final String successMessage             =   "Successfully deleted assessment";
        
        if(!validatePostData(new String[]{"assessId"}))
            return prepareView(new ResponseDataView(invalidInputMesage, false));
        else
        {
            AssessmentModel assessment  =   new AssessmentModel(postData.getMessage("assessId"));
            
            if(assessment.delete()) 
                return prepareView(new ResponseDataView(successMessage, true));
            else 
                return prepareView(new ResponseDataView(failedMessage, false));
        }
    }
    
    public View getAssessmentSubmissions(Integer assessId)
    {
        JsonArray assessmentDetails =   AssessmentSubmissionsModel.getSubmissionDetails(assessId);
        return prepareView(new AssessmentSubmissionsView(new ControllerMessage(assessmentDetails)));
    }
    
    public View postModifySubmission()
    {
        final String invalidInputMesage         =   "Invalid input, please check your fields";
        final String failedMessage              =   "Failed to modify submission";
        final String successMessage             =   "Successfully modified assessment";
        
        if(!validatePostData(new String[]{"subId", "subAttr", "subVal"}))
            return prepareView(new ResponseDataView(invalidInputMesage, false));
        else
        {
            AssessmentSubmissionsModel submission   =   new AssessmentSubmissionsModel(postData.getMessage("subId"));
            submission.set((String) postData.getMessage("subAttr"), postData.getMessage("subVal"));
            
            if(submission.update()) 
                return prepareView(new ResponseDataView(successMessage, true));
            else 
                return prepareView(new ResponseDataView(failedMessage, false));
        }
    }
    
    public View postRemoveSubmission()
    {
        final String invalidInputMesage         =   "Invalid input, please check your fields";
        final String failedMessage              =   "Failed to remove submission";
        final String successMessage             =   "Successfully removed assessment";
        
        if(!validatePostData(new String[]{"subId"})) 
            return prepareView(new ResponseDataView(invalidInputMesage, false));
        else
        {
            AssessmentSubmissionsModel submission    =   new AssessmentSubmissionsModel(postData.getMessage("subId"));
            
            if(submission.delete()) 
                return prepareView(new ResponseDataView(successMessage, true));
            else 
                return prepareView(new ResponseDataView(failedMessage, false));
        }
    }
    
    public View postMarkSubmission()
    {
        final String invalidInputMesage         =   "Invalid input, please check your fields";
        final String failedMessage              =   "Failed to mark submission";
        final String successMessage             =   "Successfully marked submission";
        
        if(!validatePostData(new String[]{"subId", "subGrade", "subMark"})) 
            return prepareView(new ResponseDataView(invalidInputMesage, false));
        else
        {
            AssessmentSubmissionsModel submission   =   new AssessmentSubmissionsModel(postData.getMessage("subId"));
            submission.set("alpha_grade", postData.getMessage("subGrade"));
            submission.set("mark", postData.getMessage("subMark"));
            
            if(submission.update()) 
                return prepareView(new ResponseDataView(successMessage, true));
            else 
                return prepareView(new ResponseDataView(failedMessage, false));
        }
    }
    
    public View postFindStudentSubmission()
    {
        final String invalidInputMesage         =   "Invalid input, please check your fields";
        final String failedMessage              =   "Failed to find submission";
        final String successMessage             =   "Successfully found assessment";
        
        if(!validatePostData(new String[]{"subUser", "assessId"})) 
            return prepareView(new ResponseDataView(invalidInputMesage, false));
        else
        {
            JsonArray foundSubmission =  AssessmentSubmissionsModel
                    .getSubmissionsForStudentAssessment((String) postData.getMessage("subUser"), Integer.parseInt((String) postData.getMessage("assessId")));
            
            if(foundSubmission.size() > 1) 
                return prepareView(new ResponseDataView(successMessage, true, new ControllerMessage(foundSubmission), 5));
            else
                return prepareView(new ResponseDataView(failedMessage, false));
        }
    }
    
    public View postAddAdminAnnouncement()
    {
        final String invalidInputMessage    =   "Invalid input";
        final String failedMessage          =   "Failed to add announcement";
        final String successMessage         =   "Successfully added announcement";
        final String codeNotFound           =   "Announcement model not found";
        
        if(!validatePostData(new String[] {"announceTitle", "announcePoster", "announceContent", "announceCode" }))
            return prepareView(new ResponseDataView(invalidInputMessage, false));
        else
        {
            Model model;
            switch((String) postData.getMessage("announceCode"))
            {
                case "ADMIN": 
                    model = new AdminAnnouncementsModel();
                    break;
                case "DEPT":
                    if(!validatePostData(new String[] { "deptID" }))
                        return prepareView(new ResponseDataView(invalidInputMessage, false));
                    else
                    {
                        model = new DeptAnnouncementsModel();
                        model.set("dept_id", postData.getMessage("deptID"));
                        break;
                    }
                default: return prepareView(new ResponseDataView(codeNotFound, false)); 
            }
            model.set("title", postData.getMessage("announceTitle"));
            model.set("content", postData.getMessage("announceContent"));
            model.set("announcer", postData.getMessage("announcePoster"));
            
            if(model.save())
                return prepareView(new ResponseDataView(successMessage, true)); 
            else
                return prepareView(new ResponseDataView(failedMessage, false));
        }
    }
    
    public View postEditAdminAnnouncement()
    {
        final String invalidInputMessage    =   "Invalid input";
        final String failedMessage          =   "Failed to edit announcement";
        final String successMessage         =   "Successfully edited announcement";
        final String codeNotFound           =   "Announcement model not found";
        
        if(!validatePostData(new String[] {"announceID", "announceTitle", "announceContent", "announceCode" }))
            return prepareView(new ResponseDataView(invalidInputMessage, false));
        else
        {
            Model model;
            switch((String) postData.getMessage("announceCode"))
            {
                case "ADMIN": 
                    model = new AdminAnnouncementsModel(postData.getMessage("announceID"));
                    break;
                case "DEPT":
                   if(!validatePostData(new String[] { "deptID" }))
                        return prepareView(new ResponseDataView(invalidInputMessage, false));
                    else
                    {
                        model = new DeptAnnouncementsModel(postData.getMessage("announceID"));
                        model.set("dept_id", postData.getMessage("deptID"));
                        break;
                    }
                default: return prepareView(new ResponseDataView(codeNotFound, false)); 
            }
            
            model.set("title", postData.getMessage("announceTitle"));
            model.set("content", postData.getMessage("announceContent"));
            
            if(model.update())
                return prepareView(new ResponseDataView(successMessage, true)); 
            else
                return prepareView(new ResponseDataView(failedMessage, false)); 
        }
    }
    
    public View postRemoveAdminAnnouncement()
    {
        final String invalidInputMessage    =   "Invalid input";
        final String failedMessage          =   "Failed to remove announcement";
        final String successMessage         =   "Successfully removed announcement";
        final String codeNotFound           =   "Announcement model not found";
        
        if(!validatePostData(new String[] {"announceID", "announceCode" }))
            return prepareView(new ResponseDataView(invalidInputMessage, false));
        else
        {
            
            Model model;
            switch((String) postData.getMessage("announceCode"))
            {
                case "ADMIN": 
                    model = new AdminAnnouncementsModel(postData.getMessage("announceID"));
                    break;
                case "DEPT":
                    if(!validatePostData(new String[] { "deptID" }))
                        return prepareView(new ResponseDataView(invalidInputMessage, false));
                    else
                    {
                        model = new DeptAnnouncementsModel(postData.getMessage("announceID"));
                        model.set("dept_id", postData.getMessage("deptID"));
                        break;
                    }
                default: return prepareView(new ResponseDataView(codeNotFound, false)); 
            }
            
            if(model.delete())
                return prepareView(new ResponseDataView(successMessage, true)); 
            else
                return prepareView(new ResponseDataView(failedMessage, false));
        }
    }
    
    public View postRemoveRole()
    {
        final String invalidInputMessage    =   "Invalid input";
        final String failedMessage          =   "Failed to remove role";
        final String successMessage         =   "Successfully removed role";
        
        if(!validatePostData(new String[]  {"roleID" }))
            return prepareView(new ResponseDataView(invalidInputMessage, false));
        else
        {
            Role roleModel  =   new Role(postData.getMessage("roleID"));
            if(roleModel.delete())
                return prepareView(new ResponseDataView(successMessage, true)); 
            else
                return prepareView(new ResponseDataView(failedMessage, false));
                
        }
    }
    
    public View postAddRole()
    {
        final String invalidInputMessage    =   "Invalid input";
        final String failedMessage          =   "Failed to add role";
        final String successMessage         =   "Successfully added role";
        
        if(!validatePostData(new String[] { "roleName", "roleDesc", "permLevel" }))
            return prepareView(new ResponseDataView(invalidInputMessage, false));
        else
        {
            Role role   =   new Role();
            role.set("name", postData.getMessage("roleName"));
            role.set("description", postData.getMessage("roleDesc"));
            role.set("permission_level", postData.getMessage("permLevel"));

            if(role.save())
                return prepareView(new ResponseDataView(successMessage, true)); 
            else
                return prepareView(new ResponseDataView(failedMessage, false));
        }
    }
    
    public View postEditRole()
    {
        final String invalidInputMessage    =   "Invalid input";
        final String failedMessage          =   "Failed to edit role";
        final String successMessage         =   "Successfully edited role";
        
        if(!validatePostData(new String[] { "roleName", "roleDesc", "permLevel", "roleID" }))
            return prepareView(new ResponseDataView(invalidInputMessage, false));
        else
        {
            int id      =   (int) postData.getMessage("roleID");
            Role role   =   new Role(id);
            role.set("name", postData.getMessage("roleName"));
            role.set("description", postData.getMessage("roleDesc"));
            role.set("permission_level", postData.getMessage("permLevel"));
            
            if(role.update())
                return prepareView(new ResponseDataView(successMessage, true)); 
            else
                return prepareView(new ResponseDataView(failedMessage, false));
                
        }
    }
    
    public View postAssignRole()
    {
        final String invalidInputMessage    =   "Invalid input";
        final String failedMessage          =   "Failed to assign role";
        final String successMessage         =   "Successfully assigned role";
        final String roleNotFoundMessage    =   "This role was not found";
        final String userNotFoundMessage    =   "User was not found";
        
        if(!validatePostData(new String[] { "roleID", "assignTO" }))
            return prepareView(new ResponseDataView(invalidInputMessage, false));
        else
        {
            int roleID          =   (int) postData.getMessage("roleID");
            String assignUser   =   (String) postData.getMessage("assignTO");
            Role role           =   new Role(roleID);
            User user           =   new User(assignUser);
            
            if(!role.exists())
                return prepareView(new ResponseDataView(roleNotFoundMessage, false));
            else if(!user.exists())
                return prepareView(new ResponseDataView(userNotFoundMessage, false));
            else
            {
                user.set("role_id", roleID);
                if(user.update())
                    return prepareView(new ResponseDataView(successMessage, true));
                else
                    return prepareView(new ResponseDataView(failedMessage, false));
            }
        }
    }
    
    public View postAddDepartment()
    {
        final String invalidInputMessage    =   "Invalid input";
        final String failedMessage          =   "Failed to create department";
        final String successMessage         =   "Successfully created department";
        final String userNotFoundMessage    =   "User assigned HOD was not found";
        
        if(!validatePostData(new String[] { "deptName", "deptDesc", "deptHOD" }))
            return prepareView(new ResponseDataView(invalidInputMessage, false));
        else
        {
            String username =   (String) postData.getMessage("deptHOD");
            User user   =   new User(username);
            if(!user.exists())
                return prepareView(new ResponseDataView(userNotFoundMessage, false));
            else
            {
                DepartmentModel dept =   new DepartmentModel();
                dept.set("name", (String) postData.getMessage("deptName"));
                dept.set("description", (String) postData.getMessage("deptDesc"));
                dept.set("dept_head", username);
                
                if(dept.save())
                    return prepareView(new ResponseDataView(successMessage, true));
                else
                    return prepareView(new ResponseDataView(failedMessage, false));
            }
        }
    }
    
    public View postRemoveDepartment()
    {
        final String invalidInputMessage    =   "Invalid input";
        final String failedMessage          =   "Failed to remove department";
        final String successMessage         =   "Successfully removed department";
        final String deptNotFoundMessage    =   "Department was not found";
        
        if(!validatePostData(new String[] { "deptID" }))
            return prepareView(new ResponseDataView(invalidInputMessage, false));
        else
        {
            DepartmentModel dept    =   new DepartmentModel(postData.getMessage("deptID"));   
            
            if(!dept.exists()) 
                return prepareView(new ResponseDataView(deptNotFoundMessage, false));
            else
            {
                if(dept.delete())
                    return prepareView(new ResponseDataView(successMessage, true));
                else
                    return prepareView(new ResponseDataView(failedMessage, false));
            }
        }
    }
    
    public View postEditDepartment()
    {
        final String invalidInputMessage    =   "Invalid input";
        final String failedMessage          =   "Failed to edit department";
        final String successMessage         =   "Successfully edited department";
        final String userNotFoundMessage    =   "User assigned HOD was not found";
        final String deptNotFoundMessage    =   "Department was not found";
        
        if(!validatePostData(new String[] { "deptName", "deptDesc", "deptHOD", "deptID" }))
            return prepareView(new ResponseDataView(invalidInputMessage, false));
        else
        {
            System.out.println("EDIT--");
            User user               =   new User(postData.getMessage("deptHOD"));
            DepartmentModel dept    =   new DepartmentModel(postData.getMessage("deptID"));
            if(!user.exists())
                return prepareView(new ResponseDataView(userNotFoundMessage, false));
            else if(!dept.exists())
                return prepareView(new ResponseDataView(deptNotFoundMessage, false));
            else
            {
                dept.set("name", postData.getMessage("deptName"));
                dept.set("description", postData.getMessage("deptDesc"));
                dept.set("dept_head", postData.getMessage("deptHOD"));
                
                if(dept.update())
                    return prepareView(new ResponseDataView(successMessage, true));
                else
                    return prepareView(new ResponseDataView(failedMessage, false));
            }
        }
    }
    
    public View postAddClass()
    {
        final String invalidInputMessage    =   "Invalid input";
        final String failedMessage          =   "Failed to create class";
        final String successMessage         =   "Successfully created class";
        final String teacherMessage         =   "Teacher was not found";
        
        if(!validatePostData(new String[] { "className", "classDescription", "teacherID", "deptID" }))
            return prepareView(new ResponseDataView(invalidInputMessage, false));
        else
        {
            User teacher    =   new User(postData.getMessage("teacherID"));
            if(!teacher.exists())
                return prepareView(new ResponseDataView(teacherMessage, false));
            else
            {
                ClassesModel classModel     =   new ClassesModel();
                classModel.set("name", postData.getMessage("className"));
                classModel.set("description", postData.getMessage("classDescription"));
                classModel.set("teacher_id", postData.getMessage("teacherID"));
                classModel.set("dept_id", postData.getMessage("deptID"));
                
                if(classModel.save())
                    return prepareView(new ResponseDataView(successMessage, true));
                else
                    return prepareView(new ResponseDataView(failedMessage, false));
            }
        }
            
    }
    
    public View postRemoveClass()
    {
        final String invalidInputMessage    =   "Invalid input";
        final String failedMessage          =   "Failed to remove class";
        final String successMessage         =   "Successfully removed class";
        final String classNotFound          =   "Class was not found";
        
        if(!validatePostData(new String[] { "classID" }))
            return prepareView(new ResponseDataView(invalidInputMessage, false));
        else
        {
            ClassesModel classModel     =   new ClassesModel(postData.getMessage("classID"));
            if(!classModel.exists()) 
                return prepareView(new ResponseDataView(classNotFound, false));
            else
            {
                if(classModel.delete())
                    return prepareView(new ResponseDataView(successMessage, true));
                else
                    return prepareView(new ResponseDataView(failedMessage, false));
            }
        }
    }
    
    public View postEditClass()
    {
        final String invalidInputMessage    =   "Invalid input";
        final String failedMessage          =   "Failed to edit class";
        final String successMessage         =   "Successfully edited class";
        final String classNotFound          =   "Class was not found";
        final String teacherNotFound        =   "Teacher was not found";
        
        if(!validatePostData(new String[] { "classID", "className", "classDescription", "teacherID", "deptID" }))
            return prepareView(new ResponseDataView(invalidInputMessage, false));
        else
        {
            User teacher            =   new User(postData.getMessage("teacherID"));
            ClassesModel classModel =   new ClassesModel(postData.getMessage("classID"));
            
            if(!classModel.exists())
                return prepareView(new ResponseDataView(classNotFound, false));
            else if(!teacher.exists())
                return prepareView(new ResponseDataView(teacherNotFound, false));
            else
            {
                classModel.set("name", postData.getMessage("className"));
                classModel.set("description", postData.getMessage("classDescription"));
                classModel.set("teacher_id", postData.getMessage("teacherID"));
                classModel.set("dept_id", postData.getMessage("deptID"));
                
                if(classModel.update())
                    return prepareView(new ResponseDataView(successMessage, true));
                else
                    return prepareView(new ResponseDataView(failedMessage, false));
            }
        }
    }
    
    public View postEditUser()
    {
        final String invalidInputMesage         =   "Invalid information, please check your fields";
        final String failedMessage              =   "Failed to edit user";
        final String successMessage             =   "Successfully edited user";
        final String medicalNotFound            =   "Medical details could not be found";
        final String contactNotFound            =   "Emergency contact details could not be found";
        final String userNotFound               =   "User was not found";
        
        String[] input  =   
        { 
            "registerUsername", "registerPassword", "registerFirstname", "registerLastname",
            "registerGender", "registerBirth", "registerPhone", "registerEmail", "registerEthnicity",
            "registerContactFirstname", "registerContactLastname", "registerContactPhone",
            "registerContactEmail", "registerMedicalDescription"
        };
        
        if(!validatePostData(input))
            return prepareView(new ResponseDataView(invalidInputMesage, false));
        else
        {
            User user       =   new User(postData.getMessage("registerUsername"));
            if(!user.exists()) 
                return prepareView(new ResponseDataView(userNotFound, false));
            
            int gender      =   ((String) postData.getMessage("registerGender")).equalsIgnoreCase("male")? 1 : 0;
            String passSalt =   Crypto.salt((String) postData.getMessage("registerUsername"));
            String passHash =   Crypto.makeHash(passSalt, (String) postData.getMessage("registerPassword"));
                    
            user.set("username", postData.getMessage("registerUsername"));
            user.set("password", passHash);
            user.set("firstname", postData.getMessage("registerFirstname"));
            user.set("lastname", postData.getMessage("registerLastname"));
            user.set("gender", gender);
            user.set("birthdate", postData.getMessage("registerBirth"));
            user.set("contact_ph", postData.getMessage("registerPhone"));
            user.set("contact_email", postData.getMessage("registerEmail"));
            user.set("ethnicity", postData.getMessage("registerEthnicity"));
            
            MedicalModel medical    =  new MedicalModel(user.get("MEDICAL_ID").getNonLiteralValue());
            if(!medical.exists())
                return prepareView(new ResponseDataView(medicalNotFound, false));
            
            medical.set("description", postData.getMessage("registerMedicalDescription"));
            
            EmergencyContactModel emergencyContact  =   new EmergencyContactModel(medical.get("CONTACT_ID").getNonLiteralValue());
            if(!emergencyContact.exists())
                return prepareView(new ResponseDataView(contactNotFound, false));
            
            emergencyContact.set("firstname", postData.getMessage("registerContactFirstname"));
            emergencyContact.set("lastname", postData.getMessage("registerContactLastname"));
            emergencyContact.set("contact_ph", postData.getMessage("registerContactPhone"));
            emergencyContact.set("contact_email", postData.getMessage("registerContactEmail"));
            emergencyContact.set("relationship", "Doctor");
            
            
            try(DataConnector conn  =   new DataConnector())
            {
                try
                {
                    conn.startTransaction();
                    conn.setQueryMutator();

                    emergencyContact.setActiveConnection(conn);
                    if(!emergencyContact.update()) throw new SQLException();

                    medical.setActiveConnection(conn);
                    if(!medical.update()) throw new SQLException();

                    user.setActiveConnection(conn);
                    if(!user.update()) throw new SQLException();
                    
                    conn.commitTransaction();
                    return prepareView(new ResponseDataView(successMessage, true));
                }

                catch(SQLException e)
                {
                    conn.rollbackTransaction();
                    conn.closeConnection();
                    return prepareView(new ResponseDataView(failedMessage, false));
                }
            }
        }
    }
    
    public View postRemoveEnrolment()
    {
        final String invalidInputMesage         =   "Invalid information, please check your fields";
        final String failedMessage              =   "Failed to remove the enrolment";
        final String successMessage             =   "Successfully removed the enrolment";
        final String enrolmentNotFound          =   "The enrolment could not be found";
        
        if(!validatePostData(new String[] { "enrolID" }))
            return prepareView(new ResponseDataView(invalidInputMesage, false));
        else
        {
            ClassEnrolmentModel enrolmentModel  =   new ClassEnrolmentModel(postData.getMessage("enrolID"));
            if(!enrolmentModel.exists())
                return prepareView(new ResponseDataView(enrolmentNotFound, false));
            else
            {
                if(enrolmentModel.delete())
                    return prepareView(new ResponseDataView(successMessage, true));
                else
                    return prepareView(new ResponseDataView(failedMessage, false));
            }
        }
    }
    
    public View postAddEnrolment()
    {
        final String invalidInputMesage         =   "Invalid information, please check your fields";
        final String failedMessage              =   "Failed to add enrolment";
        final String successMessage             =   "Successfully added enrolment";
        final String userEnroled                =   "This user is already enroled in this class";
        
        if(!validatePostData(new String[] { "userID", "classID", "semester" }))
            return prepareView(new ResponseDataView(invalidInputMesage, false));
        else
        {
            ClassEnrolmentModel enrolmentModel  =   new ClassEnrolmentModel();
            try
            {
                JsonArray results                   =   enrolmentModel.builder()
                                                        .where("user_id", "=",  postData.getMessage("userID").toString())
                                                        .where("class_id", "=", postData.getMessage("classID").toString())
                                                        .where("semester_num", "=", postData.getMessage("semester").toString())
                                                        .get();
                
                if(results != null && results.size() > 1)
                    return prepareView(new ResponseDataView(userEnroled, false)); 
                else
                {
                    enrolmentModel.set("user_id", postData.getMessage("userID"));
                    enrolmentModel.set("class_id", postData.getMessage("classID"));
                    enrolmentModel.set("semester_num", postData.getMessage("semester"));
                    
                    if(enrolmentModel.save())
                        return prepareView(new ResponseDataView(successMessage, true)); 
                    else
                        return prepareView(new ResponseDataView(failedMessage, false)); 
                }
            }
            
            catch(SQLException e)
            {
                return prepareView(new ResponseDataView(failedMessage, false)); 
            }
        }
    }
}
