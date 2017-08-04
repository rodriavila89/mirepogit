namespace DolarYa.API.Migrations
{
    using System;
    using System.Data.Entity.Migrations;
    
    public partial class ExchangeRateAdded1 : DbMigration
    {
        public override void Up()
        {
            AlterColumn("dbo.CurrencyRates", "PercentageChange", c => c.Decimal(nullable: false, precision: 16, scale: 5));
            AlterColumn("dbo.CurrencyRates", "Buy", c => c.Decimal(nullable: false, precision: 16, scale: 5));
            AlterColumn("dbo.CurrencyRates", "Sell", c => c.Decimal(nullable: false, precision: 16, scale: 5));
        }
        
        public override void Down()
        {
            AlterColumn("dbo.CurrencyRates", "Sell", c => c.Decimal(nullable: false, precision: 18, scale: 2));
            AlterColumn("dbo.CurrencyRates", "Buy", c => c.Decimal(nullable: false, precision: 18, scale: 2));
            AlterColumn("dbo.CurrencyRates", "PercentageChange", c => c.Decimal(nullable: false, precision: 18, scale: 2));
        }
    }
}
