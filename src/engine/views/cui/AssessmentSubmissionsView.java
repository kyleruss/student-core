
package engine.views.cui;

import com.google.gson.JsonArray;
import engine.controllers.ControllerMessage;
import engine.core.RouteHandler;
import engine.models.AssessmentSubmissionsModel;
import engine.views.AbstractView;
import engine.views.cui.Utilities.CUITextTools;


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
                "View and manage submissions for this assessment", 
                "/" + ((messages.getData().size() > 1)? messages.getData().get(1).getAsJsonObject().get("NAME").getAsString() + "/" : "" ) +  "submissions/" 
        );
    }
    
    @Override
    public void display()
    {
        super.display();
        
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
        
    }
    
    public void removeSubmission()
    {
        
    }
    
    public void markSubmission()
    {
        
    }
    
    public void getStudentsSubmission()
    {
        
    }

    @Override
    protected String getCommandsFile() 
    {
        return "/engine/config/listeners/AssessmentSubmissionsListener.json";
    }
    
    public static void main(String[] args)
    {
        AssessmentSubmissionsView view  =   (AssessmentSubmissionsView) RouteHandler.go("getAssessmentSubmissions", new Object[] { 3 }, new Class<?>[] { Integer.class }, null);
        view.display();
    }
    
}
