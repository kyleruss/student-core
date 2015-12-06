//====================================
//	Kyle Russell
//	jdamvc
//	Crypto
//====================================

package engine.core.authentication;

import engine.config.AuthConfig;
import engine.core.ExceptionOutput;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

//------------------------------------
//             CRYPTO
//------------------------------------
//- Crypto has methods for hashing and salting
//- typically Used to create password hashes to store in DB

public class Crypto
{
    //Creates a salted hash from the input
    //For hash algorithms and encoding format see config 
    //Salts can be made from salt()
    //Returns the hexxed hash string
    public static String makeHash(String salt, String input)
    {
        
        try
        {
            String hash;
            String saltedInput  =   salt + input; //Salt the input
            
            //Get hashing algorithm (Default: SHA-1)
            String algorithm    =   AuthConfig.HASH_ALGORITHM;
            //Get encoding format (Default: utf-8)
            String format       =   AuthConfig.ENC_FORMAT;
            
            //Make hash bytes 
            MessageDigest enc   =   MessageDigest.getInstance(algorithm);
            enc.update(saltedInput.getBytes(format));
            byte[] digested     =   enc.digest();
            
            //Get and return hexxed hash
            hash                =   Base64.getEncoder().encodeToString(digested);
            return hash;
        }
        
        catch (NoSuchAlgorithmException | UnsupportedEncodingException e)
        {
            ExceptionOutput.output("Failed to make hash, error: " + e.getMessage(), ExceptionOutput.OutputType.DEBUG);
            return null;
        }
    }
    
    //Creates a salt from the postifx
    //For passwords use the users username as postfix
    //An additional prefix is added (see AuthConfig) to salt
    public static String salt(String postfix)
    {
        String prefix  =   AuthConfig.SALT_PREFIX;
        return prefix + postfix;
    }
}
