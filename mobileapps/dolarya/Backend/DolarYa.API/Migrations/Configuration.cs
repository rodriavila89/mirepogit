using AutoMapper.Internal;
using DolarYa.Business.Models;
using Microsoft.WindowsAzure.Mobile.Service.Tables;

namespace DolarYa.API.Migrations
{
    using System;
    using System.Data.Entity;
    using System.Data.Entity.Migrations;
    using System.Linq;

    internal sealed class Configuration : DbMigrationsConfiguration<DolarYa.API.Models.MobileServiceContext>
    {
        public Configuration()
        {
            AutomaticMigrationsEnabled = false;
            SetSqlGenerator("System.Data.SqlClient", new EntityTableSqlGenerator());
            ContextKey = "DolarYa.API.Models.MobileServiceContext";
        }

        protected override void Seed(DolarYa.API.Models.MobileServiceContext context)
        {
            //  This method will be called after migrating to the latest version.

            //  You can use the DbSet<T>.AddOrUpdate() helper extension method 
            //  to avoid creating duplicate seed data. E.g.
            //
            //    context.People.AddOrUpdate(
            //      p => p.FullName,
            //      new Person { FullName = "Andrew Peters" },
            //      new Person { FullName = "Brice Lambson" },
            //      new Person { FullName = "Rowan Miller" }
            //    );
            //
            if (!context.Users.Any())
            {
                context.Users.AddOrUpdate(user => user.Username,
                    new User { Username = "Admin", Password = "adminadmin", Id = Guid.NewGuid().ToString() });
            }
        }
    }
}
