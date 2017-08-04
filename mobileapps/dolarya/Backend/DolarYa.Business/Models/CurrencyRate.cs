using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Net.Http.Headers;
using System.Runtime.CompilerServices;
using System.Text;
using System.Threading.Tasks;
using DolarYa.Business.DataObjects;
using DolarYa.Business.Models;
using Microsoft.WindowsAzure.Mobile.Service;
using Newtonsoft.Json;
using Newtonsoft.Json.Linq;

namespace DolarYa.Business.Models
{
    public class CurrencyRate : EntityData
    {

        public CurrencyRate()
        {

        }
        public CurrencyRate(CurrencyRateDTO currencyRateDto)
        {
            Id = Guid.NewGuid().ToString();
            CurrencyId = currencyRateDto.Id;
            Name = currencyRateDto.Nombre;
            PercentageChange = currencyRateDto.VariacionPorcentual;
            Buy = currencyRateDto.Compra;
            Sell = currencyRateDto.Venta;
            LastUpdated = currencyRateDto.UltimaActualizacion;
            Sort = currencyRateDto.Orden;
        }

        public int CurrencyId { get; set; }
        public string Name { get; set; }
        public decimal PercentageChange { get; set; }
        public decimal Buy { get; set; }
        public decimal Sell { get; set; }
        public DateTime LastUpdated { get; set; }
        public int Sort { get; set; }

        public void UpdateRate(CurrencyRate newRate)
        {
            Buy = newRate.Buy;
            Sell = newRate.Sell;
            PercentageChange = newRate.PercentageChange;
            LastUpdated = newRate.LastUpdated;
        }

        public async static Task<List<CurrencyRateDTO>> GetLatestRatesAsync()
        {
            List<CurrencyRateDTO> rates;
            using (var client = new HttpClient())
            {
                var request = new StringContent(string.Empty);
                var credentials = Encoding.ASCII.GetBytes("cronista:7hpFQNr");

                client.DefaultRequestHeaders.Accept.Clear();
                client.DefaultRequestHeaders.Accept.Add(new MediaTypeWithQualityHeaderValue("application/json"));
                client.DefaultRequestHeaders.Authorization = new AuthenticationHeaderValue("Basic", Convert.ToBase64String(credentials));

                HttpResponseMessage response = await client.PostAsync("http://mercado.cronista.com:8088/mercados/moneda", request).ConfigureAwait(false);
                if (response.IsSuccessStatusCode)
                {
                    string json = await response.Content.ReadAsStringAsync();
                    rates = JsonConvert.DeserializeObject<List<CurrencyRateDTO>>(json);
                }
                else
                {
                    throw new ApplicationException(string.Format("Status code [{0}] and message: {1}", response.StatusCode, await response.Content.ReadAsStringAsync()));
                }
            }
            return rates;
        }

        public async static Task<List<CurrencyRateHistoryDTO>> GetHistoryRatesAsync(int id, DateTime fromDate)
        {
            List<CurrencyRateHistoryDTO> rates;
            using (var client = new HttpClient())
            {
                var postData = "fecha=" + fromDate.ToString("yyyy-mm-dd");
                var request = new StringContent(postData);
                var credentials = Encoding.ASCII.GetBytes("cronista:7hpFQNr");

                client.DefaultRequestHeaders.Accept.Clear();
                client.DefaultRequestHeaders.Accept.Add(new MediaTypeWithQualityHeaderValue("application/json"));
                client.DefaultRequestHeaders.Authorization = new AuthenticationHeaderValue("Basic", Convert.ToBase64String(credentials));

                HttpResponseMessage response = await client.PostAsync("http://mercado.cronista.com:8088/mercados/moneda/" + id + "/historia", request).ConfigureAwait(false);
                if (response.IsSuccessStatusCode)
                {
                    string json = await response.Content.ReadAsStringAsync();
                    rates = JsonConvert.DeserializeObject<List<CurrencyRateHistoryDTO>>(json);
                }
                else
                {
                    throw new ApplicationException(string.Format("Status code [{0}] and message: {1}", response.StatusCode, await response.Content.ReadAsStringAsync()));
                }
            }
            return rates;
        }
    }
}
