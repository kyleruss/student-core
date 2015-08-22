
package engine.views.cui;

import com.google.gson.JsonArray;
import engine.controllers.ControllerMessage;
import engine.core.RouteHandler;
import engine.models.ClassEnrolmentModel;
import engine.models.ClassesModel;
import engine.parsers.JsonParser;
import engine.views.AbstractView;
import engine.views.View;
import engine.views.cui.Utilities.CUITextTools;
import java.sql.SQLException;

public class ClassPageView extends AbstractView
{
    public ClassPageView()
    {
        this(new ControllerMessage());
    }
    
    public ClassPageView(ControllerMessage messages)
    {
        super
        (
                messages, 
                messages.getData().get(1).getAsJsonObject().get("NAME").toString(), 
                "Class homepage, view and manage this class", 
                "/" + "students"//Agent.getActiveSession().getUser().get("USERNAME").getNonLiteralValue() + "/students/"
        );
    }
    
    @Override
    public void display()
    {
        super.display();
        CUITextTools.responseToTable(messages.getData());
    }
    
    public void showClassStudents()
    {
        int classId =   messages.getData().get(1).getAsJsonObject().get("ID").getAsInt();
        try
        {
            JsonArray students  =   ClassEnrolmentModel.getStudentsEnrolledIn(classId);
            System.out.println(CUITextTools.underline(CUITextTools.changeColour("Class has " + (students.size() - 1) + " student(s)", CUITextTools.GREEN)));
            CUITextTools.responseToTable(students);
        }
        
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
            System.out.println(CUITextTools.changeColour("No students found", CUITextTools.RED));
        }
    }
    
    public void showTeacherContact()
    {
        int classId =   messages.getData().get(1).getAsJsonObject().get("ID").getAsInt();
        try
        {
            JsonArray teacher   =   ClassesModel.getTeacherContact(classId);
            System.out.println(JsonParser.parsePretty(teacher));
            
            if(teacher.size() > 1)
            {
                System.out.println("\n" + CUITextTools.underline(CUITextTools.changeColour("Teacher contact details", CUITextTools.CYAN)));
                CUITextTools.responseToTable(teacher);
            }
        }
        
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
            System.out.println(CUITextTools.changeColour("No teacher found", CUITextTools.RED));
        }
    }

    @Override
    protected String getCommandsFile() 
    {
        return "/engine/config/listeners/ClassPageListener.json";
    }
    
    public static void main(String[] args)
    {
        ClassPageView v = (ClassPageView) RouteHandler.go("getClassPage", new Object[] { 1 }, new Class<?>[] { Integer.class }, null);
        v.display();
        v.showTeacherContact();
    }
    
}
