
package engine.controllers;

import engine.core.authentication.Auth;
import engine.views.View;
import engine.views.cui.LoginView;
import engine.views.cui.RegisterView;
import engine.views.cui.ResponseDataView;


public class UserController extends Controller
{   
    public UserController()
    {
        super();
    }
    
    public UserController(ControllerMessage postData)
    {
        super(postData);
    }
    
    public UserController(ControllerMessage postData, RequestType requestType)
    {
        super(postData, requestType);
    }
    
    public View getLogin()
    {
        return new LoginView();
    }
    
    public View postLogin()
    {
        final String invalidInputMesage         =   "Invalid information, please check your fields";
        final String invalidAttemptMessage      =   "Invalid username or password";
        final String successMessage             =   "Successfully logged in";
        
        if(!validatePostData(new String[]{"loginUsername", "loginPassword"}))
            return new ResponseDataView(invalidInputMesage, false);
        else
        {
            String loginUsername    =   postData.getMessage("loginUsername");
            String loginPassword    =   postData.getMessage("loginPassword");
            
            if(Auth.login(loginUsername, loginPassword) != null)
                return new ResponseDataView(successMessage, true);
            else
                return new ResponseDataView(invalidAttemptMessage, false);
        }
            
    }
    
    public View getRegister()
    {
        return new RegisterView();
    }
}
