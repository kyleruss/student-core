//====================================
//	Kyle Russell
//	jdamvc
//	Credentials
//====================================

package engine.core.authentication;

public class Credentials
{
    private String username; //Stored username
    private String password; //Stored password (hashed)
    private String dateSaved; //the last saved date of the credentials
    private String lastLogged; //the date the user last logged in

    public Credentials()
    {
        this(null, null, null, null);
    }

    public Credentials(String username, String password, String dateSaved, String lastLogged)
    {
        this.username   =   username;
        this.password   =   password;
        this.dateSaved  =   dateSaved;
        this.lastLogged =   lastLogged;
    }

    //Returns the stored username
    public String getUsername()
    {
        return username;
    }

    //Returns the stored password
    public String getPassword()
    {
        return password;
    }

    //Returns the stored saved date
    public String getDateSaved()
    {
        return dateSaved;
    }

    //Returns the stored last logged in date
    public String getLastLogged()
    {
        return lastLogged;
    }

    //Set the stored username
    public void setUsername(String username)
    {
        this.username   =   username;
    }

    //Set the stored password
    //Password should be hashed and not store plain-text
    public void setPassword(String password)
    {
        this.password   =   password;
    }

    //Set the stored last saved date
    public void setDateSaved(String dateSaved)
    {
       this.dateSaved   =   dateSaved; 
    }

    //Set the stored last logged in date
    public void setLastLogged(String lastLogged)
    {
        this.lastLogged =   lastLogged;
    }
}
