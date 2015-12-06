//====================================
//	Kyle Russell
//	jdamvc
//	AppConfig
//====================================

package engine.config;


public class AppConfig
{
    //---------------------------------------------------------------------
    //                              APP CONFIG
    //---------------------------------------------------------------------
    
    //Enabling debug mode will show all debug output messages
    public static final boolean DEBUG_MODE      =   false;
    
    //Enabling GUI mode will show the applications GUI, disable for CUI
    public static final boolean GUI_MODE        =   true;
    
    //The applications name shown on winodws, titles etc.
    public static final String APP_NAME         =   "StudentCore";
    
    //The width of the GUI window
    public static final int WINDOW_WIDTH        =   768;
    
    //The height of the GUI window
    public static final int WINDOW_HEIGHT       =   648;
    
    //The relative path to the apps images directory
    public static final String RESOURCE_DIR     =   "data/images/";
    
    //Enabling will allow users to save credentials
    public static final boolean ALLOW_CRED_SAVE =   true;
    
    //The path to the user stored credentials file
    public static final String CRED_SAVE_FILE   =   "data/SavedUsers.txt";
    
    //Notifications are checked and updated every NOTIFICATION_TIME ms
    public static final int NOTIFICATION_TIME   =   30000;
    
    //Enable to show coloured text in the CUI
    //Colours have only been tested on linux
    public static final boolean CUI_COLOURS     =   false;
}
