
package engine.controllers;

import engine.parsers.JsonParser;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;


public class ControllerMessage 
{
    Map<String, String> messages;
    
    public ControllerMessage()
    {
        messages    =   new LinkedHashMap<>();
    }
    
    public int numMessages()
    {
        return messages.size();
    }
    
    public ControllerMessage add(String name, String message)
    {
        messages.put(name, message);
        return this;
    }
    
    public String getMessage(String name)
    {
        return messages.get(name);
    }
    
    public boolean messageExists(String name)
    {
        return messages.containsKey(name);
    }
    
    public Iterator<String> getAllMessages()
    {
        return messages.values().iterator();
    }
    
    public Iterator<String> getAllMessageKeys()
    {
        return messages.keySet().iterator();
    }
    
    public String makeJson()
    {
        return JsonParser.parsePretty(messages);
    }
}
