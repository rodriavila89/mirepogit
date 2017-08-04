using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Http;
using System.Threading.Tasks;
using System.Web;
using System.Web.Mvc;
using DolarYa.Backoffice.Filters;
using DolarYa.Business.DataObjects;
using DolarYa.Business.Models;
using Microsoft.WindowsAzure.MobileServices;

namespace DolarYa.Backoffice.Controllers
{
    public class HomeController : Controller
    {
        public static MobileServiceClient MobileService = new MobileServiceClient("https://cronista-dolarya.azure-mobile.net/", "DlxPsJAXKPpqwUOIxSTrHijfiVxlXb32");
        public ActionResult Index()
        {
            
            return View();
        }

        [HttpGet]
        public async Task<ActionResult> Cotizaciones()
        {
            var response = await MobileService.InvokeApiAsync<List<CurrencyRateDTO>>("CurrencyRates", HttpMethod.Get, null);
            var list = response.ToList();
            return View(list);
        }
    }
}