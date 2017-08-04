using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using DolarYa.Business.Enum;
using Microsoft.WindowsAzure.Mobile.Service;

namespace DolarYa.Business.Models
{
    public class AutomaticNotification : EntityData
    {
        public NotificationType NotificationType { get; set; }
        public string Content { get; set; }
        public DateTime DateSent { get; set; }
    }
}
