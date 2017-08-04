using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Web;
using System.Web.Mvc;
using DolarYa.Backoffice.Common;

namespace DolarYa.Backoffice.Filters
{
    [AttributeUsage(AttributeTargets.Class | AttributeTargets.Method, AllowMultiple = false, Inherited = true)]
    public sealed class AllowAnonymousUser : AuthorizeAttribute
    {
        protected override bool AuthorizeCore(HttpContextBase httpContext)
        {
            return SessionHandler.IsUserLogged;
        }

        public override void OnAuthorization(AuthorizationContext filterContext)
        {
            var isInAction = filterContext.ActionDescriptor.IsDefined(typeof(AllowAnonymousUser), true);
            var isInController = filterContext.ActionDescriptor.ControllerDescriptor.IsDefined(typeof(AllowAnonymousUser), true);
            if (!isInAction && !isInController)
            {
                base.OnAuthorization(filterContext);
            }
        }
    }
}
