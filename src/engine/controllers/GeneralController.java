//####################################
//  KYLE RUSSELL
//  13831056
//  PDC Project
//####################################

package engine.controllers;

import engine.views.View;
import engine.views.cui.HomeView;

public class GeneralController 
{
    public View getHome()
    {
        return new HomeView();
    }
}
