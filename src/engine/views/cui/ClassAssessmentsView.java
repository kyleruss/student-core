
package engine.views.cui;

import engine.controllers.ControllerMessage;
import engine.core.RouteHandler;
import engine.views.AbstractView;
import engine.views.View;
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
                messages.getData().get(1).getAsJsonObject().get("NAME").getAsString() + " assessments", 
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
        postData.add("assessClass", messages.getData().get(1).getAsJsonObject().get("ID").getAsInt());
      /*  ControllerMessage postData      =   new ControllerMessage();
        postData.add("assessName", name);
        postData.add("assessDesc", desc);
        postData.add("assessWeight", weight);
        postData.add("assessDue", due);
        postData.add("assessClass", messages.getData().get(1).getAsJsonObject().get("ID").getAsInt());*/
        ResponseDataView response       =   (ResponseDataView) RouteHandler.go("postCreateAssessment", postData);
        
        System.out.println(response.getResponseMessage());
    }
    
    public void removeAssessment(int assessId)
    {
        ControllerMessage postData  =   new ControllerMessage();
        postData.add("assessId", assessId);
        
        ResponseDataView response   =   (ResponseDataView) RouteHandler.go("postDeleteAssessment", postData);
        System.out.println(response.getResponseMessage());
    }
    
    public void modifyAssessment(int assessId, String assessAttr, Object value)
    {
        ControllerMessage postData  =   new ControllerMessage();
        postData.add("assessId", assessId);
        postData.add("assessAttr", assessAttr);
        postData.add("assessValue", value);
        
        ResponseDataView response   =   (ResponseDataView) RouteHandler.go("postModifyAssessment", postData);
        System.out.println(response.getResponseMessage());
    }
    
    @Override
    public void display()
    {
        super.display();
    }
    
    @Override
    protected String getCommandsFile() 
    {
        return "/engine/config/listeners/ClassAssessmentsListener.json";
    }
    
    public static void main(String[] args)
    {  
        ClassAssessmentsView v  =   (ClassAssessmentsView) RouteHandler.go("getClassAssessments",new Object[] { 1 }, new Class<?>[] { Integer.class }, null);
        v.display();
        v.makeAssessment();
       // v.modifyAssessment(1, "weight", 30);
       // v.makeAssessment("Assignemt2", "Complete the graph problems", 10, "2015-09-10");
    }
    
}
