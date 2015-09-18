//====================================
//  KYLE RUSSELL
//  13831056
//  PDC Project
//====================================

package engine.core.database;

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
    
    public Column(String columnName, String dbColumnType)
    {
        this.columnName =   columnName;
        isLiteral       =   isKnownLiteral(dbColumnType);
        columnValue     =   null;
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
    
    public boolean isKnownLiteral(String dbColumnType)
    {
        return dbColumnType.equalsIgnoreCase("java.lang.String") ||
               dbColumnType.equalsIgnoreCase("java.sql.Date");

    }
    
    public String getColumnName()
    {
        return columnName;
    }
    
    public Object getColumnValue()
    {
        return (isLiteral)? Model.makeLiteral(columnValue.toString()) : columnValue;
    }
    
    public Object getNonLiteralValue()
    {
        return columnValue;
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
    
    @Override
    public boolean equals(Object other)
    {
        if(other instanceof Column)
        {
            Column otherColumn  =   (Column) other;
            return this.columnName.equalsIgnoreCase(otherColumn.getColumnName());
        }
        
        else return false;
    }
   
}
