using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using DolarYa.Business.Models;

namespace DolarYa.Business.DataObjects
{
    public class CurrencyRateHistoryDTO
    {
        public decimal Compra { get; set; }
        public decimal Venta { get; set; }
        public DateTime Fecha { get; set; }

    }
}
