
package engine.views.cui;

import engine.controllers.ControllerMessage;
import engine.core.RouteHandler;
import engine.views.AbstractView;
import engine.views.cui.Utilities.CUITextTools;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class ClassAssessmentsView extends AbstractView
{
    
    public ClassAssessmentsView()
    {
        this(new ControllerMessage());
    }
    
    public ClassAssessmentsView(ControllerMessage messages)
    {
        super
        (
                messages, 
                messages.getData().get(1).getAsJsonObject().get("NAME").toString() + " assessments", 
                "View and manage assessments for this class", 
                "/" + "students"//Agent.getActiveSession().getUser().get("USERNAME").getNonLiteralValue() + "/students/"
        );
    }
    
    public void makeAssessment()
    {
        List<String> fieldTitles    =   new ArrayList<>();
        fieldTitles.add(CUITextTools.createFormField("Assessment name", "What is the title/name of the assessment?"));
        fieldTitles.add(CUITextTools.createFormField("Assessment description", "What are the details of the assessment?"));
        fieldTitles.add(CUITextTools.createFormField("Assessment grade weight", "What is the grade weighting of this assessment?"));
        fieldTitles.add(CUITextTools.createFormField("Assessment due date", "When must this asssessment be submitted?"));
        
        List<String> fieldKeys  =   new ArrayList<>();
        fieldKeys.add("assessName");
        fieldKeys.add("assessDesc");
        fieldKeys.add("assessWeight");
        fieldKeys.add("assessDue");
        
        String[] headers    =   { "Name", "Description", "Grade weight", "Due date" };
        Map<String, String> inputData   =   CUITextTools.getFormInput(fieldTitles, fieldKeys, headers);
        
        ControllerMessage postData      =   new ControllerMessage().addAll(inputData);
        ResponseDataView response       =   (ResponseDataView) RouteHandler.go("postCreateAssessment", postData);
        
    }
    
    public void removeAssessment()
    {
        
    }
    
    public void modifyAssessment()
    {
        
    }
    
    @Override
    public void display()
    {
        super.display();
    }
    
    @Override
    protected String getCommandsFile() 
    {
        return "/engine/config/listeners/ClassPageListener.json";
    }
    
}
