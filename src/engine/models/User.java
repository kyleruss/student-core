
package engine.models;

import engine.config.AuthConfig;
import static engine.core.security.Crypto.makeHash;
import static engine.core.security.Crypto.salt;

public class User extends Model 
{
    
    public User()
    {
        
    }
    
    public User(String id)
    {
        super(id);
    }

    @Override
    protected void initTable()
    {
        table       =   (String) AuthConfig.config().get(AuthConfig.AUTH_TABLE_KEY);
        primaryKey  =   (String) AuthConfig.config().get(AuthConfig.USERNAME_COL_KEY);
    }
    
    public static void main(String[] args)
    {
        String salt =   salt("kyleruss");
        String hash =   makeHash(salt, "kyleruss123");
        
        User user   =   new User();
        
        user.set("username", "kyleruss");
        user.set("password", hash);
        user.set("firstname", "kyle");
        user.set("lastname", "russ");
        user.set("gender", 1);
        user.set("birthdate", "1995-07-04");
        user.set("contact_ph", "09-5347190");
        user.set("contact_email", "kyleruss2030@gmail.com");
        user.set("role_id", 1);
        user.set("ethnicity", "NZ/European");
        
        if(user.save()) System.out.println("Saved!");
    }
    
}
