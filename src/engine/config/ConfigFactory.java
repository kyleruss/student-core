
package engine.config;


public class ConfigFactory
{
    //------------------------------------------------------
    //          CONFIG NAMES
    //------------------------------------------------------
    public static final String APP_CONFIG   =   "app";
    public static final String AUTH_CONFIG  =   "auth";
    public static final String DB_CONFIG    =   "database";
    public static final String LOG_CONFIG   =   "log";
    //------------------------------------------------------
    
    public static Configuration get(String configName)
    {
        switch(configName.toLowerCase())
        {
            case APP_CONFIG: return new AppConfig();
            case AUTH_CONFIG: return new AuthConfig();
            case DB_CONFIG: return new DatabaseConfig();
            case LOG_CONFIG: return new LoggingConfig();
            default: return null;
        }
    }
    
    public static Object get(String configName, String configKey)
    {
        Configuration config    =   get(configName);
        if(config != null) return config.get(configKey);
        else return null;
    }
}
