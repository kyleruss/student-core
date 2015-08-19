
package engine.core;

import engine.models.Model;

public class Column
{
    private String columnName;
    private Object columnValue;
    private boolean isLiteral;
    
    public Column(String columnName, Object columnValue)
    {
        this.columnName     =   columnName;
        this.columnValue    =   columnValue;
        this.isLiteral      =   (columnValue instanceof String);
    }
    
    public Column(String columnName, Object columnValue, String dbColumnType)
    {
        this.columnName     =   columnName;
        initValue(dbColumnType, columnValue);   
    }

    private void initValue(String dbColumnType, Object value)
    {
        try 
        {
            columnValue =   Class.forName(dbColumnType).cast(value);    
            isLiteral   =   (columnValue instanceof String);
        } 
        
        catch (ClassNotFoundException ex)
        {
            columnValue =   null;
            isLiteral   =   false;
        }
    }
    
    
    
    public String getColumnName()
    {
        return columnName;
    }
    
    public Object getColumnValue()
    {
        return (isLiteral)? Model.makeLiteral(columnValue.toString()) : columnValue;
    }
    
    public boolean isLiteral()
    {
        return isLiteral;
    }
    
    public void setLiteral(boolean isLiteral)
    {
        this.isLiteral  =   isLiteral;
    }
    
    public void setColumnName(String columnName)
    {
        this.columnName =   columnName;
    }
    
    public void setColumnValue(String columnValue)
    {
        this.columnValue    =   columnValue;
    }
   
}
