using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Web;
using System.Web.Mvc;
using System.Web.Optimization;
using System.Web.Routing;
using DolarYa.Backoffice.Controllers;

namespace DolarYa.Backoffice
{
    public class MvcApplication : System.Web.HttpApplication
    {
        protected void Application_Start()
        {
            AreaRegistration.RegisterAllAreas();
            FilterConfig.RegisterGlobalFilters(GlobalFilters.Filters);
            RouteConfig.RegisterRoutes(RouteTable.Routes);
            BundleConfig.RegisterBundles(BundleTable.Bundles);
        }

        //protected void Application_EndRequest(object sender, EventArgs e)
        //{
        //    HttpApplication application = (HttpApplication)sender;
        //    var status = (HttpStatusCode)application.Response.StatusCode;
        //    if (status != HttpStatusCode.Unauthorized && status != HttpStatusCode.NotFound) return;
        //    var rd = new RouteData();
        //    //application.Response.ClearContent();
        //    rd.Values.Add("controller", "Account");
        //    rd.Values.Add("action", "Login");
        //    rd.Values.Add("redirectUrl", HttpUtility.UrlEncode(application.Request.RawUrl));
        //    IController controller = new AccountController();
        //    controller.Execute(new RequestContext(new HttpContextWrapper(Context), rd));
        //}
    }
}
