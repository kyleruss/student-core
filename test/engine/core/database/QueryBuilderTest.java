//####################################
//  KYLE RUSSELL
//  13831056
//  PDC Project
//####################################
/*
package engine.core.database;

import com.google.gson.JsonArray;
import engine.Models.Model;
import engine.Models.TestModel;
import engine.parsers.JsonParser;
import engine.core.DataConnector;
import java.sql.ResultSet;
import java.sql.SQLException;
import static org.junit.Assert.*;

import org.junit.Test;
import utilities.TestUtilities;

public class QueryBuilderTest 
{
    
  
    //Default testing model
    //Features typical columns types and limited rows
    private final Model testModel;
    
    public QueryBuilderTest()
    {
        testModel   =   new TestModel();
    }

    
    //Tests QueryBuilder's where function
    //Tests a simple where clause with literal
    @Test
    public void testWhere() 
    {
        TestUtilities.formatHeader("TEST WHERE");
        try
        {
            JsonArray results       =   testModel.builder().where("name", "=", "test", true).execute();
            assertNotNull(results);
            assertTrue(results.size() > 0);
        }
        
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
            fail(e.getMessage());
        }
    }

    //Tests QueryBuilder get functions
    //Test should return expected output
    //to built query 
    @Test
    public void testGet()
    {
        try
        {
            TestUtilities.formatHeader("TEST GET");
            JsonArray results   =   testModel.builder().get();
            assertNotNull(results);
            assertTrue(results.size() > 0);
            
            System.out.println(JsonParser.parsePretty(results));
        }
        
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
            fail(e.getMessage());
        }
    }
    
    //Tests QueryBuilder select functions
    //Tests default selects and filtering
    @Test
    public void testSelect()
    {
       try
       {
           TestUtilities.formatHeader("TEST SELECT SINGLE");
           JsonArray resultsAll         =   testModel.builder().get();
           JsonArray resultsLimited     =   testModel.builder().select("name").get();
           assertNotNull(resultsAll);
           assertNotNull(resultsLimited);
           
           TestUtilities.formatSubHeader("ALL", JsonParser.parsePretty(resultsAll));
           TestUtilities.formatSubHeader("LIMITED", JsonParser.parsePretty(resultsLimited));
       }
       
       catch(SQLException e)
       {
           System.out.println(e.getMessage());
           fail(e.getMessage());
       }
    }

    
    //Tests QueryBuilder's select by array
    //That accepts String[] and iteratively adds selects
    @Test
    public void testSelectByArray()
    {
        try
        {
            TestUtilities.formatHeader("TEST SELECT ARRAY");
            String[] columns    =   { "name", "age" };
            JsonArray results   =   testModel.builder().select(columns).get();
            
            //Check for non-empty table before test
            //Otherwise check output
            assertNotNull(results);
            assertTrue(results.size() > 0);
            
            System.out.println(JsonParser.parsePretty(results));
        }
        
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
            fail(e.getMessage());
        }
    }
    
  
    //Tests an inner join of 2 tables
    //Test tables are users and testtable
   @Test
    public void testJoin()
    {
        final String JOINTABLE    =   "users";
        try
        {
            TestUtilities.formatHeader("TEST JOINS");
            JsonArray results = testModel.builder().join(testModel.getTableName(), JOINTABLE, Join.JoinType.INNERR_JOIN).get();
            
           System.out.println(JsonParser.parsePretty(results));
        }
        
       catch(SQLException e)
       {
            System.out.println(e.getMessage());
       }
    }  
    

    //Tests SQL offsetting
    //Test checks numeric ID's in test table are after offset
    @Test
    public void testOffset()
    {
        final int OFFSET    =   2;
        try
        {
            TestUtilities.formatHeader("TEST OFFSET");
            JsonArray results   =   testModel.builder().offset(OFFSET).get();
            
            //numeric ID's should be follow after the offset 
            int id              =   results.get(1).getAsJsonObject().get("ID").getAsInt();
            assertTrue(id > OFFSET);
            
            System.out.println(JsonParser.parsePretty(results));
        }
        
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
            fail(e.getMessage());
        }
    }
    
    
    //Tests QueryBuilder SQL orderby function
    //Pass test if results are in order
    @Test
    public void testOrderBy()
    {
        try
        {
            TestUtilities.formatHeader("TEST ORDER BY");
            JsonArray results   =   testModel.builder().orderBy("name").get();
            
            //Check that results are in order
            //Test fails if results are not in ascending order
            for(int resultIndex = 2; resultIndex < results.size(); resultIndex++)
            {
                String namePrev    =   results.get(resultIndex - 1).getAsJsonObject().get("NAME").getAsString();
                String nameNext     =   results.get(resultIndex).getAsJsonObject().get("NAME").getAsString();
                
                assertTrue(namePrev.compareToIgnoreCase(nameNext) < 0);
            }
        }
        
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
            fail(e.getMessage());
        }
    }

    
    @Test
    public void testGroupBy() 
    {
       try
       {
           TestUtilities.formatHeader("TEST GROUP BY");
           JsonArray genderGroup    =   testModel.builder().select(new String[] {"name", "gender"}).groupBy("gender").get();
           JsonArray ageGroup       =   testModel.builder().select(new String[] {"name", "age"}).groupBy("age").get();
           
           assertNotNull(genderGroup);
           assertNotNull(ageGroup);
           
           TestUtilities.formatSubHeader("GENDER GROUP", JsonParser.parsePretty(genderGroup));
           TestUtilities.formatSubHeader("AGE GROUP", JsonParser.parsePretty(ageGroup));
       }
       
       catch(SQLException e)
       {
           System.out.println(e.getMessage());
           fail(e.getMessage());
       }
    }

    
    @Test
    public void testFirst()
    {
        try
        {
            TestUtilities.formatHeader("TEST FETCH FIRST");
            final int FIRST_ID  =   1;
            JsonArray results   =   testModel.builder().first().get();
            
            assertNotNull(results);
            assertTrue(results.size() <= 2);
            System.out.println(JsonParser.parsePretty(results));
            
            int id  =   results.get(1).getAsJsonObject().get("ID").getAsInt();
            assertEquals(id, FIRST_ID);
        }
        
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
            fail(e.getMessage());
        }
    }

    
    @Test
    public void testLimit() 
    {
        try
        {
            TestUtilities.formatHeader("TEST RESULT LIMIT");
            final int LIMIT             =   2;
            JsonArray limitedResults    =   testModel.builder().limit(LIMIT).get();
            
            assertNotNull(limitedResults);
            assertTrue(limitedResults.size() <= LIMIT + 1);
            System.out.println(JsonParser.parsePretty(limitedResults));
        }
        
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
            fail(e.getMessage());
        }
    }

    
    
    @Test
    public void testBuild()
    {
        TestUtilities.formatHeader("TEST RAW QUERY STRINGS");
        String queryWithSelect   =   testModel.builder().select("name").build();
        String queryWithCond     =   testModel.builder().where("name", "=", "kyle", true).build();
        String queryWithOrder    =   testModel.builder().orderBy("name").build();
        String queryWithLimit    =   testModel.builder().limit(4).build();
        String queryWithGroup    =   testModel.builder().select("name").groupBy("name").build();
        String queryWithJoin     =   testModel.builder().join("testtable", "users", Join.JoinType.INNERR_JOIN).build();
        
        TestUtilities.formatSubHeader("SELECT QUERY", queryWithSelect);
        TestUtilities.formatSubHeader("CONDITION QUERY", queryWithCond);
        TestUtilities.formatSubHeader("ORDER QUERY", queryWithOrder);
        TestUtilities.formatSubHeader("LIMIT QUERY", queryWithLimit);
        TestUtilities.formatSubHeader("GROUP QUERY", queryWithGroup);
        TestUtilities.formatSubHeader("JOIN QUERY", queryWithJoin);
    }


    
    @Test
    public void testExecute()
    {
        TestUtilities.formatHeader("TEST QUERY EXECUTE");
        try
        {
            JsonArray results   =   testModel.builder().execute();
            assertNotNull(results);
            assertTrue(results.size() > 0);
        }
        
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
            fail(e.getMessage());
        }
    }  
    
}
*/