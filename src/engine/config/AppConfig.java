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
    public static final String DEBUG_MODE       =   "app_debug";
    public static final String GUI_MODE         =   "enable_gui";
    public static final String APP_NAME         =   "name";
    public static final String GUI_HEIGHT_MULTI =   "height_multiplier";
    public static final String GUI_WIDTH_MULTI  =   "width_multiplier";
    
    @Override
    protected void initConfig() 
    {
        add(DEBUG_MODE, true);
        add(GUI_MODE, true);
        add(APP_NAME, "StudentCore");
        add(GUI_HEIGHT_MULTI, 0.5);
        add(GUI_WIDTH_MULTI, 0.4);
    }
    
}
