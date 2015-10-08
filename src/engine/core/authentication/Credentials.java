
package engine.core.authentication;

public class Credentials
{
    private String username;
    private String password;
    private String dateSaved;
    private String lastLogged;

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

    public String getUsername()
    {
        return username;
    }

    public String getPassword()
    {
        return password;
    }

    public String getDateSaved()
    {
        return dateSaved;
    }

    public String getLastLogged()
    {
        return lastLogged;
    }

    public void setUsername(String username)
    {
        this.username   =   username;
    }

    public void setPassword(String password)
    {
        this.password   =   password;
    }

    public void setDateSaved(String dateSaved)
    {
       this.dateSaved   =   dateSaved; 
    }

    public void setLastLogged(String lastLogged)
    {
        this.lastLogged =   lastLogged;
    }
}
