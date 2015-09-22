//====================================
//  KYLE RUSSELL
//  13831056
//  PDC Project
//====================================

package engine.views.cui;

import com.google.gson.JsonArray;
import engine.controllers.ControllerMessage;
import engine.core.Agent;
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
                "Access and manage your staff department" 
               // "/"  + "department"//Agent.getActiveSession().getUser().get("USERNAME").getNonLiteralValue() + "/department/"
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
        JsonArray staff =   DepartmentModel.getStaffInDept(deptId);
        
        if(staff != null || staff.size() > 1)
        {
            System.out.println("\n" + CUITextTools.underline(CUITextTools.changeColour("Department staff", CUITextTools.MAGENTA)) + "\n");
            CUITextTools.responseToTable(staff);
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
