//####################################
//  KYLE RUSSELL
//  13831056
//  PDC Project
//####################################

package engine.Controllers;

import engine.Views.View;
import engine.Views.cui.Home;

public class BaseController 
{
    public View getHome()
    {
        return new Home();
    }
}
