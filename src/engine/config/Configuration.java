//####################################
//  KYLE RUSSELL
//  13831056
//  PDC Project
//####################################

package engine.config;

import java.util.Properties;

public abstract class Configuration
{
    //Configurations stored in key-value entries
    //Initialize entries in initConfig() in implementation
    protected Properties config;
    
    //Creates a config file with default settings
    public Configuration()
    {
        this(new Properties());
    }
    
    //Creates a config file with custom settings
    public Configuration(Properties config)
    {
        this.config =   config;
        initConfig();
    }
    
    //initConfig() should initialize and handle all relavent configs
    //typical implementation would be to push config entries onto this.config
    //should also handle i/o and parsing if config is remote
    protected abstract void initConfig();
    
    //Returns the Configurations active config
    public Properties getConfig()
    {
        return config;
    }
    
    //Returns a configs value
    //care - can return null
    public Object get(String config_name)
    {
        return config.get(config_name);//.getProperty(config_name);
    }   
    
    //Adds a new config setting
    //Does not override existing entries to consider passed Properties
    //See change() for mutating existing config
    protected void add(String config_name, Object config_value)
    {
       if(!config.containsKey(config_name)) 
            config.put(config_name, config_value);
    }  
    
    //Changes an existing config setting
    //entry is NOT added if it does not exist
    protected void change(String config_name, Object config_value)
    {
        if(config.containsKey(config_name))
            config.put(config_name, config_value);
    }
    
}
