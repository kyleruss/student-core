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


public class Role extends Model
{
    public Role()
    {
        super();
    }
    
    public Role(String id)
    {
        super(id);
    }
    
    public static String getUserRole(String username)
    {
        
        try
        {
            JsonArray arr = new User().builder().join(new Join("users", "role", "role_id", "id", Join.JoinType.INNERR_JOIN)
                                    .filter(new Conditional("users.username", "=", "\'" + username + "\'")))
                                    .select("role.name")
                                    .get();
            
            return arr.get(1).getAsJsonObject().get("NAME").getAsString();
        }
        
        catch(SQLException e)
        {
            return "";
        }
    }

    @Override
    protected void initTable() 
    {
        table       =   "role";
        primaryKey  =   "id";   
    }
}
