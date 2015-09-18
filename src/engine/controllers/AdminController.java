//====================================
//  KYLE RUSSELL
//  13831056
//  PDC Project
//====================================

package engine.controllers;

import com.google.gson.JsonArray;
import engine.models.AssessmentModel;
import engine.models.AssessmentSubmissionsModel;
import engine.models.Model;
import engine.models.User;
import engine.views.cui.AdminControlPanelView;
import engine.views.View;
import engine.views.cui.AssessmentSubmissionsView;
import engine.views.cui.ResponseDataView;
import engine.views.cui.StudentListView;
import java.sql.SQLException;


public class AdminController extends Controller
{
    public AdminController()
    {
        super();
    }
    
    public AdminController(ControllerMessage postData)
    {
        super(postData);
    }
    
    public AdminController(ControllerMessage postData, RequestType requestType)
    {
        super(postData, requestType);
    }
    
        
    public View getAdmincp()
    {
        return new AdminControlPanelView();
    }
    
    
    public View getStudents()
    {
        return new StudentListView();
    }
    
    public View getStudentList(Integer page, Integer numResults)
    {
        try
        {
            JsonArray users         = new User().builder().setPage(page, numResults).get();
            ControllerMessage data  =   new ControllerMessage(users);
            return new ResponseDataView("Users have been fetched successfully", true, data, 5);
        }
        
        catch(SQLException e)
        {
            return new ResponseDataView("An error occured fetching students", false);
        }
    }
    
    public View postRemoveStudent()
    {
        final String invalidInputMesage         =   "Invalid information, please check your fields";
        final String failedMessage              =   "Failed to remove user";
        final String successMessage             =   "Successfully removed user";
        
        if(!validatePostData(new String[]{"removeUsername"})) return new ResponseDataView(invalidInputMesage, false); 
        else
        {
            String username =   (String) postData.getMessage("removeUsername");
            User user       =   new User(username);
            
            if(user.delete()) return new ResponseDataView(successMessage, true);
            else return new ResponseDataView(failedMessage, false);
        }
    }
    
     
    public View postModifyStudent()
    {
        final String invalidInputMesage         =   "Invalid information, please check your fields";
        final String failedMessage              =   "Failed to modify user";
        final String successMessage             =   "Successfully modified user";
        
        if(!validatePostData(new String[]{"modifyUsername", "modifyAttribute", "modifyValue"})) return new ResponseDataView(invalidInputMesage, false); 
        else
        {
            String username =   (String) postData.getMessage("modifyUsername");
            String attr     =   (String) postData.getMessage("modifyAttribute");
            Object value    =   postData.getMessage("modifyValue");
            
            User user       =   new User(username);
            if(!user.hasColumn(attr))
                return new ResponseDataView(invalidInputMesage, false);
            
            else
            {
                user.set(attr, value);
                if(user.update()) return new ResponseDataView(successMessage, true); 
                else return new ResponseDataView(failedMessage, false); 
            }
        }
    }
    
    
    public View postSearchStudent()
    {
        final String invalidInputMesage         =   "Invalid information, please check your fields";
        final String failedMessage              =   "Failed to search for user(s)";
        final String successMessage             =   "Successfully found";
        
       if(!validatePostData(new String[]{"searchAttribute", "searchValue", "searchOperator"})) return new ResponseDataView(invalidInputMesage, false);
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
                if(results == null) return new ResponseDataView(failedMessage, false);
                
                ControllerMessage data  =   new ControllerMessage(results);

                int numResultsFound =   results.size() - 1;
                return new ResponseDataView(successMessage + " " + numResultsFound + " user(s)", true, data, 5);
           }
           
           catch(SQLException e)
           {
          //     e.printStackTrace();
               return new ResponseDataView(failedMessage, false);
           }
       }
    }
    
    public View postCreateAssessment()
    {
        final String invalidInputMesage         =   "Invalid input, please check your fields";
        final String failedMessage              =   "Failed to create assessment";
        final String successMessage             =   "Successfully created assessment";
        
        if(!validatePostData(new String[]{"assessName", "assessDesc", "assessWeight", "assessClass", "assessDue"})) 
            return new ResponseDataView(invalidInputMesage, false);
        else
        {
            AssessmentModel assessment  =   new AssessmentModel();
            assessment.set("name", postData.getMessage("assessName"));
            assessment.set("description", postData.getMessage("assessDesc"));
            assessment.set("weight",  postData.getMessage("assessWeight"));
            assessment.set("class_id", postData.getMessage("assessClass"));
            assessment.set("due_date", postData.getMessage("assessDue"));
                    
            try
            {
                if(assessment.save()) return new ResponseDataView(successMessage, true);
                else return new ResponseDataView(failedMessage, false);
            }
            
            catch(SQLException e)
            {
              //  System.out.println("[SQL Exception] " + e.getMessage());
                return new ResponseDataView(failedMessage, false);
            }
        }
    }
    
    public View postModifyAssessment()
    {
        final String invalidInputMesage         =   "Invalid input, please check your fields";
        final String failedMessage              =   "Failed to modify assessment";
        final String successMessage             =   "Successfully modified assessment";
        
        if(!validatePostData(new String[]{"assessId", "assessAttr", "assessValue"}))
            return new ResponseDataView(invalidInputMesage, false);
        else
        {
            AssessmentModel assessment   =   new AssessmentModel(postData.getMessage("assessId"));
            String columnModify          =   (String) postData.getMessage("assessAttr");
            Object value                 =   postData.getMessage("assessValue");
            assessment.set(columnModify, value);
            
            if(assessment.update()) return new ResponseDataView(successMessage, true);
            else return new ResponseDataView(failedMessage, false);
        }
    }
    
    public View postDeleteAssessment()
    {
        final String invalidInputMesage         =   "Invalid input, please check your fields";
        final String failedMessage              =   "Failed to delete assessment";
        final String successMessage             =   "Successfully deleted assessment";
        
        if(!validatePostData(new String[]{"assessId"}))
            return new ResponseDataView(invalidInputMesage, false);
        else
        {
            AssessmentModel assessment  =   new AssessmentModel(postData.getMessage("assessId"));
            
            if(assessment.delete()) return new ResponseDataView(successMessage, true);
            else return new ResponseDataView(failedMessage, false);
        }
    }
    
    public View getAssessmentSubmissions(Integer assessId)
    {
        JsonArray assessmentDetails =   AssessmentSubmissionsModel.getSubmissionDetails(assessId);
        return new AssessmentSubmissionsView(new ControllerMessage(assessmentDetails));
    }
    
    public View postModifySubmission()
    {
        final String invalidInputMesage         =   "Invalid input, please check your fields";
        final String failedMessage              =   "Failed to modify submission";
        final String successMessage             =   "Successfully modified assessment";
        
        if(!validatePostData(new String[]{"subId", "subAttr", "subVal"}))
            return new ResponseDataView(invalidInputMesage, false);
        else
        {
            AssessmentSubmissionsModel submission   =   new AssessmentSubmissionsModel(postData.getMessage("subId"));
            submission.set((String) postData.getMessage("subAttr"), postData.getMessage("subVal"));
            
            if(submission.update()) return new ResponseDataView(successMessage, true);
            else return new ResponseDataView(failedMessage, false);
        }
    }
    
    public View postRemoveSubmission()
    {
        final String invalidInputMesage         =   "Invalid input, please check your fields";
        final String failedMessage              =   "Failed to remove submission";
        final String successMessage             =   "Successfully removed assessment";
        
        if(!validatePostData(new String[]{"subId"})) 
            return new ResponseDataView(invalidInputMesage, false);
        else
        {
            AssessmentSubmissionsModel submission    =   new AssessmentSubmissionsModel(postData.getMessage("subId"));
            
            if(submission.delete()) return new ResponseDataView(successMessage, true);
            else return new ResponseDataView(failedMessage, false);
        }
    }
    
    public View postMarkSubmission()
    {
        final String invalidInputMesage         =   "Invalid input, please check your fields";
        final String failedMessage              =   "Failed to mark submission";
        final String successMessage             =   "Successfully marked submission";
        
        if(!validatePostData(new String[]{"subId", "subGrade", "subMark"})) 
            return new ResponseDataView(invalidInputMesage, false);
        else
        {
            AssessmentSubmissionsModel submission   =   new AssessmentSubmissionsModel(postData.getMessage("subId"));
            submission.set("alpha_grade", postData.getMessage("subGrade"));
            submission.set("mark", postData.getMessage("subMark"));
            
            if(submission.update()) return new ResponseDataView(successMessage, true);
            else return new ResponseDataView(failedMessage, false);
        }
    }
    
    public View postFindStudentSubmission()
    {
        final String invalidInputMesage         =   "Invalid input, please check your fields";
        final String failedMessage              =   "Failed to find submission";
        final String successMessage             =   "Successfully found assessment";
        
        if(!validatePostData(new String[]{"subUser", "assessId"})) 
            return new ResponseDataView(invalidInputMesage, false);
        else
        {
            JsonArray foundSubmission =  AssessmentSubmissionsModel
                    .getSubmissionsForStudentAssessment((String) postData.getMessage("subUser"), Integer.parseInt((String) postData.getMessage("assessId")));
            
            if(foundSubmission.size() > 1) 
                return new ResponseDataView(successMessage, true, new ControllerMessage(foundSubmission), 5);
            else
                return new ResponseDataView(failedMessage, false);
        }
    }
}
