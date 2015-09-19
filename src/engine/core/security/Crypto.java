//====================================
//  KYLE RUSSELL
//  13831056
//  PDC Project
//====================================

package engine.core.security;

import engine.config.AuthConfig;
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
            String algorithm    =   (String) AuthConfig.config().get(AuthConfig.HASH_ALGORITHM_KEY);
            //Get encoding format (Default: utf-8)
            String format       =   (String) AuthConfig.config().get(AuthConfig.ENCRYPT_FORMAT_KEY);
            
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
            System.out.println("Failed to make hash, error: " + e.getMessage());
            return null;
        }
    }
    
    //Creates a salt from the postifx
    //For passwords use the users username as postfix
    //An additional prefix is added (see AuthConfig) to salt
    public static String salt(String postfix)
    {
        String prefix  =   (String) AuthConfig.config().get(AuthConfig.SALT_PREFIX_KEY);
        return prefix + postfix;
    }
}
