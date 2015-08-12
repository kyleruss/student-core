/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package engine.core.database;

import com.google.gson.JsonArray;
import engine.Models.Model;
import engine.Models.User;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author kyle
 */
public class QueryBuilderTest 
{
    private final Model testModel;
    
    public QueryBuilderTest()
    {
        testModel   =   new User();
    }
  
    @Test
    public void testWhere() 
    {
        try
        {
            ResultSet results       =   testModel.builder().where("name", "=", "kyle").execute();
            assertNotNull(results);
            assertTrue(results.next());
        }
        
        catch(SQLException e)
        {
            fail(e.getMessage());
        }
    }

    public  void testGet()
    {
        try
        {
            JsonArray results   =   testModel.builder().get();
            assertNotNull(results);
            assertTrue(results.size() > 0);
            
            System.out.println("[TEST GET] " + results);
        }
        
        catch(SQLException e)
        {
            System.out.println("[SQL EXCEPTION] Failed to get - " + e.getMessage());
            fail(e.getMessage());
        }
    }
    
    
    @Test
    public void testSelect()
    {
       try
       {
           JsonArray resultsAll         =   testModel.builder().get();
           JsonArray resultsLimited     =   testModel.builder().select("name").get();
           assertNotNull(resultsAll);
           assertNotNull(resultsLimited);
           
           System.out.println("[TEST SELECT] Results all - " + resultsAll);
           System.out.println("[TEST SELECT] Results limited - " + resultsLimited);
       }
       
       catch(SQLException e)
       {
           System.out.println("[SQL EXCEPTION] Failed to select - " + e.getMessage());
           fail(e.getMessage());
       }
    }

    @Test
    public void testSelectByArray()
    {
        try
        {
            String[] columns    =   { "name", "description" };
            JsonArray results   =   testModel.builder().select(columns).get();
            assertNotNull(results);
            assertTrue(results.size() > 0);
            
            System.out.println(results);
        }
        
        catch(SQLException e)
        {
            System.out.println("[SQL EXCEPTION] Failed to select by array - " + e.getMessage());
            fail(e.getMessage());
        }
    }
    
    @Test
    public void testJoin()
    {
        
    }

    @Test
    public void testJoinWithIds()
    {
        
    }

    /**
     * Test of offset method, of class QueryBuilder.
     */
    @Test
    public void testOffset()
    {
        final int offset    =   1;
        try
        {
            JsonArray results   =   testModel.builder().offset(1).get();
            System.out.println(results.get(0));
        }
        
        catch(SQLException e)
        {
            System.out.println("[SQL EXCEPTION] Failed to set offset of: " + offset + "; Error: " + e.getMessage());
            fail(e.getMessage());
        }
    }

    @Test
    public void testOrderBy()
    {
        try
        {
            JsonArray results   =   testModel.builder().orderBy("name").get();
            
            for(int resultIndex = 1; resultIndex < results.size(); resultIndex++)
            {
                String namePrev    =   results.get(resultIndex - 1).getAsJsonObject().get("name").getAsString();
                String nameNext     =   results.get(resultIndex).getAsJsonObject().get("name").getAsString();
                
                assertTrue(namePrev.compareToIgnoreCase(nameNext) < 0);
            }
        }
        
        catch(SQLException e)
        {
            System.out.println("[SQL EXCEPTION] Failed to order results - " + e.getMessage());
            fail(e.getMessage());
        }
        
    }

    /**
     * Test of groupBy method, of class QueryBuilder.
     */
    @Test
    public void testGroupBy() 
    {
        
    }

    @Test
    public void testFirst()
    {
        try
        {
            final int FIRST_ID  =   1;
            JsonArray results   =   testModel.builder().first().get();
            assertNotNull(results);
            assertTrue(results.size() <= 1);
            
            int id  =   results.get(0).getAsJsonObject().get("ID").getAsInt();
            assertEquals(id, FIRST_ID);
        }
        
        catch(SQLException e)
        {
            System.out.println("[SQL EXCEPTION] Failed to fetch first - " + e.getMessage());
            fail(e.getMessage());
        }
    }

    /**
     * Test of limit method, of class QueryBuilder.
     */
    @Test
    public void testLimit() {
        System.out.println("limit");
        int rowLimit = 0;
        QueryBuilder instance = null;
        QueryBuilder expResult = null;
        QueryBuilder result = instance.limit(rowLimit);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of build method, of class QueryBuilder.
     */
    @Test
    public void testBuild()
    {
        String queryWithSelect   =   testModel.builder().select("name").build();
        String queryWithCond     =   testModel.builder().where("name", "=", "kyle").build();
        String queryWithOrder    =   testModel.builder().orderBy("name").build();
        String queryWithLimit    =   testModel.builder().limit(4).build();
        String queryWithGroup    =   testModel.builder().groupBy("name").build();
        
        System.out.println("## SELECT QUERY ##\n" + queryWithSelect);
        
    }

    /**
     * Test of execute method, of class QueryBuilder.
     */
    @Test
    public void testExecute() throws Exception {
        System.out.println("execute");
        QueryBuilder instance = null;
        ResultSet expResult = null;
        ResultSet result = instance.execute();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
