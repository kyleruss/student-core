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
    
    public static JsonArray getStudentsEnrolledIn(int classId) throws SQLException
    {
        return new ClassEnrolmentModel().builder()
                .join(new Join("class_enrolments", "users", "user_id", "username", Join.JoinType.INNERR_JOIN).filter(new Conditional("class_id", "=", "" + classId)))
                .select("users.username", "User ID")
                .select("users.firstname", "First name")
                .select("users.lastname", "Last name")
                .select("users.contact_email", "Email")
                .get();
    }
    
    @Override
    protected void initTable() 
    {
        table       =   "class_enrolments";
        primaryKey  =   "id";
    }
    
}
