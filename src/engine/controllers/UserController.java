//====================================
//  KYLE RUSSELL
//  13831056
//  PDC Project
//====================================

package engine.controllers;

import com.google.gson.JsonArray;
import engine.core.Agent;
import engine.core.DataConnector;
import engine.core.Path;
import engine.core.authentication.Auth;
import engine.core.database.Conditional;
import engine.core.database.Join;
import engine.core.security.Crypto;
import engine.models.ClassesModel;
import engine.models.EmergencyContactModel;
import engine.models.MedicalModel;
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
    
    public View getLogin()
    {
        if(!Agent.isGUIMode()) return prepareView(new LoginView());
        else return prepareView(new engine.views.gui.LoginView());
    }
    
    public View getHome()
    {
        if(!Agent.isGUIMode()) return prepareView(new HomeView());
        else return prepareView(new engine.views.gui.Home());
    }
    
    public View logout()
    {
        Agent.setActiveSession(null);
        return prepareView(new LoginView());
    }
    
    public View postLogin()
    {
        final String invalidInputMesage         =   "Invalid information, please check your fields";
        final String invalidAttemptMessage      =   "Invalid username or password";
        final String successMessage             =   "Successfully logged in";
        
        if(!validatePostData(new String[]{"loginUsername", "loginPassword"}))
            return prepareView(new ResponseDataView(invalidInputMesage, false));
        else
        {
            String loginUsername    =   (String) postData.getMessage("loginUsername");
            String loginPassword    =   (String) postData.getMessage("loginPassword");
            
            if(Auth.login(loginUsername, loginPassword) != null)
                return prepareView(new ResponseDataView(successMessage, true));
            else
                return prepareView(new ResponseDataView(invalidAttemptMessage, false));
        }
    }
    
    public View getMyClasses()
    {
        User user       =   new User("kyleruss");//Agent.getActiveSession().getUser();
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
            return prepareView(new MyClassesView(new ControllerMessage(results))); 
        }
        
        catch(SQLException e)
        {
         //   System.out.println("[SQL Exception] " + e.getMessage());
            return null;
        }
    }
    
    public View getClassPage(Integer classId)
    {
        try
        {
            JsonArray classDetails  =   new ClassesModel(classId).builder().where("id", "=", "1")
                    .select("id", "Class ID")
                    .select("name", "Class name")
                    .select("teacher_id", "Teacher ID")
                    .select("created_date", "Date created")
                    .get();
            return prepareView(new ClassPageView(new ControllerMessage(classDetails)));
        }
        
        catch(SQLException  e)
        {
         //   System.out.println(e.getMessage());
            return null;
        }
        
    }
    
    public View getClassAssessments(Integer classId)
    {
        AbstractView classPage      =   (AbstractView) getClassPage(classId);
        JsonArray details           =   classPage.getMessages().getData();
        return prepareView(new ClassAssessmentsView(new ControllerMessage(details)));
    }
    
   public View getMyDepartment()
    {
        User user            =   new User("kyleruss"); //Agent.getActiveSession().getUser();
        String username      =   user.get("username").getColumnValue().toString();
        
        try
        {
            JsonArray results   =   user.builder()
                    .join(new Join("users", "staff", "username", "user_id", Join.JoinType.INNERR_JOIN).filter(new Conditional("username", "=", username)))
                    .join("staff", "department", "dept_id", "id", Join.JoinType.INNERR_JOIN)
                    .select("department.*")
                    .get();
            
            return prepareView(new DepartmentView(new ControllerMessage(results)));
        }
        
        catch(SQLException e)
        {
            return null;
        }
    } 
    
    public View getRegister()
    {
        return new RegisterView();
    }
    
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
             //       e.printStackTrace();
              //      System.out.println("[SQL Exception] " + e.getMessage());
                    conn.rollbackTransaction();
                    conn.closeConnection();
                    return prepareView(new ResponseDataView(failedMessage, false));
                }
            }
        }
    }
}
