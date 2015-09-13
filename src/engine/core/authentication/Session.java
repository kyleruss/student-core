//####################################
//  KYLE RUSSELL
//  13831056
//  PDC Project
//####################################

package engine.core.authentication;

import engine.models.User;


public class Session
{
    private final User authUser;
    
    public Session(User authUser)
    {
        this.authUser    =   authUser;
    }
    
    public User getUser()
    {
        return authUser;
    }
}
