//####################################
//  KYLE RUSSELL
//  13831056
//  PDC Project
//####################################

package engine.core;

import engine.config.DatabaseConfig;
import java.text.MessageFormat;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
        
public class DataConnection 
{
    private String db_server; //database server
    private String db_port; //database port - default: 1527
    private String db_user; //database username
    private String db_pass;//database password
    private String db_database; //the apps database 
    private String db_driver; //databases driver
    private String db_schema;
    private Properties attributes; //connection properties
    
    //Creates a DataConnection with default Database configurations
    public DataConnection()
    {
       this(DatabaseConfig.config().getConfig());
    }
    
    //Creates a DataConnection with custom configurations
    public DataConnection(Properties db_conf)
    {
       db_server            =   db_conf.getProperty(DatabaseConfig.SERVER_KEY);
       db_port              =   db_conf.getProperty(DatabaseConfig.PORT_KEY);
       db_user              =   db_conf.getProperty(DatabaseConfig.USER_KEY);
       db_pass              =   db_conf.getProperty(DatabaseConfig.PASS_KEY);
       db_database          =   db_conf.getProperty(DatabaseConfig.DB_KEY);
       db_driver            =   db_conf.getProperty(DatabaseConfig.DRIVER_KEY);
       db_schema            =   db_conf.getProperty(DatabaseConfig.SCHEMA_KEY);
    }
    
    //Creates a DataConnection with custom config & additional attributes
    //to add to connection string
    public DataConnection(Properties db_conf, Properties attributes)
    {
        this(db_conf);
        this.attributes =   attributes;
    }
    
    //-------------------------------------
    //      CREATE CONNECTION STRING
    //-------------------------------------

    public String getConnectionString()
    {
        String attrs_string = "";
        Properties db_attrs = attributes;
        db_attrs   =   (db_attrs == null)? new Properties() : db_attrs;
        
        //user & pass is attribute => push to db_attrs
        db_attrs.put("user", db_user);
        db_attrs.put("password", db_pass);
        
        //add attributes to attrs_string
        //TEMPLATE: attr_name=attr_value;
        Iterator<Map.Entry<Object, Object>> conf_iterator  =   db_attrs.entrySet().iterator();
        while(conf_iterator.hasNext())
        {
            Map.Entry<Object, Object> conf_property =   conf_iterator.next();
            attrs_string += MessageFormat.format(";{0}={1}", conf_property.getKey(), conf_property.getValue());
        }
        
        //TEMPLATE: DB_DRIVER://DB_SERVER[:DB_PORT]/DB_DATABASE;[attrs]
        return  MessageFormat.format
        (
                "{0}://{1}:{2}/{3}{4}", 
                db_driver, db_server, db_port, db_database, attrs_string
        );
    }
    
    //Returns db server
    public String getServer()
    {
        return db_server;
    }
    
    //Returns db port
    public String getPort()
    {
        return db_port;
    }
    
    //Returns db user
    public String getUser()
    {
        return db_user;
    }
    
    //Returns db password
    public String getPass()
    {
        return db_pass;
    }
    
    //Returns active database
    public String getDatabase()
    {
        return db_database;
    }
    
    //Returns db driver
    public String getDriver()
    {
        return db_driver;
    }
    
    //Returns the dbs using schema
    public String getSchema()
    {
        return db_schema;
    }
    
    //Returns active attributes 
    public Properties getAttributes()
    {
        return attributes;
    }
}
