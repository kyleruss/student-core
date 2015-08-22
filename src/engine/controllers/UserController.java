
package engine.controllers;

import com.google.gson.JsonArray;
import engine.core.Agent;
import engine.core.DataConnector;
import engine.core.authentication.Auth;
import engine.core.database.Conditional;
import engine.core.database.Join;
import engine.core.security.Crypto;
import engine.models.ClassEnrolmentsModel;
import engine.models.DepartmentModel;
import engine.models.EmergencyContactModel;
import engine.models.MedicalModel;
import engine.models.StaffModel;
import engine.models.User;
import engine.parsers.JsonParser;
import engine.views.View;
import engine.views.cui.DepartmentView;
import engine.views.cui.LoginView;
import engine.views.cui.MyClassesView;
import engine.views.cui.RegisterView;
import engine.views.cui.ResponseDataView;
import java.sql.ResultSet;
import java.sql.SQLException;


public class UserController extends Controller
{   
    public UserController()
    {
        super();
    }
    
    public UserController(ControllerMessage postData)
    {
        super(postData);
    }
    
    public UserController(ControllerMessage postData, RequestType requestType)
    {
        super(postData, requestType);
    }
    
    public View getLogin()
    {
        return new LoginView();
    }
    
    public View logout()
    {
        Agent.setActiveSession(null);
        return new LoginView();
    }
    
    public View postLogin()
    {
        final String invalidInputMesage         =   "Invalid information, please check your fields";
        final String invalidAttemptMessage      =   "Invalid username or password";
        final String successMessage             =   "Successfully logged in";
        
        if(!validatePostData(new String[]{"loginUsername", "loginPassword"}))
            return new ResponseDataView(invalidInputMesage, false);
        else
        {
            String loginUsername    =   postData.getMessage("loginUsername");
            String loginPassword    =   postData.getMessage("loginPassword");
            
            if(Auth.login(loginUsername, loginPassword) != null)
                return new ResponseDataView(successMessage, true);
            else
                return new ResponseDataView(invalidAttemptMessage, false);
        }
            
    }
    
    public View getMyClasses()
    {
        User user       =   Agent.getActiveSession().getUser();
        String username =   user.get("username").getColumnValue().toString();
        try
        {
            JsonArray classList =   new ClassEnrolmentsModel().builder().where("user_id", "=", username).get();
            return new MyClassesView(new ControllerMessage(classList)); 
        }
        
        catch(SQLException e)
        {
            System.out.println("[SQL Exception] " + e.getMessage());
            return null;
        }
    }
    
   /* public View getMyDepartment()
    {
        User user            =   Agent.getActiveSession().getUser();
        String username      =   user.get("username").getColumnValue().toString();
        int deptId           =   (int) new StaffModel(username).get("dept_id").getColumnValue();
        
        try
        {
            JsonArray dept  =   new DepartmentModel().builder().where("id", username, username)
        }
        
        catch(SQLException e)
        {
            
        }
        return new DepartmentView();
    } */
    
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
            return new ResponseDataView(invalidInputMesage, false);
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
            int gender      =   postData.getMessage("registerGender").equalsIgnoreCase("male")? 1 : 0;
            String passSalt =   Crypto.salt(postData.getMessage("registerUsername"));
            String passHash =   Crypto.makeHash(passSalt, postData.getMessage("registerPassword"));
                    
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
                        ResultSet rs1    =   conn.getResults();
                        if(rs1.next()) System.out.println("next!");
                        long emergContactId =   rs1.getLong(1);
                        

                        //Create medical
                        medical.setActiveConnection(conn);
                        medical.set("contact_id", emergContactId);
                        medical.save();
                        ResultSet rs2 =   conn.getResults();
                        if(rs2.next()) System.out.println("next!");
                        long medicalId  =   rs2.getLong(1);
                        
                        //Create user and commit
                        System.out.println("starting user create");
                        user.setActiveConnection(conn);
                        user.set("medical_id", medicalId);
                        
                        if(user.save())
                        {
                            conn.commitTransaction();
                            return new ResponseDataView(successMessage, true);
                        }

                        else
                        {
                            conn.rollbackTransaction();
                            return new ResponseDataView(failedMessage, false);
                        }
                    }
                    
                    catch(SQLException e)
                    {
                        e.printStackTrace();
                        System.out.println("[SQL Exception] " + e.getMessage());
                        conn.rollbackTransaction();
                        conn.closeConnection();
                        return new ResponseDataView(failedMessage, false);
                    }
                }
        }
    }
    
    public static void main(String[] args)
    {
        try
        {
            JsonArray results   =   new User().builder()
                    .join(new Join("users", "staff", "username", "user_id", Join.JoinType.INNERR_JOIN).filter(new Conditional("username", "=", "kyleruss").literal()))
                    .select("staff.*").get();
                  //  .join("staff", "department", "dept_id", "id", Join.JoinType.INNERR_JOIN).select("department.*")
                 //   .where("username", "=", "'kyleruss'").get();
            System.out.println(JsonParser.parsePretty(results));
        }
        
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }
}
