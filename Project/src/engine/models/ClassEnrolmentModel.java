//====================================
//  KYLE RUSSELL
//  13831056
//  PDC Project
//====================================

package engine.models;

import com.google.gson.JsonArray;
import engine.core.database.Conditional;
import engine.core.database.Join;
import java.sql.SQLException;


public class ClassEnrolmentModel extends Model
{
    public ClassEnrolmentModel()
    {
        super();
    }
    
    public ClassEnrolmentModel(Object id)
    {
        super(id);
    }
    
    public static JsonArray getStudentsEnrolledIn(int classId)
    {
        try
        {
            JsonArray results = new ClassEnrolmentModel().builder()
                    .join(new Join("class_enrolments", "users", "user_id", "username", Join.JoinType.INNERR_JOIN).filter(new Conditional("class_id", "=", "" + classId)))
                    .join(new Join("class_enrolments", "classes", "class_id", "id", Join.JoinType.INNERR_JOIN))
                    .select("class_enrolments.id", "enrol_id")
                    .select("users.username", "User ID")
                    .select("users.firstname", "First name")
                    .select("users.lastname", "Last name")
                    .select("users.contact_email", "Email")
                    .select("class_enrolments.semester_num", "semester")
                    .select("classes.name", "class_name")
                    .get();
            return results;
        }
        
        catch(SQLException e)
        {
            return new JsonArray();
        }
    }
    
    public static JsonArray getStudentEnrolments(String username)
    {
        try
        {
            JsonArray results   =   new ClassEnrolmentModel().builder()
                                    .join(new Join("class_enrolments", "classes", "class_id", "id", Join.JoinType.INNERR_JOIN).
                                            filter(new Conditional("user_id", "=", username).literal()))
                                    .select("class_enrolments.*")
                                    .select("classes.name", "class_name")
                                    .select("classes.description", "class_desc")
                                    .select("classes.id", "class_id")
                                    .get();
            
            return results;
        }
        
        catch(SQLException e)
        {
            return new JsonArray();
        }
    }
    
    @Override
    protected void initTable() 
    {
        table       =   "class_enrolments";
        primaryKey  =   "id";
    }
    
}
