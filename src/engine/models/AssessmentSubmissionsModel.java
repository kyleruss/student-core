 
package engine.models;

import com.google.gson.JsonArray;
import engine.core.database.Conditional;
import engine.core.database.Join;
import java.sql.SQLException;


public class AssessmentSubmissionsModel extends Model
{
    public AssessmentSubmissionsModel()
    {
        super();
    }
    
    public AssessmentSubmissionsModel(Object id)
    {
        super(id);
    }
    
    public static JsonArray getSubmissionDetails(int assessId)
    {
        try
        {
            JsonArray results   =   new AssessmentSubmissionsModel().builder()
                    .join(new Join("assessment_submissions", "assessment", Join.JoinType.INNERR_JOIN)
                            .filter(new Conditional("assessment_id", "=", "" + assessId)))
                    .select("assessment.*")
                    .get();
            
         //   System.out.println(results);
            return results;
        }
        
        catch(SQLException e)
        {
            System.out.println("[SQL Exception] " + e.getMessage());
            return new JsonArray();
        }
    }
    
    public static JsonArray getSubmissionsForAssessment(int assessId)
    {
        try
        {
            JsonArray results =  new AssessmentSubmissionsModel().builder()
                .join(new Join("assessment_submissions", "users", "user_id", "username", Join.JoinType.INNERR_JOIN)
                        .filter(new Conditional("assessment_id", "=", "" + assessId)))
                .select("assessment_submissions.id", "Submission ID")
                .select("user_id", "Username")
                .select("users.firstname", "First name")
                .select("users.lastname", "Last name")
                .select("date_submitted", "Date submitted")
                .select("alpha_grade", "Grade")
                .select("mark")
                .get();
            
            return results;
        }
        
        catch(SQLException e)
        {
            System.out.println("[SQL Exception] " + e.getMessage());
            return new JsonArray();
        }
    }
    
    public static JsonArray getSubmissionsForStudentAssessment(String userId, int assessId)
    {
        try
        {
            JsonArray results =  new AssessmentSubmissionsModel().builder()
                .join(new Join("assessment_submissions", "users", "user_id", "username", Join.JoinType.INNERR_JOIN)
                        .filter(new Conditional("assessment_id", "=", "" + assessId))
                        .filter(new Conditional("user_id", "=", userId).literal()))
                .select("assessment_submissions.id", "Submission ID")
                .select("user_id", "Username")
                .select("users.firstname", "First name")
                .select("users.lastname", "Last name")
                .select("date_submitted", "Date submitted")
                .select("alpha_grade", "Grade")
                .select("mark")
                .get();
            
            return results;
        }
        
        catch(SQLException e)
        {
            System.out.println("[SQL Exception] " + e.getMessage());
            return new JsonArray();
        }
    }
    
    public static JsonArray getSubmissionsForStudent(String userId, int classId)
    {
        try
        {
            JsonArray results   =   new AssessmentSubmissionsModel().builder()
            .join(new Join("assessment_submissions", "users", "user_id", "username", Join.JoinType.INNERR_JOIN)
                    .filter(new Conditional("user_id", "=", userId).literal()))
            .join(new Join("assessment_submissions", "assessment", Join.JoinType.INNERR_JOIN)
                    .filter(new Conditional("assessment.class_id", "=", "" + classId)))
            .select("assessment_submissions.id", "Submission ID")
            .select("user_id", "Username")
            .select("users.firstname", "First name")
            .select("users.lastname", "Last name")
            .select("assessment_id", "Assessment ID")
            .select("assessment.name", "Assessment name")
            .select("date_submitted", "Date submitted")
            .select("alpha_grade", "Grade")
            .select("mark")
            .get();
            
            return results;
        }
        
        catch(SQLException e)
        {
            System.out.println("[SQL Exception] " + e.getMessage());
            return new JsonArray();
        }
    }
    
    @Override
    protected void initTable() 
    {
        table       =   "assessment_submissions";
        primaryKey  =   "id";
    }
    
}
