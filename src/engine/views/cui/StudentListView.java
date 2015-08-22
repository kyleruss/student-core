
package engine.views.cui;

import com.bethecoder.ascii_table.ASCIITable;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import engine.controllers.ControllerMessage;
import engine.core.RouteHandler;
import engine.views.AbstractView;
import engine.views.cui.Utilities.CUITextTools;


public class StudentListView extends AbstractView
{
    private int currentPage;
    private static final int NUM_RESULTS = 1;
    
    public StudentListView()
    {
        this(new ControllerMessage());
    }
    
    public StudentListView(ControllerMessage messages)
    {
        super
        (
                messages, 
                "Students", 
                "Find, remove, modify students in the school", 
                "/" + "students"//Agent.getActiveSession().getUser().get("USERNAME").getNonLiteralValue() + "/students/"
        );
        
        currentPage =   1;
    }
    
    @Override
    public void display()
    {
        super.display();
        System.out.println("\n" + CUITextTools.changeColour(CUITextTools.underline("Student list - page 1/3"), CUITextTools.MAGENTA) + "\n");
        showList(1);
       
    }
    
    public void showList(int page)
    {
        ResponseDataView results = (ResponseDataView) RouteHandler.go("getStudentList", new Object[] { page, NUM_RESULTS }, new Class<?>[] { Integer.class, Integer.class }, null);
        if(results.getResponseStatus())
        {
            
            JsonArray response  =   results.getResponseData().getData();
            JsonArray columns   =   response.get(0).getAsJsonObject().get("columnNames").getAsJsonArray();
            
            String[][] data     =   new String[response.size() - 1][columns.size()];
            String[] headers    =   new String[columns.size()];
            for(int colIndex = 0; colIndex < headers.length; colIndex++)
                headers[colIndex] = columns.get(colIndex).getAsString();
            
            for(int rowIndex = 1; rowIndex <= data.length; rowIndex++)
            {
                JsonObject userRow  =   response.get(rowIndex).getAsJsonObject();
                
                for(int colIndex = 0; colIndex < headers.length; colIndex++)
                {
                    String colName  =   headers[colIndex];
                    String userCol  =   userRow.get(colName).getAsString();
                    data[rowIndex - 1][colIndex] = userCol;
                }
            }
            
            ASCIITable.getInstance().printTable(headers, data);
        }
        
        else System.out.println(results.getResponseMessage());
    }
    
    public void deleteStudent()
    {
        
    }
    
    public void modifyStudent()
    {
        
    }
    
    public void findStudent()
    {
        
    }
    
    public void nextPage()
    {
        
    }
    
    public void prevPage()
    {
        
    }

    @Override
    protected String getCommandsFile() 
    {
        return "/engine/config/listeners/StudentListListener.json";
    }
    
    public static void main(String[] args)
    {
      //  ResponseDataView results = (ResponseDataView) RouteHandler.go("getStudentList", new Object[] { 1 }, new Class<?>[] { Integer.class }, null);
        StudentListView view    =   new StudentListView();
        view.display();
       // view.showList(1);
    }
    
}
