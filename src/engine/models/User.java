//====================================
//  KYLE RUSSELL
//  13831056
//  PDC Project
//====================================

package engine.models;

import com.google.gson.JsonArray;
import engine.config.AuthConfig;
import engine.core.database.Conditional;
import engine.core.database.Join;
import java.sql.SQLException;

public class User extends Model 
{
    
    public User()
    {
        super();
    }
    
    public User(Object id)
    {
        super(id);
    }
    

    @Override
    protected void initTable()
    {
        table       =   AuthConfig.AUTH_TABLE;
        primaryKey  =   AuthConfig.USERNAME_COL;
    }
    
    public static JsonArray getAllUsers()
    {
        try
        {
            return new User().builder()
                    .join("users", "role", "role_id", "id", Join.JoinType.INNERR_JOIN)
                    .select("users.*")
                    .select("role.name", "role_name")
                    .get();
        }
        
        catch(SQLException e)
        {
            return new JsonArray();
        }
    }
    
    public static JsonArray searchUser(String col, String operator, String val)
    {
        try
        {
            User user           =   new User();
            boolean literal     =   user.getColumn(col).isLiteral();
            Conditional cond    =   new Conditional(col, operator, val);
            if(literal) cond.literal();
            
            return user.builder()                    
                    .join(new Join("users", "role", "role_id", "id", Join.JoinType.INNERR_JOIN)
                        .filter(cond))
                    .select("users.*")
                    .select("role.name", "role_name")
                    .get();
        }
        
        catch(SQLException e)
        {
            return new JsonArray();
        }
    }
    
    public static JsonArray getUserWithJoins(String username)
    {
        try
        {
            return new User().builder()
                    .join(new Join("users", "medical", "medical_id", "id", Join.JoinType.INNERR_JOIN))
                    .join(new Join("medical", "emergency_contact", "contact_id", "id", Join.JoinType.INNERR_JOIN)
                            .filter(new Conditional("users.username", "=", username).literal()))
                    .select("users.*")
                    .select("medical.description", "medical_desc")
                    .select("medical.id", "medical_id")
                    .select("emergency_contact.firstname", "contact_firstname")
                    .select("emergency_contact.lastname", "contact_lastname")
                    .select("emergency_contact.contact_ph", "e_contact_ph")
                    .select("emergency_contact.contact_email", "e_contact_email")
                    .select("emergency_contact.relationship", "e_contact_relationship")
                    .get();
        }
        
        catch(SQLException e)
        {
            return new JsonArray();
        }
    }
}
