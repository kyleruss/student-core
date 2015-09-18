//====================================
//  KYLE RUSSELL
//  13831056
//  PDC Project
//====================================

package engine.views;


public interface ViewExplorer 
{
    public View getPrevView();
    
    public View getNextView();
    
    public void setPrevView(View prevView);
    
    public void setNextView(View nextView);
}
