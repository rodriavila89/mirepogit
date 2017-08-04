using System;
using System.Collections.Generic;
using System.Data;
using System.Data.Entity;
using System.Data.Entity.Infrastructure;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Threading.Tasks;
using System.Web.Http;
using System.Web.Http.Description;
using DolarYa.API.Models;
using DolarYa.Business.Enum;
using DolarYa.Business.Models;
using Microsoft.WindowsAzure.Mobile.Service;

namespace DolarYa.API.Controllers
{
    public class NotificationsController : ApiController
    {
        private MobileServiceContext db = new MobileServiceContext();
        public ApiServices Services { get; set; }
        // GET: api/Notifications
        public List<Notification> GetNotifications()
        {
            return db.Notifications.ToList();
        }

        // GET: api/Notifications/5
        [ResponseType(typeof(Notification))]
        public async Task<IHttpActionResult> GetNotification(string id)
        {
            Notification notification = await db.Notifications.FindAsync(id);
            if (notification == null)
            {
                return NotFound();
            }

            return Ok(notification);
        }

        // POST: api/Notifications
        [ResponseType(typeof(Notification))]
        public async Task<IHttpActionResult> PostNotification(Notification notification)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            if (notification.NotificationType == NotificationType.Custom)
            {
                notification.Id = Guid.NewGuid().ToString();
                db.Notifications.Add(notification);
                await db.SaveChangesAsync();
                Notification.SendPushNotification(notification, Services);
            }
            else
            {
                var latestDolar = db.CurrencyRates.OrderByDescending(x => x.LastUpdated).First(x => x.CurrencyId == 1);
                var automaticNotification = Notification.SendPushNotification(latestDolar, notification.NotificationType, Services);
                if (automaticNotification != null)
                {
                    db.AutomaticNotifications.Add(automaticNotification);
                    db.SaveChanges();
                }
            }

            return Ok(notification);
        }

        // DELETE: api/Notifications/5
        [ResponseType(typeof(Notification))]
        public async Task<IHttpActionResult> DeleteNotification(string id)
        {
            Notification notification = await db.Notifications.FindAsync(id);
            if (notification == null)
            {
                return NotFound();
            }

            db.Notifications.Remove(notification);
            await db.SaveChangesAsync();

            return Ok(notification);
        }

        protected override void Dispose(bool disposing)
        {
            if (disposing)
            {
                db.Dispose();
            }
            base.Dispose(disposing);
        }

        private bool NotificationExists(string id)
        {
            return db.Notifications.Count(e => e.Id == id) > 0;
        }
    }
}