using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Microsoft.WindowsAzure.Mobile.Service;

namespace DolarYa.Business.Models
{
    public class User : EntityData
    {
        public string Username { get; set; }
        public string Password { get; set; }
    }
}
