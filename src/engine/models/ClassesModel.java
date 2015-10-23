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

public class ClassesModel extends Model
{
    public ClassesModel()
    {
        super();
    }
    
    public ClassesModel(Object id)
    {
        super(id);
    }
    
    public static JsonArray getTeacherContact(int classId) throws SQLException
    {
        return new ClassesModel().builder()
                .join(new Join("classes", "users", "teacher_id", "username", Join.JoinType.INNERR_JOIN).filter(new Conditional("id", "=", "" + classId)))
                .select("users.firstname", "First name")
                .select("users.lastname", "Last name")
                .select("users.contact_ph", "Contact phone")
                .select("users.contact_email", "Contact email")
                .get();
    }
    
    public static JsonArray getAllClasses()
    {
        try
        {
            return new ClassesModel().builder()
                    .join("classes", "department", "dept_id", "id", Join.JoinType.INNERR_JOIN)
                    .select("classes.*")
                    .select("department.name", "dept_name")
                    .get();
                    
                    
        }
        
        catch(SQLException e)
        {
            return new JsonArray();
        }
    }
    
    public static JsonArray getClassDetails(int classID)
    {
        try
        {
            JsonArray results   =   new ClassesModel().builder()
                                .join(new Join("classes", "users", "teacher_id", "username", Join.JoinType.INNERR_JOIN).filter(new Conditional("id", "=", "" + classID)))
                                .join("classes", "department", "dept_id", "id", Join.JoinType.INNERR_JOIN)
                                .select("department.name", "dept_name")
                                .select("users.firstname", "teacher_firstname")
                                .select("users.lastname", "teacher_lastname")
                                .select("users.contact_ph", "teacher_phone")
                                .select("users.contact_email", "teacher_email")
                                .select("classes.name", "class_name")
                                .select("classes.description", "class_desc")
                                .select("classes.created_date", "class_created")
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
        table       =   "classes";
        primaryKey  =   "id";
    }
    
    
    
}
