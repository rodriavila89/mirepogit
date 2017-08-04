using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using DolarYa.Business.Models;

namespace DolarYa.Business.DataObjects
{
    public class CurrencyRateDTO
    {
        public int Id { get; set; }
        public string Nombre { get; set; }
        public decimal VariacionPorcentual { get; set; }
        public decimal Compra { get; set; }
        public decimal Venta { get; set; }
        public DateTime UltimaActualizacion { get; set; }
        public int Orden { get; set; }

        public CurrencyRateDTO() { }

        public CurrencyRateDTO(CurrencyRate rate)
        {
            Id = rate.CurrencyId;
            Nombre = rate.Name;
            VariacionPorcentual = rate.PercentageChange;
            Compra = rate.Buy;
            Venta = rate.Sell;
            UltimaActualizacion = rate.LastUpdated;
            Orden = rate.Sort;
        }
    }
}
