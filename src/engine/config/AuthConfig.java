//====================================
//  KYLE RUSSELL
//  13831056
//  PDC Project
//====================================

package engine.config;

import java.util.Properties;

public class AuthConfig extends Configuration 
{


    //---------------------------------------------------------------------
    //                              CONFIG KEYS
    //---------------------------------------------------------------------
    public static final String AUTH_TABLE_KEY       =   "auth_table";
    public static final String PERM_TABLE_KEY       =   "perm_table";
    public static final String HASH_ALGORITHM_KEY   =   "crypt_algorithm";
    public static final String ENCRYPT_FORMAT_KEY   =   "enc_format";
    public static final String USERNAME_COL_KEY     =   "user_column";
    public static final String PASSWORD_COL_KEY     =   "pass_column";
    public static final String SALT_PREFIX_KEY      =   "salt_pre";
    //---------------------------------------------------------------------
    
    public AuthConfig()
    {
        super();
    }
    
    public AuthConfig(Properties config)
    {
        super(config);
    }
    
    @Override
    protected void initConfig()
    {
        add(AUTH_TABLE_KEY, "users");
        add(HASH_ALGORITHM_KEY, "SHA-1");
        add(ENCRYPT_FORMAT_KEY, "utf-8");
        add(USERNAME_COL_KEY, "username");
        add(PASSWORD_COL_KEY, "password");
        add(SALT_PREFIX_KEY, "vb+FwcR~Sj+bq5imRBJd3%L");
    }
    
    //factory => default auth config
    public static AuthConfig config()
    {
        return new AuthConfig();
    }
    
    //factory => auth config with custom params
    public static AuthConfig config(Properties config)
    {
        return new AuthConfig(config);
    }
}
