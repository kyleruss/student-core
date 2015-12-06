//====================================
//	Kyle Russell
//	jdamvc
//	Routes
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
            new Path("getLogin", "UserController", "getLogin", "/login"),
            new Path("postRegister", "UserController", "postRegister", "/postregister"),
            new Path("logout", "UserController", "logout", "/logout"),
            new Path("getNotifications", "UserController", "getNotifications", "/notifications")
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
            new Path("postCreateAssessment", "AdminController", "postCreateAssessment", "/make"),
            new Path("postModifyAssessment", "AdminController", "postModifyAssessment", "/modify"),
            new Path("postDeleteAssessment", "AdminController", "postDeleteAssessment", "/delete"),
            new Path("postEditAssessment", "AdminController", "postEditAssessment", "/edit")
        
        }));
        
        RouteGroup submissions;
        registerGroup(submissions = new RouteGroup("submissions", assessments, new Path[]
        {
            new Path("getAssessmentSubmissions", "AdminController", "getAssessmentSubmissions", "/assessment={assessId}/submissions"),
            new Path("postModifySubmission", "AdminController", "postModifySubmission", "/modifysubmission"),
            new Path("postRemoveSubmission", "AdminController", "postRemoveSubmission", "/removesubmission"),
            new Path("postMarkSubmission", "AdminController", "postMarkSubmission", "/marksubmission"),
            new Path("postFindStudentSubmission", "AdminController", "postFindStudentSubmission", "/findsubmission"),
            new Path("postAddSubmission", "AdminController", "postAddSubmission", "/addsubmission"),
            new Path("postEditSubmission", "AdminController", "postEditSubmission", "/editsubmission")
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
            new Path("postEditRole", "AdminController", "postEditRole", "/panel/roles/edit"),
            new Path("postAssignRole", "AdminController", "postAssignRole", "/panel/roles/assign"),
            new Path("postAddDepartment", "AdminController", "postAddDepartment", "/panel/departments/add"),
            new Path("postRemoveDepartment", "AdminController", "postRemoveDepartment", "/panel/departments/remove"),
            new Path("postEditDepartment", "AdminController", "postEditDepartment", "/panel/departments/edit"),
            new Path("postAddClass", "AdminController", "postAddClass", "/panel/classes/add"),
            new Path("postEditClass", "AdminController", "postEditClass", "/panel/classes/edit"),
            new Path("postRemoveClass", "AdminController", "postRemoveClass", "/panel/classes/remove"),
            new Path("postEditUser", "AdminController", "postEditUser", "/panel/users/edit"),
            new Path("postAddEnrolment", "AdminController", "postAddEnrolment", "/panel/enrolment/add"),
            new Path("postRemoveEnrolment", "AdminController", "postRemoveEnrolment", "/panel/enrolment/remove")
        }));

    }
}
