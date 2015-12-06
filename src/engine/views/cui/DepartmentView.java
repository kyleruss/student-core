//====================================
//  KYLE RUSSELL
//  13831056
//  PDC Project
//====================================

package engine.views.cui;

import com.google.gson.JsonArray;
import engine.controllers.ControllerMessage;
import engine.core.RouteHandler;
import engine.models.DepartmentModel;
import engine.views.AbstractView;
import engine.views.cui.Utilities.CUITextTools;


public class DepartmentView extends AbstractView
{
    
    public DepartmentView()
    {
        this(new ControllerMessage());
    }
    
    public DepartmentView(ControllerMessage message)
    {
        super
        (
            message,
            message.getData().get(1).getAsJsonObject().get("name".toUpperCase()).getAsString() + " Department", 
            "Access and manage your department" 
        );
    }
    
    @Override
    public void display()
    {
        super.display();
        
    }
    
    public void showDeptStaff()
    {
        int deptId      =   messages.getData().get(1).getAsJsonObject().get("ID").getAsInt();
        JsonArray users =   DepartmentModel.getUsersInDeptFiltered(deptId);
        
        if(users != null || users.size() > 1)
        {
            System.out.println("\n" + CUITextTools.underline(CUITextTools.changeColour("Department staff", CUITextTools.MAGENTA)) + "\n");
            CUITextTools.responseToTable(users);
        }
    }

    @Override
    protected String getCommandsFile() 
    {
        return "/engine/config/listeners/DepartmentListener.json";
    }
    
    
    public static void main(String[] args)
    {
        DepartmentView v = (DepartmentView) RouteHandler.go("getMyDepartment", null);
        v.display();
        v.showDeptStaff();
    }
}
