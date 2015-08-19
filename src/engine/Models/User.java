
package engine.models;

import engine.config.AuthConfig;

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
    
}