//====================================
//  KYLE RUSSELL
//  13831056
//  PDC Project
//====================================

package engine.controllers;

import com.google.gson.JsonArray;
import engine.parsers.JsonParser;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;


public class ControllerMessage 
{
    Map<String, Object> messages;
    JsonArray data;
    
    public ControllerMessage()
    {
        this(new JsonArray());
    }
    
    public ControllerMessage(JsonArray data)
    {
        messages    =   new LinkedHashMap<>();
        this.data   =   data;
    }
    
    public int numMessages()
    {
        return messages.size();
    }
    
    public JsonArray getData()
    {
        return data;
    }
    
    public ControllerMessage add(String name, Object message)
    {
        messages.put(name, message);
        return this;
    }
    
    public ControllerMessage addAll(Map<String, String> other)
    {
        messages.putAll(other);
        return this;
    }
    
    public Object getMessage(String name)
    {
        return messages.get(name);
    }
    
    public boolean messageExists(String name)
    {
        return messages.containsKey(name);
    }
    
    public Iterator<Object> getAllMessages()
    {
        return messages.values().iterator();
    }
    
    public Iterator<String> getAllMessageKeys()
    {
        return messages.keySet().iterator();
    }
    
    public Iterator<Entry<String, Object>> getAllMessageEntries()
    {
        return messages.entrySet().iterator();
    }
    
    public String makeJson()
    {
        return JsonParser.parsePretty(messages);
    }
    
    public boolean hasMessages()
    {
        return !messages.isEmpty();
    }
    
}
