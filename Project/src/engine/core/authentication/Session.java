//====================================
//	Kyle Russell
//	jdamvc
//	Session
//====================================

package engine.core.authentication;

import engine.models.User;

//---------------------------------------
//             SESSION
//---------------------------------------
//- Session holds the auth User
//- Agent stores an active session of logged in users
//- Logged in users are accessible from session by getUser()

public class Session
{
    //The sessions auth user
    //User is logged in
    private final User authUser;
    
    //Create a session for user authUser
    public Session(User authUser)
    {
        this.authUser    =   authUser;
    }
    
    //Returns the sessions auth user
    public User getUser()
    {
        return authUser;
    }
}
