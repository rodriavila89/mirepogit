using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using System.Web.Http;
using DolarYa.API.Models;
using DolarYa.Business.Enum;
using DolarYa.Business.Models;
using Microsoft.WindowsAzure.Mobile.Service;

namespace DolarYa.API.ScheduledJobs
{
    public class UpdateExchangeRateJob : ScheduledJob
    {
        private MobileServiceContext db = new MobileServiceContext();
        public override async Task ExecuteAsync()
        {
            try
            {
                var now = TimeZoneInfo.ConvertTime(DateTime.Now,
                    TimeZoneInfo.FindSystemTimeZoneById("Argentina Standard Time"));
                var isWeekDay = now.DayOfWeek != DayOfWeek.Saturday && now.DayOfWeek != DayOfWeek.Sunday;
                var todayAutomaticNotifications = db.AutomaticNotifications.Where(x => x.DateSent >= now.Date).ToList();
                AutomaticNotification automaticNotification = null;
                //Verifica que solo busque actualizaciones de Lunes a Viernes de 10hs a 17hs
                if (isWeekDay && now.Hour >= 10 && now.Hour <= 17)
                {
                    var addedList = new List<CurrencyRate>();
                    var latestUpdatedList = await CurrencyRate.GetLatestRatesAsync();
                    var latestUpdate = latestUpdatedList.OrderByDescending(x => x.UltimaActualizacion).FirstOrDefault();
                    //Si la ultima fecha de actualizacion devuelta es anterior a la fecha actual, se interrumpe el proceso
                    if (latestUpdate == null || latestUpdate.UltimaActualizacion.Date < now.Date) return;
                    var todayList = db.CurrencyRates.Where(x => x.LastUpdated >= now.Date);
                    foreach (var item in latestUpdatedList)
                    {
                        var newRate = new CurrencyRate(item);
                        var existingRate = todayList.FirstOrDefault(x => x.CurrencyId == newRate.CurrencyId);
                        //Si en la fecha actual ya hay una cotización, la actualiza. Si no la hay, la agrega como nueva
                        if (existingRate != null && existingRate.LastUpdated.Date == now.Date)
                            existingRate.UpdateRate(newRate);
                        else
                            addedList.Add(newRate);
                    }
                    db.CurrencyRates.AddRange(addedList);
                    db.SaveChanges();
                    //Verifica que hayan cotizaciones para el ultimo mes en todas las monedas, en caso negativo busca el historial y lo guarda en la DB para mayor respuesta cuando los usuarios lo pidan
                    foreach (var addedRate in addedList)
                    {
                        var rate = addedRate;
                        var lastMonth = now.AddMonths(-1).Date;
                        if (!db.CurrencyRates.Any(x => x.CurrencyId == rate.CurrencyId && x.LastUpdated >= lastMonth))
                            continue;
                        var historyList = await CurrencyRate.GetHistoryRatesAsync(rate.CurrencyId, lastMonth);
                        var lastMonthList =
                            db.CurrencyRates.Where(
                                x => x.CurrencyId == rate.CurrencyId && x.LastUpdated >= lastMonth).ToList();
                        var historyRatesToAdd =
                            historyList.Where(
                                history => lastMonthList.All(x => x.LastUpdated.Date != history.Fecha.Date)).ToList();
                        foreach (var history in historyRatesToAdd)
                        {
                            var newRate = new CurrencyRate
                            {
                                Id = Guid.NewGuid().ToString(),
                                CurrencyId = rate.CurrencyId,
                                Name = rate.Name,
                                Buy = history.Compra,
                                Sell = history.Venta,
                                LastUpdated = history.Fecha,
                                Sort = rate.Sort
                            };
                            db.CurrencyRates.Add(newRate);
                        }
                    }
                    db.SaveChanges();
                    var latestDolar =
                        db.CurrencyRates.OrderByDescending(x => x.LastUpdated).First(x => x.CurrencyId == 1);
                    //Se envia notificacion de apertura si no hay nunguna notificacion de apertura enviada en el dia
                    if (todayAutomaticNotifications.All(x => x.NotificationType != NotificationType.OpenMarket))
                    {
                        automaticNotification = Notification.SendPushNotification(latestDolar,
                            NotificationType.OpenMarket, Services);
                    }
                    //Si ya se envio la notificacion de apertura, verifica que para enviar una nueva notificacion, haya pasado 1hs
                    else if (todayAutomaticNotifications.All(x => x.DateSent <= now.AddHours(-1)))
                    {
                        automaticNotification = Notification.SendPushNotification(latestDolar, NotificationType.Hourly,
                            Services);
                    }

                }
                else if (isWeekDay && now.Hour >= 18 && todayAutomaticNotifications.Any() &&
                         todayAutomaticNotifications.All(x => x.NotificationType != NotificationType.CloseMarket))
                {
                    var latestDolar =
                        db.CurrencyRates.OrderByDescending(x => x.LastUpdated).First(x => x.CurrencyId == 1);
                    automaticNotification = Notification.SendPushNotification(latestDolar,
                        NotificationType.CloseMarket, Services);
                }
                if (automaticNotification != null)
                {
                    db.AutomaticNotifications.Add(automaticNotification);
                    db.SaveChanges();
                }
            }
            catch (Exception ex)
            {
                throw ex;
            }
        }
    }
}