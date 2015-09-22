//====================================
//  KYLE RUSSELL
//  13831056
//  PDC Project
//====================================

package engine.views.cui;

import engine.views.ResponseDataView;
import com.google.gson.JsonArray;
import engine.controllers.ControllerMessage;
import engine.core.RouteHandler;
import engine.models.AssessmentSubmissionsModel;
import engine.views.AbstractView;
import engine.views.cui.Utilities.CUITextTools;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class AssessmentSubmissionsView extends AbstractView
{
    public AssessmentSubmissionsView()
    {
        this(new ControllerMessage());
    }
    
    public AssessmentSubmissionsView(ControllerMessage messages)
    {
        super
        (
                messages, 
                ((messages.getData().size() > 1)? messages.getData().get(1).getAsJsonObject().get("NAME").getAsString() : "" )+ " submissions", 
                "View and manage submissions for this assessment" 
             //   "/" + ((messages.getData().size() > 1)? messages.getData().get(1).getAsJsonObject().get("NAME").getAsString() + "/" : "" ) +  "submissions/" 
        );
    }
    
    @Override
    public void display()
    {
        super.display();
        
        if(messages.getData() == null || messages.getData().size() <= 1) return; 
        
        int assessId    =   messages.getData().get(1).getAsJsonObject().get("ID").getAsInt();
        JsonArray submissionsForAssessment  =   AssessmentSubmissionsModel.getSubmissionsForAssessment(assessId);
        
        if(submissionsForAssessment != null && submissionsForAssessment.size() > 1)
        {
            System.out.println("\n" + CUITextTools.underline(CUITextTools.changeColour("Submissions", CUITextTools.CYAN)));
            CUITextTools.responseToTable(submissionsForAssessment);
        }
    }
    
    public void modifySubmission()
    {        
        List<String> fieldTitles    =   new ArrayList<>();
        fieldTitles.add(CUITextTools.createFormField("Submission ID", "Enter the submission ID to change"));
        fieldTitles.add(CUITextTools.createFormField("Change attribute", "What is the attribute you want to change?"));
        fieldTitles.add(CUITextTools.createFormField("New value", "What is the new value to change to?"));
        
        List<String> fieldKeys  =   new ArrayList<>();
        fieldKeys.add("subId");
        fieldKeys.add("subAttr");
        fieldKeys.add("subVal");
        
        String[] headers    =   { "Submission ID", "Change attribute", "New value" };
        
        Map<String, String> inputData   =   CUITextTools.getFormInput(fieldTitles, fieldKeys, headers);
        ControllerMessage postData      =   new ControllerMessage().addAll(inputData);
        
        ResponseDataView response       =   (ResponseDataView) RouteHandler.go("postModifySubmission", postData);
        System.out.println(response.getResponseMessage());
    }
    
    public void removeSubmission()
    {
        List<String> fieldTitles    =   new ArrayList<>();
        fieldTitles.add(CUITextTools.createFormField("Submission ID", "Enter the submission ID of the submission to remove"));
        
        List<String> fieldKeys      =   new ArrayList<>();
        fieldKeys.add("subId");
        
        String[] headers    =   { "Submission ID" };
        
        Map<String, String> inputData   =   CUITextTools.getFormInput(fieldTitles, fieldKeys, headers);
        ControllerMessage postData      =   new ControllerMessage().addAll(inputData);
        
        ResponseDataView response       =   (ResponseDataView) RouteHandler.go("postRemoveSubmission", postData);
        System.out.println(response.getResponseMessage());
    }
    
    public void markSubmission()
    { 
        List<String> fieldTitles    =   new ArrayList<>();
        fieldTitles.add(CUITextTools.createFormField("Submission ID", "Enter the submission ID of the submission to mark"));
        fieldTitles.add(CUITextTools.createFormField("Grade", "Enter the grade for this submission"));
        fieldTitles.add(CUITextTools.createFormField("Mark", "Enter the mark for this submission"));
        
        List<String> fieldKeys      =   new ArrayList<>();
        fieldKeys.add("subId");
        fieldKeys.add("subGrade");
        fieldKeys.add("subMark");
        
        String[] headers    =   { "Submission ID", "Grade", "Mark" };
        
        Map<String, String> inputData   =   CUITextTools.getFormInput(fieldTitles, fieldKeys, headers);
        ControllerMessage postData      =   new ControllerMessage().addAll(inputData);
        
        ResponseDataView response       =   (ResponseDataView) RouteHandler.go("postMarkSubmission", postData);
        System.out.println(response.getResponseMessage());
    }
    
    public void getStudentsSubmission()
    { 
        List<String> fieldTitles    =   new ArrayList<>();
        fieldTitles.add(CUITextTools.createFormField("Student username", "Enter the username whose submission you are retrieving"));
        
        List<String> fieldKeys      =   new ArrayList<>();
        fieldKeys.add("subUser");
        
        String[] headers    =   { "Student username" };
        
        if(messages.getData() == null || messages.getData().size() <= 1) return; 
        
        int assessId    =   messages.getData().get(1).getAsJsonObject().get("ID").getAsInt();
        Map<String, String> inputData   =   CUITextTools.getFormInput(fieldTitles, fieldKeys, headers);
        inputData.put("assessId", "" + assessId);
        
        ControllerMessage postData      =   new ControllerMessage().addAll(inputData); 
        ResponseDataView response       =   (ResponseDataView) RouteHandler.go("postFindStudentSubmission", postData);
        
        
        if(response.getResponseStatus())
        {
            JsonArray data  =   response.getResponseData().getData();
            CUITextTools.responseToTable(data);
        }
        
        else System.out.println(response.getResponseMessage());
        
    }

    @Override
    protected String getCommandsFile() 
    {
        return "/engine/config/listeners/AssessmentSubmissionsListener.json";
    }
    
    public static void main(String[] args)
    {
        AssessmentSubmissionsView subView    =   (AssessmentSubmissionsView) RouteHandler.go("getAssessmentSubmissions", new Object[] { 3 }, new Class<?>[] { Integer.class }, null);
        subView.display();
        subView.modifySubmission();
    }
    
}
