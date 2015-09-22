//====================================
//  KYLE RUSSELL
//  13831056
//  PDC Project
//====================================

package engine.core;

//------------------------------------------
//                ROUTES
//------------------------------------------
//- Routes define paths in the application
//- Routes map to a controller that handles behaviour
//- Routes can be accessed by name and location
//- Paramaters can be passed where necessary
//- Filters can be used to restrict access to a route
//- Groups can be defined to cluster similar routes

public class Routes extends Router 
{
    public Routes()
    {
        super();
    }
    
    //Define the applications routes in here
    //Routes only need to be defined once
    @Override
    protected void initRoutes()
    {
        add("getLogin", "UserController", "getLogin", "/");
        add("getHome", "GeneralController", "getHome", "/home");
        add("postLogin", "UserController", "postLogin", "/postlogin");
        add("getRegister", "UserController", "getRegister", "/register");
        add("postRegister", "UserController", "postRegister", "/postregister");
        add("logout", "UserController", "logout", "/logout");
        
     
        
        //home-personal
        add("getMyClasses", "UserController", "getMyClasses", "/myclasses");
        add("getMyDepartment", "UserController", "getMyDepartment", "/department");
        add("getClassPage", "UserController", "getClassPage", "/class/class={classId}");
        add("getClassAssessments", "UserController", "getClassAssessments", "/class/class={classId}/assessments");
        
        add("postCreateAssessment", "AdminController", "postCreateAssessment", "/makeassessment");
        add("postModifyAssessment", "AdminController", "postModifyAssessment", "/modifyassessment");
        add("postDeleteAssessment", "AdminController", "postDeleteAssessment", "/deleteassessment");
        
        add("getAssessmentSubmissions", "AdminController", "getAssessmentSubmissions", "/assessment={assessId}/submissions");
        add("postModifySubmission", "AdminController", "postModifySubmission", "/modifysubmission");
        add("postRemoveSubmission", "AdminController", "postRemoveSubmission", "/removesubmission");
        add("postMarkSubmission", "AdminController", "postMarkSubmission", "/marksubmission");
        add("postFindStudentSubmission", "AdminController", "postFindStudentSubmission", "/findsubmission");
        
        //Admin panel
        add("getAdmincp", "AdminController", "getAdmincp", "/admincp");
        add("getStudents", "AdminController", "getStudents", "/students");
        add("getStudentList", "AdminController", "getStudentList", "/students/list/page={page}/numresults={numResults}");
        add("postRemoveStudent", "AdminController", "postRemoveStudent", "/students/remove");
        add("postModifyStudent", "AdminController", "postModifyStudent", "/students/modify");
        add("postSearchStudent", "AdminController", "postSearchStudent", "/students/search");
    }
}
