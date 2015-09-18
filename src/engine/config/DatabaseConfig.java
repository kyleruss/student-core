//====================================
//  KYLE RUSSELL
//  13831056
//  PDC Project
//====================================

package engine.config;

import java.util.Properties;


public class DatabaseConfig extends Configuration
{ 
    //-------------------------------------------------------
    //                      CONFIG KEYS
    //-------------------------------------------------------
    public static final String SERVER_KEY   =   "server";
    public static final String PORT_KEY     =   "port";
    public static final String USER_KEY     =   "user";
    public static final String PASS_KEY     =   "password";
    public static final String DB_KEY       =   "daatabase";
    public static final String DRIVER_KEY   =   "driver";
    public static final String SCHEMA_KEY   =   "schema";
    
    //-------------------------------------------------------
    //                  MODEL KEYS
    //-------------------------------------------------------
    public static final String DEFAULT_PRIMARY  =   "id";
    //-------------------------------------------------------
    
    
    //Creates a default db config
    public DatabaseConfig()
    {
        super();
    }
    
    //Creates a db config with custom params
    public DatabaseConfig(Properties config)
    {
        super(config);
    }
    
    //Default config settings
    @Override
    protected void initConfig()
    {
        add(SERVER_KEY, "localhost"); //database server
        add(PORT_KEY, "1527"); //database port - default: 1527
        add(USER_KEY, "kyleruss"); //database server username
        add(PASS_KEY, "fgsmg2"); //database server password
        add(DB_KEY, "School"); //active using database
        add(DRIVER_KEY, "jdbc:derby"); //db connection driver
        add(SCHEMA_KEY, "APP");
        add(DEFAULT_PRIMARY, "id");
    }
    
    //factory => default db config
    public static DatabaseConfig config()
    {
        return new DatabaseConfig();
    }
    
    //factory => db config with custom params
    public static DatabaseConfig config(Properties config)
    {
        return new DatabaseConfig(config);
    }
}
