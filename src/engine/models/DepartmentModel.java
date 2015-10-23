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


public class DepartmentModel extends Model
{
    public DepartmentModel()
    {
        super();
    }
    
    public DepartmentModel(Object id)
    {
        super(id);
    }
    
    public static JsonArray getStaffInDept(int deptID)
    {
        try
        {
            JsonArray results   =   new StaffModel().builder()
                    .join(new Join("staff", "users", "user_id", "username", Join.JoinType.INNERR_JOIN)
                        .filter(new Conditional("dept_id", "=", "" + deptID)))
                    .select("user_id", "Username")
                    .select("users.firstname", "First name")
                    .select("users.lastname", "Last name")
                    .select("users.contact_ph", "Phone")
                    .select("users.contact_email", "Email")
                    .get();
            return results;
        }
        
        catch(SQLException e)
        {
            return new JsonArray();
        }
    }
    
    public static JsonArray getUsersInDept(int deptID)
    {
        try
        {
            JsonArray results   =   new User().builder()
                                    .where("dept_id", "=", "" + deptID)
                                    .get();
            return results;
        }
        
        catch(SQLException e)
        {
            return new JsonArray();
        }
    }
    
    public static JsonArray getAllDepartments()
    {
        try
        {
            JsonArray results =  new DepartmentModel().builder().get();
            return results;
        }
        
        catch(SQLException e)
        {
            return new JsonArray();
        }
    }
    
    public static JsonArray getHOD(int deptID)
    {
        try
        {
            JsonArray results   =   new DepartmentModel().builder()
                                    .join(new Join("department", "users", "dept_head", "username", Join.JoinType.INNERR_JOIN)
                                        .filter(new Conditional("dept_id", "=", "" + deptID)))
                                    .join("users", "role", "role_id", "id", Join.JoinType.INNERR_JOIN)
                                    .select("users.*")
                                    .select("role.name", "role_name")
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
        table       =   "department";
        primaryKey  =   "id";
    }
    
}
