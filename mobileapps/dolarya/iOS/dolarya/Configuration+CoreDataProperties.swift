//
//  Configuration+CoreDataProperties.swift
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

extension Configuration {

    @NSManaged var recieveOpenClose: Bool
    @NSManaged var recieveHourly: Bool

}
