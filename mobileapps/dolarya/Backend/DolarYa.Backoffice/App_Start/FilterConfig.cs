using System.Web;
using System.Web.Mvc;
using DolarYa.Backoffice.Filters;

namespace DolarYa.Backoffice
{
    public class FilterConfig
    {
        public static void RegisterGlobalFilters(GlobalFilterCollection filters)
        {
            filters.Add(new HandleErrorAttribute());
            filters.Add(new AllowAnonymousUser());
        }
    }
}
