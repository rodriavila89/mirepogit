//
//  CurrencyRate+CoreDataProperties.swift
//  dolarya
//
//  Created by Ramiro Rinaldi on 10/17/15.
//  Copyright © 2015 El Cronista. All rights reserved.
//
//  Choose "Create NSManagedObject Subclass…" from the Core Data editor menu
//  to delete and recreate this implementation file for your updated model.
//

import Foundation
import CoreData

extension CurrencyRate {

    @NSManaged var id: Int32
    @NSManaged var currencyId: Int32
    @NSManaged var name: String?
    @NSManaged var buy: Float
    @NSManaged var sell: Float
    @NSManaged var percentageChange: Float
    @NSManaged var lastUpdated: NSDate
    @NSManaged var sort: Int32

}
