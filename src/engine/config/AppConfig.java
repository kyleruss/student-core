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
    
    @Override
    protected void initConfig() 
    {
        add(DEBUG_MODE, true);
    }
    
}
