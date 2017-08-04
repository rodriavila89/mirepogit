using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Http;
using System.Threading.Tasks;
using System.Web;
using System.Web.Mvc;
using DolarYa.Backoffice.Filters;
using DolarYa.Business.Enum;
using DolarYa.Business.Models;
using Microsoft.WindowsAzure.MobileServices;

namespace DolarYa.Backoffice.Controllers
{
    public class NotificationController : Controller
    {
        //public static MobileServiceClient MobileService = new MobileServiceClient("http://localhost:59990");
        public static MobileServiceClient MobileService = new MobileServiceClient("https://cronista-dolarya.azure-mobile.net/", "DlxPsJAXKPpqwUOIxSTrHijfiVxlXb32");

        [HttpGet]
        public async Task<ActionResult> Index()
        {
            var response = await MobileService.InvokeApiAsync<List<Notification>>("Notifications", HttpMethod.Get, null);
            var list = response.ToList();
            return View(list);
        }

        [HttpGet]
        public ActionResult Send()
        {
            return View();
        }

        [HttpPost]
        public async Task<ActionResult> Send(Notification notification)
        {
            try
            {
                notification.NotificationType =  NotificationType.Custom;
                await MobileService.InvokeApiAsync<Notification, Notification>("Notifications", notification, HttpMethod.Post, null);
            }
            catch (Exception ex)
            {
                ViewBag.ErrorMessage = ex.Message;
            }
            return RedirectToAction("Index");
        }
        [HttpGet]
        public async Task<ActionResult> SendAutomatic(NotificationType notificationType)
        {
            var notification = new Notification { NotificationType = notificationType };
            await MobileService.InvokeApiAsync<Notification, Notification>("Notifications", notification, HttpMethod.Post, null);
            return RedirectToAction("Index");
        }
    }
}