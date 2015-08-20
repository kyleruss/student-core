//####################################
//  KYLE RUSSELL
//  13831056
//  PDC Project
//####################################

package engine.controllers;

import engine.views.View;
import engine.views.cui.HomeView;

public class BaseController 
{
    public View getHome()
    {
        return new HomeView();
    }
}
