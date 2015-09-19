//====================================
//  KYLE RUSSELL
//  13831056
//  PDC Project
//====================================

package engine.core;

//-------------------------------------
//          COMMANDEXECUTE
//-------------------------------------
//- Implementors identify as able to execute commands
//- Typically views should implement this

public interface CommandExecute 
{
    //Executes/fires a command and typical behaviour:
    //Should create a command from fetched commands
    //and perform the Commands call() 
    public void fire(String command, Object instance);
}
