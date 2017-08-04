using System;
using System.Net.Http;
using System.Threading.Tasks;
using System.Web.Mvc;
using DolarYa.Backoffice.Common;
using DolarYa.Backoffice.Filters;
using DolarYa.Business.Models;
using DolarYa.Business.DataObjects;
using Microsoft.WindowsAzure.MobileServices;

namespace DolarYa.Backoffice.Controllers
{
    [AllowAnonymousUser]
    public class AccountController : Controller
    {
        //public static MobileServiceClient MobileService = new MobileServiceClient("http://localhost:59990");
        public static MobileServiceClient MobileService = new MobileServiceClient("https://cronista-dolarya.azure-mobile.net/", "DlxPsJAXKPpqwUOIxSTrHijfiVxlXb32");

        [HttpGet]
        public ActionResult Login()
        {
            return View();
        }

        [HttpPost]
        public async Task<ActionResult> Login(string username, string password)
        {
            try
            {
                var sarasa = System.Web.HttpContext.Current;
                var user = await LoginAsync(new Login() { Username = username, Password = password });
                if (user == null)
                {
                    throw new ApplicationException("Usuario o password invalidos.");
                }
                SessionHandler.SetUserId(sarasa, user.Id);
                return RedirectToAction("Index", "Notification");
            }
            catch (Exception ex)
            {
                ViewBag.ErrorMessage = ex.Message;
            }
            return View();
        }

        public User Login(Login login)
        {
            var task = LoginAsync(login);
            task.ConfigureAwait(false);
            return task.Result;
        }

        public async Task<User> LoginAsync(Login login)
        {
            return await MobileService.InvokeApiAsync<Login, User>("Users/Login", login, HttpMethod.Post, null).ConfigureAwait(false);
        }
    }
}