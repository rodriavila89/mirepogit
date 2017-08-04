using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Web.Http;
using DolarYa.Business.Enum;
using Microsoft.WindowsAzure.Mobile.Service;

namespace DolarYa.Business.Models
{
    public class Notification : EntityData
    {
        public NotificationType NotificationType { get; set; }
        public string Content { get; set; }



        public static AutomaticNotification SendPushNotification(CurrencyRate rate, NotificationType notificationType, ApiServices services)
        {
            try
            {
                var now = TimeZoneInfo.ConvertTime(DateTime.Now, TimeZoneInfo.FindSystemTimeZoneById("Argentina Standard Time"));
                var category = notificationType == NotificationType.Hourly ? "Hourly" : "OpenClose";
                var msj = notificationType == NotificationType.Hourly ? string.Empty : notificationType == NotificationType.OpenMarket ? "Apertura " : "Cierre ";
                msj += rate.Name + ": $" + decimal.Round(rate.Buy, 2, MidpointRounding.AwayFromZero) + " / $" + decimal.Round(rate.Sell, 2, MidpointRounding.AwayFromZero);

                var automaticNotification = new AutomaticNotification
                {
                    Id = Guid.NewGuid().ToString(),
                    NotificationType = notificationType,
                    DateSent = now,
                    Content = msj
                };
                Dictionary<string, string> data = new Dictionary<string, string>()
                {
                    {"message", msj},
                    {"category", category}
                };
                GooglePushMessage googleMessage = new GooglePushMessage(data, TimeSpan.FromHours(1));
				var result = services.Push.SendAsync(googleMessage, category).Result;
                services.Log.Info(result.State.ToString());


				ApplePushMessage appleMessage = new ApplePushMessage(msj, TimeSpan.FromHours(1));
                result = services.Push.SendAsync(appleMessage, category == "OpenClose" ? "OpenAndClose" : category).Result;
				services.Log.Info(result.State.ToString());

				return automaticNotification;
            }
            catch (Exception ex)
            {
                services.Log.Error(ex.Message, null, "Push.SendAsync Error");
                return null;
            }
        }

        public static void SendPushNotification(Notification notification, ApiServices services)
        {
            try
            {
                Dictionary<string, string> data = new Dictionary<string, string>()
                {
                    {"message", notification.Content}
                };
                GooglePushMessage googleMessage = new GooglePushMessage(data, TimeSpan.FromHours(1));
				var result = services.Push.SendAsync(googleMessage).Result;
                services.Log.Info(result.State.ToString());

				data = new Dictionary<string, string>()
				{
					{"alert", notification.Content}
				};
				ApplePushMessage appleMessage = new ApplePushMessage(notification.Content, TimeSpan.FromHours(1));
				result = services.Push.SendAsync(appleMessage).Result;
				services.Log.Info(result.State.ToString());
            }
            catch (Exception ex)
            {
                services.Log.Error(ex.Message, null, "Push.SendAsync Error");
            }
        }
    }
}
