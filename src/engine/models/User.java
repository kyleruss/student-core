//====================================
//  KYLE RUSSELL
//  13831056
//  PDC Project
//====================================

package engine.models;

import engine.config.AuthConfig;

public class User extends Model 
{
    
    public User()
    {
        
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
}
