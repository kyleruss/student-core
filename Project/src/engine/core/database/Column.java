//====================================
//	Kyle Russell
//	jdamvc
//	Column
//====================================

package engine.core.database;

import engine.models.Model;

//---------------------------------------
//                COLUMN
//---------------------------------------
//- Represents a column type in a table
//- Holds the columns name, value (temp), and literal type
//- Provides an extra layer of info for models 


public class Column
{
    private String columnName; //Column name in table
    private Object columnValue; //The columns value (leave null for just column definitions)
    private boolean isLiteral; //Column is literal if the column type requires literal encapsulation e.g varchar
    
    //Creates a column with name and value
    //isLiteral is set if passed value is a string
    public Column(String columnName, Object columnValue)
    {
        this.columnName     =   columnName;
        this.columnValue    =   columnValue;
        this.isLiteral      =   (columnValue instanceof String);
    }
    
    //Creates a column definition
    //Column type can be fetched from meta data in ResultSet
    //value is set null and can be added later
    public Column(String columnName, String dbColumnType)
    {
        this.columnName =   columnName;
        isLiteral       =   isKnownLiteral(dbColumnType);
        columnValue     =   null;
    }
    
    //Create a column with value of type dbColumnType
    //value is cast to dbColumnType
    public Column(String columnName, Object columnValue, String dbColumnType)
    {
        this.columnName     =   columnName;
        initValue(dbColumnType, columnValue);   
    }

    //Sets isLiteral based on value
    //Casts value to the dbColumnType
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
    
    //Returns true if the column type is a known literal
    //These typically include dates and strings
    public boolean isKnownLiteral(String dbColumnType)
    {
        return dbColumnType.equalsIgnoreCase("java.lang.String") ||
               dbColumnType.equalsIgnoreCase("java.sql.Date");
    }
    
    //Returns the columns name in the table
    public String getColumnName()
    {
        return columnName;
    }
    
    //Returns the columns value
    //Value may be null if Column is being used as a column definition
    //If value is a literal then value returned is encapsulated in ''
    public Object getColumnValue()
    {
        if(columnValue == null) return null;
        else return (isLiteral)? Model.makeLiteral(columnValue.toString()) : columnValue;
    }
    
    //Returns the non-single-quote encapsulated string of the value
    //getColumnValue() automatically encloses the value if it is literal
    //Use this to get the non-literal value
    public Object getNonLiteralValue()
    {
        return columnValue;
    }
    
    //Returns true if the value/column is a literal
    public boolean isLiteral()
    {
        return isLiteral;
    }
    
    //Set the column and value to literal
    public void setLiteral(boolean isLiteral)
    {
        this.isLiteral  =   isLiteral;
    }
    
    //Sets the columns name
    public void setColumnName(String columnName)
    {
        this.columnName =   columnName;
    }
    
    //Sets the columns value
    //Sets literal based on value type
    public void setColumnValue(Object columnValue)
    {
        this.columnValue    =   columnValue;
        if(columnValue instanceof String)
            isLiteral = true;
    }
    
    //Returns true if the columns have the same name 
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
