
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
    
    public static JsonArray getStaffInDept(int deptId)
    {
        try
        {
            JsonArray results   =   new StaffModel().builder()
                    .join(new Join("staff", "users", "user_id", "username", Join.JoinType.INNERR_JOIN)
                        .filter(new Conditional("dept_id", "=", "" + deptId)))
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
    
    @Override
    protected void initTable() 
    {
        table       =   "department";
        primaryKey  =   "id";
    }
    
}
