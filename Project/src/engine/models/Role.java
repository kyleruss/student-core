//====================================
//  KYLE RUSSELL
//  13831056
//  PDC Project
//====================================

package engine.models;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import engine.core.database.Conditional;
import engine.core.database.Join;
import java.sql.SQLException;


public class Role extends Model
{
    public Role()
    {
        super();
    }
    
    public Role(Object id)
    {
        super(id);
    }
    
    public static String getUserRole(String username)
    {
        
        try
        {
            JsonArray arr = new User().builder().join(new Join("users", "role", "role_id", "id", Join.JoinType.INNERR_JOIN)
                                    .filter(new Conditional("users.username", "=", username).literal()))
                                    .select("role.name")
                                    .get();
            
            return arr.get(1).getAsJsonObject().get("NAME").getAsString();
        }
        
        catch(SQLException e)
        {
            return "";
        }
    }
    
    public static int getUserPermissionLevel(String username)
    {
        try
        {
            JsonArray results   =   new User().builder()
                                    .join(new Join("users", "role", "role_id", "id", Join.JoinType.INNERR_JOIN)
                                    .filter(new Conditional("users.username", "=", username).literal()))
                                    .get();
            
            if(results != null && results.size() > 1)
            {
                JsonObject userObj  =   results.get(1).getAsJsonObject();
                return userObj.get("PERMISSION_LEVEL").getAsInt();
            }
            
            else return 0;
        }
        
        catch(SQLException e)
        {
            return 0;
        }
    }
    
    public static JsonArray getRoles()
    {
        try
        {
            return new Role().builder().get();
        }
        
        catch(SQLException e)
        {
            return new JsonArray();
        }
    }

    @Override
    protected void initTable() 
    {
        table       =   "role";
        primaryKey  =   "id";   
    }
}
