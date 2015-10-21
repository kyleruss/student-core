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
        add("getErrorPage", "BaseController", "getErrorView", "/error");
        
        RouteGroup user;
        registerGroup(user = new RouteGroup("user", new Path[]
        {
            new Path("getHome", "UserController", "getHome", "/home"),
            new Path("postLogin", "UserController", "postLogin", "/postlogin"),
            new Path("getRegister", "UserController", "getRegister", "/register"),
            new Path("getLogin", "UserController", "getLogin", "/"),
            new Path("postRegister", "UserController", "postRegister", "/postregister"),
            new Path("logout", "UserController", "logout", "/logout")
        }));
        
        RouteGroup classes;
        registerGroup(classes = new RouteGroup("class", new Path[]
        {
            new Path("getMyClasses", "UserController", "getMyClasses", "/classes"),
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
        
        //Admin panel
        RouteGroup admin;
        registerGroup(submissions = new RouteGroup("admin", new Path[]
        {
            new Path("getAdmincp", "AdminController", "getAdmincp", "/panel"),
            new Path("getStudents", "AdminController", "getStudents", "/students"),
            new Path("getStudentList", "AdminController", "getStudentList", "/students/list/page={page}/numresults={numResults}"),
            new Path("postRemoveStudent", "AdminController", "postRemoveStudent", "/students/remove"),
            new Path("postModifyStudent", "AdminController", "postModifyStudent", "/students/modify"),
            new Path("postSearchStudent", "AdminController", "postSearchStudent", "/students/search"),
            new Path("postAddAdminAnnouncement", "AdminController", "postAddAdminAnnouncement", "/panel/announcements/add"),
            new Path("postEditAdminAnnouncement", "AdminController", "postEditAdminAnnouncement", "/panel/announcements/edit"),
            new Path("postRemoveAdminAnnouncement", "AdminController", "postRemoveAdminAnnouncement", "/panel/announcements/remove"),
            new Path("postRemoveRole", "AdminController", "postRemoveRole", "/panel/roles/remove"),
            new Path("postAddRole", "AdminController", "postAddRole", "/panel/roles/add"),
            new Path("postEditRole", "AdminController", "postEditRole", "/panel/roles/edit")
        }));

    }
}
