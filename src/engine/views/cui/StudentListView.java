
package engine.views.cui;

import engine.controllers.ControllerMessage;
import engine.core.Agent;
import engine.core.RouteHandler;
import engine.views.AbstractView;
import engine.views.View;


public class StudentListView extends AbstractView
{
    
    public StudentListView()
    {
        this(new ControllerMessage());
    }
    
    public StudentListView(ControllerMessage messages)
    {
        super
        (
                messages, 
                "Students", 
                "Find, remove, modify students in the school", 
                "/" + Agent.getActiveSession().getUser().get("USERNAME").getNonLiteralValue() + "/students/"
        );
    }
    
    @Override
    public void display()
    {
        super.display();
    //    ResponseDataView results = (ResponseDataView) RouteHandler.go("getStudentList", new String[] { "1" }, null);
      //  System.out.println("result: " +results.getResponseMessage());
       
    }
    
    public void deleteStudent()
    {
        
    }
    
    public void modifyStudent()
    {
        
    }
    
    public void findStudent()
    {
        
    }
    
    public void nextPage()
    {
        
    }
    
    public void prevPage()
    {
        
    }

    @Override
    protected String getCommandsFile() 
    {
        return "/engine/config/listeners/StudentListListener.json";
    }
    
    public static void main(String[] args)
    {
        ResponseDataView results = (ResponseDataView) RouteHandler.go("getStudentList", null);//new String[] { "1" }, new Class<?>[] { String.class }, null);
    }
    
}
