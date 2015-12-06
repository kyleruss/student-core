//====================================
//	Kyle Russell
//	jdamvc
//	UserController
//====================================

package engine.controllers;

import com.google.gson.JsonArray;
import engine.config.AppConfig;
import engine.core.Agent;
import engine.core.DataConnector;
import engine.core.ExceptionOutput;
import engine.core.Path;
import engine.core.RouteHandler;
import engine.core.authentication.Auth;
import engine.core.database.Conditional;
import engine.core.database.Join;
import engine.core.authentication.Crypto;
import engine.models.ClassesModel;
import engine.models.EmergencyContactModel;
import engine.models.MedicalModel;
import engine.models.NotificationModel;
import engine.models.User;
import engine.views.AbstractView;
import engine.views.View;
import engine.views.cui.ClassAssessmentsView;
import engine.views.cui.ClassPageView;
import engine.views.cui.DepartmentView;
import engine.views.cui.LoginView;
import engine.views.cui.MyClassesView;
import engine.views.cui.RegisterView;
import engine.views.ResponseDataView;
import engine.views.cui.HomeView;
import engine.views.cui.NotificationView;
import java.sql.ResultSet;
import java.sql.SQLException;


public class UserController extends Controller
{   
    public UserController(Path path)
    {
        super(path);
    }
    
    public UserController(ControllerMessage postData, Path path)
    {
        super(postData, path);
    }
    
    public UserController(ControllerMessage postData, RequestType requestType, Path path)
    {
        super(postData, requestType, path);
    }
    
    //Returns the login view
    public View getLogin()
    {
        if(!Agent.isGUIMode()) return prepareView(new LoginView());
        else return prepareView(new engine.views.gui.LoginView());
    }
    
    //Returns the home view
    //Requires authenticated user
    public View getHome()
    {
        if(!Agent.isGUIMode()) return prepareView(new HomeView());
        else return prepareView(new engine.views.gui.Home());
    }
    
    //Returns the users notifications
    //CUI only (GUI handles seperately)
    public View getNotifications()
    {
        return prepareView(new NotificationView());
    }
    
    //Logs the user out
    public View logout()
    {
        Agent.setActiveSession(null);
        return getLogin();
    }
    
    //Attempts to log the user in
    public View postLogin()
    {
        final String invalidInputMesage         =   "Invalid information, please check your fields";
        final String invalidAttemptMessage      =   "Invalid username or password";
        final String successMessage             =   "Successfully logged in";
        
        if(!validatePostData(new String[]{"loginUsername", "loginPassword", "storeCredentials"}))
            return prepareView(new ResponseDataView(invalidInputMesage, false));
        else
        {
            String loginUsername        =   (String) postData.getMessage("loginUsername");
            String loginPassword        =   (String) postData.getMessage("loginPassword");
            boolean storeCredentials    =   (boolean) postData.getMessage("storeCredentials");
            
            if(Auth.login(loginUsername, loginPassword, storeCredentials) != null)
                return prepareView(new ResponseDataView(successMessage, true));
            else
                return prepareView(new ResponseDataView(invalidAttemptMessage, false));
        }
    }
    
    //Returns the authenticated users enrolled classes
    public View getMyClasses()
    {
        User user       =   Agent.getActiveSession().getUser();
        String username =   user.get("username").getColumnValue().toString();
        try
        {
            JsonArray results   =   user.builder().join(new Join("users", "class_enrolments", "username", "user_id", Join.JoinType.INNERR_JOIN)
            .filter(new Conditional("username", "=", username)))
            .join("class_enrolments", "classes", "class_id", "id", Join.JoinType.INNERR_JOIN)
            .select("classes.id", "Class ID")
            .select("classes.name", "Class name")
            .select("classes.description", "Class description")
            .select("class_enrolments.semester_num", "Semester")   
            .get();
            
            if(!Agent.isGUIMode()) return prepareView(new MyClassesView(new ControllerMessage(results))); 
            else return null;
        }
        
        catch(SQLException e)
        {
            return null;
        }
    }
    
    //Returns the class page of the class
    public View getClassPage(Integer classId)
    {
        try
        {
            JsonArray classDetails  =   new ClassesModel().builder().where("id", "=", "" + classId)
                    .select("id", "Class ID")
                    .select("name", "Class name")
                    .select("teacher_id", "Teacher ID")
                    .select("created_date", "Date created")
                    .get();
            
            
            if(!Agent.isGUIMode()) return prepareView(new ClassPageView(new ControllerMessage(classDetails)));
            else return prepareView(new engine.views.gui.ClassPageView(new ControllerMessage(classDetails)));
        }
        
        catch(SQLException  e)
        {
            return null;
        }
    }
    
    //Returns the assessments of the class
    public View getClassAssessments(Integer classId)
    {
        AbstractView classPage      =   (AbstractView) getClassPage(classId);
        JsonArray details           =   classPage.getMessages().getData();
        return prepareView(new ClassAssessmentsView(new ControllerMessage(details)));
    }
    
    //Returns the department of the authenticated user
    //User is denied if they do not belong to a department
   public View getMyDepartment()
   {
        User user            =    Agent.getActiveSession().getUser();
        String username      =   user.get("username").getColumnValue().toString();

        //User doesn't belong to a department
        if(user.get("dept_id") == null || user.get("dept_id").getNonLiteralValue() == null)
        {
            if(AppConfig.GUI_MODE)
                return RouteHandler.go("getErrorPage", new Object[] { "You are not in a department" }, new Class<?>[] { String.class }, null);
            else
            {
                ExceptionOutput.output("You are not in a department", ExceptionOutput.OutputType.MESSAGE);
                return null;
            }
        }
        
        try
        {
           JsonArray results   =   new User().builder()
                    .join(new Join("users", "department", "dept_id", "id", Join.JoinType.INNERR_JOIN)
                           .filter(new Conditional("username", "=", username)))
                    .select("department.*")
                    .get(); 
            
            if(!Agent.isGUIMode()) return prepareView(new DepartmentView(new ControllerMessage(results)));
            else return prepareView(new engine.views.gui.DepartmentView(new ControllerMessage(results)));
        }
        
        catch(SQLException e)
        {
            return null;
        }
    } 
    
   //Returns the register view
    public View getRegister()
    {
        if(!Agent.isGUIMode()) return prepareView(new RegisterView());
        else return prepareView(new engine.views.gui.RegisterView());
    }
    
    //Attempts to register a user
    //User will be redirected to the login view if successful
    public View postRegister()
    {
        final String invalidInputMesage         =   "Invalid information, please check your fields";
        final String failedMessage              =   "Failed to complete registration, please try again";
        final String successMessage             =   "Account has been successfully created, you can now login";
        
        String[] expectedInput  =   
        { 
            "registerUsername", "registerPassword", "registerFirstname", "registerLastname",
            "registerGender", "registerBirth", "registerPhone", "registerEmail", "registerEthnicity",
            "registerContactFirstname", "registerContactLastname", "registerContactPhone",
            "registerContactEmail", "registerMedicalDescription"
        };
        
        if(!validatePostData(expectedInput))
            return prepareView(new ResponseDataView(invalidInputMesage, false));
        else
        {
            EmergencyContactModel emergencyContact  =   new EmergencyContactModel();
            emergencyContact.set("firstname", postData.getMessage("registerContactFirstname"));
            emergencyContact.set("lastname", postData.getMessage("registerContactLastname"));
            emergencyContact.set("contact_ph", postData.getMessage("registerContactPhone"));
            emergencyContact.set("contact_email", postData.getMessage("registerContactEmail"));
            emergencyContact.set("relationship", "Doctor");
            
            MedicalModel medical    =  new MedicalModel();
            medical.set("description", postData.getMessage("registerMedicalDescription"));
            
            User user       =   new User();
            int gender      =   ((String) postData.getMessage("registerGender")).equalsIgnoreCase("male")? 1 : 0;
            String passSalt =   Crypto.salt((String) postData.getMessage("registerUsername"));
            String passHash =   Crypto.makeHash(passSalt, (String) postData.getMessage("registerPassword"));
                    
            user.set("username", postData.getMessage("registerUsername"));
            user.set("password", passHash);
            user.set("firstname", postData.getMessage("registerFirstname"));
            user.set("lastname", postData.getMessage("registerLastname"));
            user.set("gender", gender);
            user.set("birthdate", postData.getMessage("registerBirth"));
            user.set("contact_ph", postData.getMessage("registerPhone"));
            user.set("contact_email", postData.getMessage("registerEmail"));
            user.set("ethnicity", postData.getMessage("registerEthnicity"));
            user.set("role_id", 1);
            
            try(DataConnector conn  =   new DataConnector())
            {
                try
                {
                    conn.startTransaction();
                    conn.setQueryMutator();

                    //Create contact
                    emergencyContact.setActiveConnection(conn);
                    emergencyContact.save();
                    
                    ResultSet rs1       =   conn.getResults();
                    if(!rs1.next()) throw new SQLException("Failed to create emergency contact");
                    long emergContactId =   rs1.getLong(1);


                    //Create medical
                    medical.setActiveConnection(conn);
                    medical.set("contact_id", emergContactId);
                    medical.save();
                    
                    ResultSet rs2 =   conn.getResults();
                     if(!rs2.next()) throw new SQLException("Failed to create medical record");
                    long medicalId  =   rs2.getLong(1);

                    //Create user and commit
                    user.setActiveConnection(conn);
                    user.set("medical_id", medicalId);

                    if(user.save())
                    {
                        conn.commitTransaction();
                        NotificationModel.sendNotification(postData.getMessage("registerUsername").toString(), 
                                                            "Welcome to StudentCore! Thank you for registering");
                        return prepareView(new ResponseDataView(successMessage, true));
                    }

                    else
                    {
                        conn.rollbackTransaction();
                        return prepareView(new ResponseDataView(failedMessage, false));
                    }
                }

                catch(SQLException e)
                {
                    conn.rollbackTransaction();
                    conn.closeConnection();
                    return prepareView(new ResponseDataView(failedMessage, false));
                }
            }
        }
    }
}
