//====================================
//	Kyle Russell
//	StudentCore
//	TestView
//====================================

package engine.views.cui;


public class TestView
{
    public void methodWithoutParam()
    {
        System.out.println("-- Called method without param --");
    }
    
    public String methodWithParam(String param)
    {
        System.out.println("-- Called method with param --");
        return param;
    }
}
