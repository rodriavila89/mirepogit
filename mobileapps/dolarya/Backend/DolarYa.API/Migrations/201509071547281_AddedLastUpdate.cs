namespace DolarYa.API.Migrations
{
    using System;
    using System.Data.Entity.Migrations;
    
    public partial class AddedLastUpdate : DbMigration
    {
        public override void Up()
        {
            AddColumn("dbo.CurrencyRates", "LastUpdated", c => c.DateTime(nullable: false));
        }
        
        public override void Down()
        {
            DropColumn("dbo.CurrencyRates", "LastUpdated");
        }
    }
}
