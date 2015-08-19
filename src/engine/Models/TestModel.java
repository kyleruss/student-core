//####################################
//  KYLE RUSSELL
//  13831056
//  PDC Project
//####################################

package engine.models;


import engine.core.database.QueryBuilder;

//---------------------------------
//          TEST MODEL
//--------------------------------
//- Dummy model used in unit testing
//- Basic example of bare model
//- Not used in core - only tests

public class TestModel extends Model
{
    
    //Init a TestModel with no existing rows
    //Data will be empty
    public TestModel()
    {
        super();
    }
    
    //Init a TestModel by primary key
    //Finds a TestModel with PK of id
    public TestModel(String id)
    {
        super(id);
    }
    
    //Initializes the models config with
    //table and primary key properties
    @Override
    protected void initTable()
    {
        table       =   "app.testtable";
        primaryKey  =   "id";
    }
    
    public static QueryBuilder model()
    {
        return new TestModel().builder();
    }
}
