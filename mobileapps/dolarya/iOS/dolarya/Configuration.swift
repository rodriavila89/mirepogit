//
//  Configuration.swift
//  dolarya
//
//  Created by Ramiro Rinaldi on 10/17/15.
//  Copyright © 2015 El Cronista. All rights reserved.
//

import Foundation
import CoreData

class Configuration: NSManagedObject {
    
    
    static func Save(moc: NSManagedObjectContext, configuration: Configuration) -> Bool
    {
        do
        {
            
            let fetchRequest = NSFetchRequest(entityName: "Configuration")
            let fetchedEntities = try moc.executeFetchRequest(fetchRequest) as? [Configuration]
            let existingConfiguration = fetchedEntities![0]
            existingConfiguration.recieveHourly = configuration.recieveHourly
            existingConfiguration.recieveOpenClose = configuration.recieveOpenClose
            try moc.save()
            return true
        }
        catch
        {
            return false
        }
    }
    
    // Insert code here to add functionality to your managed object subclass
    static func Get(moc: NSManagedObjectContext) -> Configuration?
    {
        do
        {
            var configuration: Configuration
            let fetchRequest = NSFetchRequest(entityName: "Configuration")
            let fetchedEntities = try moc.executeFetchRequest(fetchRequest) as? [Configuration]
            if(fetchedEntities!.count == 0)
            {
                configuration = NSEntityDescription.insertNewObjectForEntityForName("Configuration", inManagedObjectContext: moc) as! Configuration
                configuration.recieveOpenClose = true
                configuration.recieveHourly = false
                try moc.save()
                return configuration
            }
            else
            {
                return fetchedEntities![0];
                
            }
        }
        catch
        {
            return nil
        }
    }
    
    
}
