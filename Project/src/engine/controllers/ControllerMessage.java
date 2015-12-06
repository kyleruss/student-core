//====================================
//	Kyle Russell
//	jdamvc
//	ControllerMessage
//====================================

package engine.controllers;

import com.google.gson.JsonArray;
import engine.core.JsonParser;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;


public class ControllerMessage 
{
    Map<String, Object> messages; //The messages to be passed to controller/view
    JsonArray data; //Data from result that can be passed
    
    //Creates controller with empty data and messages
    //Should be added to before passing
    public ControllerMessage()
    {
        this(new JsonArray());
    }
    
    //Creates controller with data
    public ControllerMessage(JsonArray data)
    {
        messages    =   new LinkedHashMap<>();
        this.data   =   data;
    }
    
    //Returns the number of messages
    public int numMessages()
    {
        return messages.size();
    }
    
    //Returns the message data
    public JsonArray getData()
    {
        return data;
    }
    
    //Adds a new message
    public ControllerMessage add(String name, Object message)
    {
        messages.put(name, message);
        return this;
    }
    
    //Adds all messages in other
    public ControllerMessage addAll(Map<String, String> other)
    {
        messages.putAll(other);
        return this;
    }
    
    //Returns the message by name
    //Null if message not found
    public Object getMessage(String name)
    {
        return messages.get(name);
    }
    
    //Returns true if message found
    public boolean messageExists(String name)
    {
        return messages.containsKey(name);
    }
    
    //Returns the message in iterator
    public Iterator<Object> getAllMessages()
    {
        return messages.values().iterator();
    }
    
    //Returns the message keys in iterator
    public Iterator<String> getAllMessageKeys()
    {
        return messages.keySet().iterator();
    }
    
    //Returns the message entries in iterator
    public Iterator<Entry<String, Object>> getAllMessageEntries()
    {
        return messages.entrySet().iterator();
    }
    
    //Returns a json parsed string of the messages
    public String makeJson()
    {
        return JsonParser.parsePretty(messages);
    }
    
    //Returns true if there are messages
    public boolean hasMessages()
    {
        return !messages.isEmpty();
    }
    
    //Returns a json parsed string of the messages
    @Override
    public String toString()
    {
        return makeJson();
    }
    
}
