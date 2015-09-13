//####################################
//  KYLE RUSSELL
//  13831056
//  PDC Project
//####################################

package engine.core.security;

import engine.config.AuthConfig;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;


public class Crypto
{
    public static String makeHash(String salt, String input)
    {
        
        try
        {
            String hash;
            String saltedInput  =   salt + input;
            String algorithm    =   (String) AuthConfig.config().get(AuthConfig.HASH_ALGORITHM_KEY);
            String format       =   (String) AuthConfig.config().get(AuthConfig.ENCRYPT_FORMAT_KEY);
            
            MessageDigest enc   =   MessageDigest.getInstance(algorithm);
            enc.update(saltedInput.getBytes(format));
            byte[] digested     =   enc.digest();
            hash                =   Base64.getEncoder().encodeToString(digested);
            return hash;
        }
        
        catch (NoSuchAlgorithmException | UnsupportedEncodingException e)
        {
            System.out.println("Failed to make hash, error: " + e.getMessage());
            return null;
        }
    }
    
    public static String salt(String postfix)
    {
        String prefix  =   (String) AuthConfig.config().get(AuthConfig.SALT_PREFIX_KEY);
        return prefix + postfix;
    }
}
