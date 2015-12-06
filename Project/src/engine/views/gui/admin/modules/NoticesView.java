//====================================
//	Kyle Russell
//	StudentCore
//	NoticesView
//====================================

package engine.views.gui.admin.modules;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import engine.controllers.ControllerMessage;
import engine.core.Agent;
import engine.core.RouteHandler;
import engine.models.AdminAnnouncementsModel;
import engine.views.ResponseDataView;
import java.awt.event.ActionEvent;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.Timer;


public class NoticesView extends AnnouncementView
{
    protected String announcementCode  =   "ADMIN";
        @Override
        protected void showAddAnnouncement() 
        {
            modifyPanel.clear();
            modifyPanel.header.setText("Add announcement");
            modifyPanel.header.setIcon(new ImageIcon(addSmallImage));
            showAnnouncementView(AnnouncementView.MODIFY_VIEW);
            context = AnnouncementView.ManageContext.ADDING;
        }

        @Override
        protected void showRemoveAnnouncement() 
        {
            if(announcementList.getSelectedIndex() == -1)
                JOptionPane.showMessageDialog(null, "Please select an announcement in remove");
            else
            {
                int option      =   JOptionPane.showConfirmDialog(null, "Are you sure you want to remove this announcement?", "Remove announcement", JOptionPane.YES_NO_OPTION);
                if(option == JOptionPane.YES_OPTION)
                {
                    submitRemoveAnnouncement();
                }
            }
        }

        @Override
        protected void showEditAnnouncement() 
        {
            if(announcementList.getSelectedIndex() == -1)
            {
                JOptionPane.showMessageDialog(null, "Please select an announcement to edit");
                return;
            }
            
            modifyPanel.header.setText("Edit announcement");
            modifyPanel.header.setIcon(new ImageIcon(editSmallImage));
            JsonObject value = (JsonObject) announcementList.getSelectedValue();
            String title    =   value.get("TITLE").getAsString();
            String content  =   value.get("CONTENT").getAsString();
            modifyPanel.header.setText("Edit announcement");
            modifyPanel.header.setIcon(new ImageIcon(editSmallImage));
            modifyPanel.fill(title, content);
            showAnnouncementView(AnnouncementView.MODIFY_VIEW);
            context = AnnouncementView.ManageContext.EDITING;
        }

        @Override
        protected JsonArray getData()
        {
            return AdminAnnouncementsModel.getAllAnnouncements();
        }

        @Override
        protected void submitAddAnnouncement() 
        {
            ControllerMessage postData  =   prepareAddPost();
            if(postData == null) return;

            ResponseDataView response   =   (ResponseDataView) RouteHandler.go("postAddAdminAnnouncement", postData);
            Timer responseTimer =   new Timer(2000, (ActionEvent e)->
            {
                showStatusLabel(response.getRawResponseMessage(), response.getResponseStatus());
                back();
            });

            responseTimer.setRepeats(false);
            responseTimer.start();

            if(response.getResponseStatus())
                initData();
        }

        @Override
        protected void submitRemoveAnnouncement()  
        {
            ControllerMessage postData  =   prepareRemovePost();
            if(postData == null) return;
            
            ResponseDataView response   =   (ResponseDataView) RouteHandler.go("postRemoveAdminAnnouncement", postData);
            showStatusLabel(response.getRawResponseMessage(), response.getResponseStatus());
            
            if(response.getResponseStatus())
            {
                announcementModel.remove(announcementList.getSelectedIndex());
                if(announcementModel.size() > 0)
                    announcementList.setSelectedIndex(0);
            }
        }
        
        protected ControllerMessage prepareEditPost()
        {
            String title    =   modifyPanel.getTitle();
            String content  =   modifyPanel.getContent();
            int id          =   getSelectedValue().get("ID").getAsInt();

            if(title.equals("") || content.equals(""))
            {
                JOptionPane.showMessageDialog(null, "Please fill in all the fields");
                return null;
            }
            
            else
            {
                showProcessing();
                ControllerMessage postData  =   new ControllerMessage();
                postData.add("announceID", id);
                postData.add("announceContent", content);
                postData.add("announceTitle", title);
                postData.add("announceCode", announcementCode);
                
                return postData;
            }
        }
        
        protected ControllerMessage prepareAddPost()
        {
            String title    =   modifyPanel.getTitle();
            String content  =   modifyPanel.getContent();
            
            if(title.equals("") || content.equals(""))
            {
                JOptionPane.showMessageDialog(null, "Please fill in all the fields");
                return null;
            }
            
            else
            {
                String username =   Agent.getActiveSession().getUser().get("username").getNonLiteralValue().toString();
                showProcessing();
                ControllerMessage postData  =   new ControllerMessage();
                postData.add("announcePoster", username);
                postData.add("announceContent", content);
                postData.add("announceTitle", title);
                postData.add("announceCode", announcementCode);
                
                return postData;
            }
        }
        
        protected ControllerMessage prepareRemovePost()
        {
            JsonObject value            =   (JsonObject) announcementList.getSelectedValue();
            if(value == null)
            {
                JOptionPane.showMessageDialog(null, "Please select an announcement to delete");
                return null;
            }
            int announceID              =   value.get("ID").getAsInt();
            ControllerMessage postData  =   new ControllerMessage();
            postData.add("announceID", announceID);
            postData.add("announceCode", announcementCode);
            
            return postData;
        }

        @Override
        protected void submitEditAnnouncement() 
        {
            ControllerMessage postData  =   prepareEditPost();
            if(postData == null) return;

            ResponseDataView response   =   (ResponseDataView) RouteHandler.go("postEditAdminAnnouncement", postData);
            Timer responseTimer =   new Timer(2000, (ActionEvent e)->
            {
                showStatusLabel(response.getRawResponseMessage(), response.getResponseStatus());
                back();
            });

            responseTimer.setRepeats(false);
            responseTimer.start();

            if(response.getResponseStatus())
                initData();
        }
    }