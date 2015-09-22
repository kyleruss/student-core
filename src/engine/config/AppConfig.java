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
    public static final String APP_NAME     =   "name";
    
    @Override
    protected void initConfig() 
    {
        add(DEBUG_MODE, true);
        add(GUI_MODE, false);
        add(APP_NAME, "StudentCore");
    }
    
}
