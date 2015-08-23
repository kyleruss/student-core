
package engine.views.cui;

import com.google.gson.JsonArray;
import engine.controllers.ControllerMessage;
import engine.core.RouteHandler;
import engine.models.AssessmentModel;
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
        ResponseDataView response       =   (ResponseDataView) RouteHandler.go("postCreateAssessment", postData);
        
        System.out.println(response.getResponseMessage());
    }
    
    public void removeAssessment()
    {
        List<String> fieldTitles    =   new ArrayList<>();
        fieldTitles.add(CUITextTools.createFormField("Assessment ID", "What is the ID of the assessment to remove?"));
        
        List<String> fieldKeys  =   new ArrayList<>();
        fieldKeys.add("assessId");
        
        String[] headers    =   { "Assessment ID" };
        Map<String, String> inputData   =   CUITextTools.getFormInput(fieldTitles, fieldKeys, headers);
        
        ControllerMessage postData  =   new ControllerMessage().addAll(inputData);
        ResponseDataView response   =   (ResponseDataView) RouteHandler.go("postDeleteAssessment", postData);
        System.out.println(response.getResponseMessage());
    }
    
    public void modifyAssessment()
    {
        List<String> fieldTitles    =   new ArrayList<>();
        fieldTitles.add(CUITextTools.createFormField("Assessment ID", "What is the ID fo the assessment to modify?"));
        fieldTitles.add(CUITextTools.createFormField("Modify attribute", "What attribute are you modifying?"));
        fieldTitles.add(CUITextTools.createFormField("Modified value", "What is the new value for the field?"));
        
        List<String> fieldKeys      =   new ArrayList<>();
        fieldKeys.add("assessId");
        fieldKeys.add("assessAttr");
        fieldKeys.add("assessValue");
        
        String[] headers                =   { "Assessment ID", "Modify attribute", "New value" };   
        Map<String, String> inputData   =   CUITextTools.getFormInput(fieldTitles, fieldKeys, headers);
        
        ControllerMessage postData  =   new ControllerMessage().addAll(inputData);
        ResponseDataView response   =   (ResponseDataView) RouteHandler.go("postModifyAssessment", postData);
        System.out.println(response.getResponseMessage());
    }
    
    public void showSubmissions()
    {
        List<String> fieldTitles    =   new ArrayList<>();
        fieldTitles.add(CUITextTools.createFormField("Assessment ID", "What is the assessment ID of the assessment submissions"));
        
        List<String> fieldKeys  =   new ArrayList<>();
        fieldKeys.add("assessId");
        
        String[] headers    =   { "Asessment ID" };
        Map<String, String> inputData   =   CUITextTools.getFormInput(fieldTitles, fieldKeys, headers);
        
        int assessID    =   Integer.parseInt(inputData.get("assessId"));
        
    }
    
    @Override
    public void display()
    {
        super.display();
        
        JsonArray assessments   =   AssessmentModel.getAssessmentsForClass(messages.getData().get(1).getAsJsonObject().get("ID").getAsInt());
        if(assessments != null && assessments.size() > 0)
        {
            System.out.println("\n" + CUITextTools.underline(CUITextTools.changeColour("Assessments", CUITextTools.MAGENTA)));
            CUITextTools.responseToTable(assessments);
        }
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
       // v.makeAssessment();
     //   v.removeAssessment();
       // v.makeAssessment("Assignemt2", "Complete the graph problems", 10, "2015-09-10");
    }
    
}
