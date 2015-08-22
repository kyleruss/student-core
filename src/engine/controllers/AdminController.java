
package engine.controllers;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
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
        final String failedMessage              =   "Invalid username or password";
        final String successMessage             =   "Successfully logged in";
        
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
        return null;
    }
    
    
    public View getSearchStudent()
    {
        return null;
    }
    
}
