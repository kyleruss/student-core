
package engine.controllers;

import engine.views.View;
import engine.views.cui.Login;


public class UserController
{
    public View getLogin()
    {
        return new Login();
    }
}
