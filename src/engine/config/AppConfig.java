//====================================
//  KYLE RUSSELL
//  13831056
//  PDC Project
//====================================

package engine.config;


public class AppConfig extends Configuration
{

    //---------------------------------------------------------------------
    //                              CONFIG KEYS
    //---------------------------------------------------------------------
    public static final String DEBUG_MODE   =   "app_debug";
    public static final String GUI_MODE     =   "enable_gui";
    
    @Override
    protected void initConfig() 
    {
        add(DEBUG_MODE, true);
        add(GUI_MODE, false);
    }
    
}
