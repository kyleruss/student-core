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
      //  add("getLogin", "UserController", "getLogin", "/");
        add("getHome", "GeneralController", "getHome", "/home");
    //    add("postLogin", "UserController", "postLogin", "/postlogin");
     //   add("getRegister", "UserController", "getRegister", "/register");
    //    add("postRegister", "UserController", "postRegister", "/postregister");
      //  add("logout", "UserController", "logout", "/logout");
        
     
        
        //home-personal
     /*   add("getMyClasses", "UserController", "getMyClasses", "/myclasses");
        add("getMyDepartment", "UserController", "getMyDepartment", "/department");
        add("getClassPage", "UserController", "getClassPage", "/class/class={classId}");
        add("getClassAssessments", "UserController", "getClassAssessments", "/class/class={classId}/assessments"); */
        
        RouteGroup user;
        registerGroup(user = new RouteGroup("user", new Path[]
        {
            new Path("postLogin", "UserController", "postLogin", "/postlogin"),
            new Path("getRegister", "UserController", "getRegister", "/register"),
            new Path("getLogin", "UserController", "getLogin", "/"),
            new Path("postRegister", "UserController", "postRegister", "/postregister"),
            new Path("logout", "UserController", "logout", "/logout")
        }));
        
        RouteGroup classes;
        registerGroup(classes = new RouteGroup("class", new Path[]
        {
            new Path("getMyClasses", "UserController", "getMyClasses", "/myclasses"),
            new Path("getMyDepartment", "UserController", "getMyDepartment", "/department"),
            new Path("getClassPage", "UserController", "getClassPage", "/class={classId}"),
            new Path("getClassAssessments", "UserController", "getClassAssessments", "/class={classId}/assessments"),
        }));
        
        RouteGroup assessments;
        registerGroup(assessments = new RouteGroup("assessment", classes, new Path[]
        {
            new Path("postCreateAssessment", "AdminController", "postCreateAssessment", "/makeassessment"),
            new Path("postModifyAssessment", "AdminController", "postModifyAssessment", "/modifyassessment"),
            new Path("postDeleteAssessment", "AdminController", "postDeleteAssessment", "/deleteassessment"),
        
        }));
        
        RouteGroup submissions;
        registerGroup(submissions = new RouteGroup("submissions", assessments, new Path[]
        {
            new Path("getAssessmentSubmissions", "AdminController", "getAssessmentSubmissions", "/assessment={assessId}/submissions"),
            new Path("postModifySubmission", "AdminController", "postModifySubmission", "/modifysubmission"),
            new Path("postRemoveSubmission", "AdminController", "postRemoveSubmission", "/removesubmission"),
            new Path("postMarkSubmission", "AdminController", "postMarkSubmission", "/marksubmission"),
            new Path("postFindStudentSubmission", "AdminController", "postFindStudentSubmission", "/findsubmission")
        }));
        
     /*   add("postCreateAssessment", "AdminController", "postCreateAssessment", "/makeassessment");
        add("postModifyAssessment", "AdminController", "postModifyAssessment", "/modifyassessment");
        add("postDeleteAssessment", "AdminController", "postDeleteAssessment", "/deleteassessment");
        
        add("getAssessmentSubmissions", "AdminController", "getAssessmentSubmissions", "/assessment={assessId}/submissions");
        add("postModifySubmission", "AdminController", "postModifySubmission", "/modifysubmission");
        add("postRemoveSubmission", "AdminController", "postRemoveSubmission", "/removesubmission");
        add("postMarkSubmission", "AdminController", "postMarkSubmission", "/marksubmission");
        add("postFindStudentSubmission", "AdminController", "postFindStudentSubmission", "/findsubmission"); */
        
        
        //Admin panel
        RouteGroup admin;
        registerGroup(submissions = new RouteGroup("admin", new Path[]
        {
            new Path("getAdmincp", "AdminController", "getAdmincp", "/panel"),
            new Path("getStudents", "AdminController", "getStudents", "/students"),
            new Path("getStudentList", "AdminController", "getStudentList", "/students/list/page={page}/numresults={numResults}"),
            new Path("postRemoveStudent", "AdminController", "postRemoveStudent", "/students/remove"),
            new Path("postModifyStudent", "AdminController", "postModifyStudent", "/students/modify"),
            new Path("postSearchStudent", "AdminController", "postSearchStudent", "/students/search")
        }));

    }
    
    public static void main(String[] args)
    {
        Routes routes   =   new Routes();
    }
}
