namespace DolarYa.API.Migrations
{
    using System;
    using System.Data.Entity.Migrations;
    
    public partial class UserFieldsAdded : DbMigration
    {
        public override void Up()
        {
            AddColumn("dbo.Users", "Username", c => c.String());
            AddColumn("dbo.Users", "Password", c => c.String());
        }
        
        public override void Down()
        {
            DropColumn("dbo.Users", "Password");
            DropColumn("dbo.Users", "Username");
        }
    }
}
