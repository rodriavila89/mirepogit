//
//  CurrencyRate.swift
//  dolarya
//
//  Created by Ramiro Rinaldi on 10/17/15.
//  Copyright © 2015 El Cronista. All rights reserved.
//

import Foundation
import CoreData

class CurrencyRate: NSManagedObject
{
    static func Save(moc: NSManagedObjectContext, json: JSON) -> Bool
    {
        do
        {
            let ultimaActualizacion = json["ultimaActualizacion"].data as! NSDate
            let id = json["id"].asInt32!
            var rate = try GetByDate(moc, currencyId: id, date: ultimaActualizacion)
            if(rate == nil)
            {
                rate = NSEntityDescription.insertNewObjectForEntityForName("CurrencyRate", inManagedObjectContext: moc) as! CurrencyRate
            }
            rate!.currencyId = id
            rate!.name = json["nombre"].asString!
            rate!.percentageChange = json["variacionPorcentual"].asFloat ?? 0
            rate!.buy = json["compra"].asFloat ?? 0
            rate!.sell = json["venta"].asFloat ?? 0
            rate!.lastUpdated = ultimaActualizacion
            rate!.sort = json["orden"].asInt32 ?? 0
            try moc.save()
            return true
        }
        catch
        {
            return false
        }
    }
    
    static func GetByDate(moc: NSManagedObjectContext, currencyId: Int32, date: NSDate) throws -> CurrencyRate?
    {
        let cal = NSCalendar(calendarIdentifier: NSCalendarIdentifierGregorian)!
        let components = cal.components([.Day , .Month, .Year], fromDate: date)
        let fromDate = cal.dateFromComponents(components)!
        
        
        var currencyRate: CurrencyRate?
        let fetchRequest = NSFetchRequest(entityName: "CurrencyRate")
        let primarySortDescriptor = NSSortDescriptor(key: "lastUpdated", ascending: false)
        let idPredicate = NSPredicate(format: "currencyId = %i", currencyId)
        let datePredicate = NSPredicate(format: "lastUpdated Between {%@, %@}", fromDate, date)
        let predicate = NSCompoundPredicate(type: NSCompoundPredicateType.AndPredicateType, subpredicates: [idPredicate, datePredicate])
        fetchRequest.fetchLimit = 1
        fetchRequest.predicate = predicate
        fetchRequest.sortDescriptors = [primarySortDescriptor]
        let fetchedEntities = try moc.executeFetchRequest(fetchRequest) as! [CurrencyRate]
        if(fetchedEntities.count > 0)
        {
            currencyRate = fetchedEntities[0];
        }
        return currencyRate
    }
    
    static func GetLatestUpdates(moc: NSManagedObjectContext) -> NSFetchRequest
    {
        let fetchRequest = NSFetchRequest(entityName: "CurrencyRate")
        let primarySortDescriptor = NSSortDescriptor(key: "sort", ascending: true)
        let latestUpdate = GetLatestUpdate(moc)
        if(latestUpdate != nil)
        {
            let datePredicate = NSPredicate(format: "lastUpdated = %@", latestUpdate!)
            fetchRequest.predicate = datePredicate
        }
        fetchRequest.sortDescriptors = [primarySortDescriptor]
        return fetchRequest
    }
    
    static func GetLatestUpdatesList(moc: NSManagedObjectContext) -> [CurrencyRate]
    {
        var currencyRates: [CurrencyRate] = []
        do
        {
            let fetchRequest = NSFetchRequest(entityName: "CurrencyRate")
            let primarySortDescriptor = NSSortDescriptor(key: "sort", ascending: true)
            let latestUpdate = GetLatestUpdate(moc)
            if(latestUpdate != nil)
            {
                let datePredicate = NSPredicate(format: "lastUpdated = %@", latestUpdate!)
                fetchRequest.predicate = datePredicate
            }
            fetchRequest.sortDescriptors = [primarySortDescriptor]
            let fetchedEntities = try moc.executeFetchRequest(fetchRequest) as! [CurrencyRate]
            currencyRates = fetchedEntities
        }
        catch
        {
            
        }
        return currencyRates
    }
    
    static func GetLatestUpdate(moc: NSManagedObjectContext) -> NSDate?
    {
        var latestUpdate: NSDate?
        do
        {
            let fetchRequest = NSFetchRequest(entityName: "CurrencyRate")
            let primarySortDescriptor = NSSortDescriptor(key: "lastUpdated", ascending: false)
            fetchRequest.fetchLimit = 1
            fetchRequest.sortDescriptors = [primarySortDescriptor]
            let fetchedEntities = try moc.executeFetchRequest(fetchRequest) as! [CurrencyRate]
            if(fetchedEntities.count > 0)
            {
                latestUpdate = fetchedEntities[0].lastUpdated;
            }
        }
        catch
        {
            
        }
        return latestUpdate
    }
    
    func GetLatestWeek(moc: NSManagedObjectContext) -> [CurrencyRate]
    {
        let calendar = NSCalendar.currentCalendar()
        let date: NSDate = calendar.dateByAddingUnit(.Day, value: -7, toDate: NSDate(), options: [])!
        return self.GetLatest(date, moc: moc)
    }
    
    func GetLatestMonth(moc: NSManagedObjectContext) -> [CurrencyRate]
    {
        let calendar = NSCalendar.currentCalendar()
        let date: NSDate = calendar.dateByAddingUnit(.Day, value: -25, toDate: NSDate(), options: [])!
        return self.GetLatest(date, moc: moc)
    }
    
    func GetLatest(dateFrom: NSDate, moc: NSManagedObjectContext) -> [CurrencyRate]
    {
        var currencyRates: [CurrencyRate] = []
        do
        {
            let fetchRequest = NSFetchRequest(entityName: "CurrencyRate")
            let primarySortDescriptor = NSSortDescriptor(key: "lastUpdated", ascending: true)
            let idPredicate = NSPredicate(format: "currencyId = %i", self.currencyId)
            let datePredicate = NSPredicate(format: "lastUpdated >= %@", dateFrom)
            let predicate = NSCompoundPredicate(type: NSCompoundPredicateType.AndPredicateType, subpredicates: [idPredicate, datePredicate])
            //fetchRequest.fetchLimit = 1
            fetchRequest.predicate = predicate
            fetchRequest.sortDescriptors = [primarySortDescriptor]
            currencyRates = try moc.executeFetchRequest(fetchRequest) as! [CurrencyRate]
        }
        catch
        {
            print("Error")
        }
        return currencyRates
    }
}
