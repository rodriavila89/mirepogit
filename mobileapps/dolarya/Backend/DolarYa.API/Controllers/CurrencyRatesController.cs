using System;
using System.Collections.Generic;
using System.Globalization;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;
using System.Web.Http.Description;
using DolarYa.API.Models;
using DolarYa.Business.DataObjects;
using DolarYa.Business.Enum;
using DolarYa.Business.Models;

namespace DolarYa.API.Controllers
{
    public class CurrencyRatesController : ApiController
    {
        private MobileServiceContext db = new MobileServiceContext();

        // GET: api/CurrencyRates
        public List<CurrencyRateDTO> GetLatestRates()
        {
            var lastUpdated = db.CurrencyRates.OrderByDescending(x => x.LastUpdated).First().LastUpdated;
            var ratesList = db.CurrencyRates.Where(x => x.LastUpdated == lastUpdated).ToList();
            var dtoList = ratesList.Select(rate => new CurrencyRateDTO(rate)).ToList();
            return dtoList;
        }

        // GET: api/CurrencyRates/5
        [ResponseType(typeof(CurrencyRate))]
        public List<CurrencyRateDTO> GetCurrencyRates(int? id, string fromDate)
        {
            var dtoList = new List<CurrencyRateDTO>();
            DateTime oldestDate;
            if (DateTime.TryParseExact(fromDate, "yyyy-M-d", CultureInfo.InvariantCulture, DateTimeStyles.None, out oldestDate))
            {
                var list = db.CurrencyRates.Where(x => x.LastUpdated >= oldestDate).AsQueryable();
                if (id.HasValue)
                {
                    list = list.Where(x => x.CurrencyId == id.Value);
                }
                //list = list.Where(x => x.LastUpdated >= oldestDate);
                dtoList = list.ToList().Select(rate => new CurrencyRateDTO(rate)).ToList();
            }
            return dtoList;
        }

        protected override void Dispose(bool disposing)
        {
            if (disposing)
            {
                db.Dispose();
            }
            base.Dispose(disposing);
        }
    }
}