//====================================
//	Kyle Russell
//	jdamvc
//	ViewExplorer
//====================================

package engine.views;

//------------------------------------
//            VIEWEXPLORER
//------------------------------------
//- Interface provides views with traversal capabilities
//- Gives views a doubly-linked structure and allows
//forward and back traversal 

public interface ViewExplorer 
{
    //Returns the previous view
    //Should return null and inform error if no previous view 
    //Can go back if the current view is atleast 2 views deep
    public View getPrevView();
    
    //Returns the next view
    //Should return null and inform if no next view (at current view)
    //Can go forward if there is atleast one view infront
    public View getNextView();
    
    //Set the previous view
    //Should resolve paradox scenario
    public void setPrevView(View prevView);
    
    //Set the next view
    //Should resolve paradox scenario
    public void setNextView(View nextView);
}
