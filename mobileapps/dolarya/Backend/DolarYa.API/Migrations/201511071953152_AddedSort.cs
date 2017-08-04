namespace DolarYa.API.Migrations
{
    using System;
    using System.Data.Entity.Migrations;
    
    public partial class AddedSort : DbMigration
    {
        public override void Up()
        {
            AddColumn("dbo.CurrencyRates", "Sort", c => c.Int(nullable: false, defaultValue:1));
        }
        
        public override void Down()
        {
            DropColumn("dbo.CurrencyRates", "Sort");
        }
    }
}
