//====================================
//  KYLE RUSSELL
//  13831056
//  PDC Project
//====================================

package engine.views.cui;

import engine.controllers.ControllerMessage;
import engine.core.Agent;
import engine.core.RouteHandler;
import engine.views.AbstractView;
import engine.views.View;
import engine.views.cui.Utilities.CUITextTools;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MyClassesView extends AbstractView
{
    public MyClassesView()
    {
        this(new ControllerMessage());
    }
    
    public MyClassesView(ControllerMessage messages)
    {
        super
        (
            messages, 
            "My classes", 
            "View and manage your classes" 
        );
    }
    
    @Override
    public void display()
    {
        super.display();
        CUITextTools.responseToTable(messages.getData());
    }
    
    public void showClassInfo()
    {
        List<String> fieldTitles    =   new ArrayList<>();
        fieldTitles.add(CUITextTools.createFormField("Class ID", "What is the class ID of the class you want to view?"));
        
        List<String> fieldKeys      =   new ArrayList<>();
        fieldKeys.add("classId");
        
        String[] headers    =   { "Class ID" };
        Map<String, String> inputData   =   CUITextTools.getFormInput(fieldTitles, fieldKeys, headers);
        
        int classId =   Integer.parseInt(inputData.get("classId"));
        View v  =   RouteHandler.go("getClassPage", new Object[] { classId }, new Class<?>[] { Integer.class }, null);
        Agent.setView(v);
    }
    
    @Override
    protected String getCommandsFile() 
    {
        return "/engine/config/listeners/MyClassesListener.json";
    }
    
    public static void main(String[] args)
    {
        MyClassesView view   =   (MyClassesView) RouteHandler.go("getMyClasses", null);
        view.display();
        view.showClassInfo();
    }
    
}
