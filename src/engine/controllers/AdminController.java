
package engine.controllers;

import engine.views.cui.AdminControlPanelView;
import engine.views.View;
import engine.views.cui.ResponseDataView;
import engine.views.cui.StudentListView;


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
    
    public View getStudentList()
    {
        //System.out.println("response: " + page);
        return new ResponseDataView("found!", true);
    }
    
    public View postRemoveStudent()
    {
        return null;
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
