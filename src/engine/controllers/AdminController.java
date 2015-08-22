
package engine.controllers;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import engine.models.Model;
import engine.models.User;
import engine.views.cui.AdminControlPanelView;
import engine.views.View;
import engine.views.cui.ResponseDataView;
import engine.views.cui.StudentListView;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


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
            String username =   postData.getMessage("removeUsername");
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
            String username =   postData.getMessage("modifyUsername");
            String attr     =   postData.getMessage("modifyAttribute");
            String value    =   postData.getMessage("modifyValue");
            
            User user       =   new User(username);
            if(!user.hasColumn(attr))
            {
                System.out.println("doesnt have column");
                return new ResponseDataView(invalidInputMesage, false);
                
            }
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
           String searchAttribute   =  postData.getMessage("searchAttribute");
           String searchValue       =  postData.getMessage("searchValue");
           String searchOperator    =   postData.getMessage("searchOperator");
           
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
               e.printStackTrace();
               return new ResponseDataView(failedMessage, false);
           }
       }
    }
    
}
