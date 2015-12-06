//====================================
//	Kyle Russell
//	jdamvc
//	ConnectionParams
//====================================

package engine.core;

import engine.config.DatabaseConfig;
import java.text.MessageFormat;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
        
public class ConnectionParams 
{
    private String server; //database server
    private int port; //database port - default: 1527
    private String user; //database username
    private String pass;//database password
    private String database; //the apps database 
    private String driver; //databases driver
    private Properties attributes; //connection properties
    
    //Creates a DataConnection with default Database configurations
    public ConnectionParams()
    {
        this(DatabaseConfig.DATABASE);
    }
    
    public ConnectionParams(String database)
    {
        this(DatabaseConfig.SERVER, DatabaseConfig.PORT, DatabaseConfig.DB_USERNAME, DatabaseConfig.DB_PASSWORD, database, DatabaseConfig.DRIVER);
    }
    
    //Creates a DataConnection with custom configurations
    public ConnectionParams(String server, int port, String user, String pass, String database, String driver)
    {
       this.server            =     server;
       this.port              =     port;
       this.user              =     user;
       this.pass              =     pass;
       this.database          =     database;
       this.driver            =     driver;
   
    }
    
    //Creates a DataConnection with custom config & additional attributes
    //to add to connection string
    public ConnectionParams(String server, int port, String user, String pass, String database, String driver, Properties attributes)
    {
        this(server, port, user, pass, database, driver);
        this.attributes =   attributes;
    }
    
    //-------------------------------------
    //      CREATE CONNECTION STRING
    //-------------------------------------

    public String getConnectionString()
    {
        String attrs_string =   "";
        Properties attrs    =   attributes;
        attrs               =   (attrs == null)? new Properties() : attrs;
        
        //user & pass is attribute => push to db_attrs
        attrs.put("user", user);
        attrs.put("password", pass);
        
        //add attributes to attrs_string
        //TEMPLATE: attr_name=attr_value;
        Iterator<Map.Entry<Object, Object>> conf_iterator  =   attrs.entrySet().iterator();
        while(conf_iterator.hasNext())
        {
            Map.Entry<Object, Object> conf_property =   conf_iterator.next();
            attrs_string += MessageFormat.format(";{0}={1}", conf_property.getKey(), conf_property.getValue());
        }
        
        //TEMPLATE: DB_DRIVER://DB_SERVER[:DB_PORT]/DB_DATABASE;[attrs]
        return  MessageFormat.format
        (
                "{0}://{1}:{2}/{3}{4}", 
                driver, server, "" + port, database, attrs_string
        );
        
    }
    
    //Returns db server
    public String getServer()
    {
        return server;
    }
    
    //Returns db port
    public int getPort()
    {
        return port;
    }
    
    //Returns db user
    public String getUser()
    {
        return user;
    }
    
    //Returns db password
    public String getPass()
    {
        return pass;
    }
    
    //Returns active database
    public String getDatabase()
    {
        return database;
    }
    
    //Returns db driver
    public String getDriver()
    {
        return driver;
    }
    
    //Returns active attributes 
    public Properties getAttributes()
    {
        return attributes;
    }
}
