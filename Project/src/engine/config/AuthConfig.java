//====================================
//	Kyle Russell
//	jdamvc
//	AuthConfig
//====================================

package engine.config;

public class AuthConfig 
{
    //---------------------------------------------------------------------
    //                              AUTH CONFIG
    //---------------------------------------------------------------------
    
    //The table name used for user authentication
    public static final String AUTH_TABLE           =   "users";
    
    //The hashing algorithm used for passwords
    public static final String HASH_ALGORITHM       =   "SHA-1";
    
    //A prefix used for the salt in the password hash
    public static final String SALT_PREFIX          =   "vb+FwcR~Sj+bq5imRBJd3%L";
    
    //The encoding format used in hashing
    public static final String ENC_FORMAT           =   "utf-8";
    
    //The primary key column name in the auth table
    public static final String USERNAME_COL         =   "username";
    
    //The password column in the auth table
    public static final String PASSWORD_COL         =   "password";
    
}
