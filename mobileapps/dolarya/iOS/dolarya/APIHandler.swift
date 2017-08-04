//
//  APIHandler.swift
//  dolarya
//
//  Created by Ramiro Rinaldi on 10/16/15.
//  Copyright © 2015 El Cronista. All rights reserved.
//

import UIKit

class APIHandler: NSObject
{
    func UpdateCurrencyAsync(moc: NSManagedObjectContext, tableView: UITableView, fetchController: NSFetchedResultsController)
    {
        MobileServiceHelper.GetInstance().GetClient().invokeAPI("CurrencyRates", body: nil, HTTPMethod: "Get", parameters: nil, headers: nil)
            {
                myobject, response, error in
                if(myobject != nil)
                {
                    let json = JSON(myobject)
                    self.SaveList(moc, json: json)
                    do
                    {
                        try fetchController.performFetch()
                        tableView.reloadData()
                    }
                    catch
                    {
                        print("An error occurred")
                    }
                }
        }
    }
    
    func SaveList(moc: NSManagedObjectContext, json: JSON)
    {
        let jsonList = json.asArray
        if(jsonList != nil)
        {
            for jsonElement in jsonList!
            {
                CurrencyRate.Save(moc, json: jsonElement)
            }
            
            do
            {
                try moc.save()
            }
            catch
            {
                print("An error occurred")
            }
        }
    }

    func UpdateWeekAsync(moc: NSManagedObjectContext, mainViewController: MainViewController)
    {
        let calendar = NSCalendar.currentCalendar()
        let date = calendar.dateByAddingUnit(.Day, value: -7, toDate: NSDate(), options: [])
        let components = calendar.components([NSCalendarUnit.Day, NSCalendarUnit.Month, NSCalendarUnit.Year], fromDate: date!)
        
        let year =  components.year
        let month = components.month
        let day = components.day
        
        let params: NSDictionary = ["id":"","fromDate":String(year) + "-" + String(month) + "-" + String(day)]
        MobileServiceHelper.GetInstance().GetClient().invokeAPI("CurrencyRates", body: nil, HTTPMethod: "Get", parameters: params as [NSObject : AnyObject], headers: nil)
        {
            myobject, response, error in
            if(myobject != nil)
            {
                let json: JSON = JSON(myobject)
                self.SaveList(moc, json: json)
                do
                {
                    mainViewController._frc = nil
                    try mainViewController.fetchedResultsController.performFetch()
                    mainViewController.tableView.reloadData()
                }
                catch
                {
                    print("An error occurred")
                }
                mainViewController.UpdateLastUpdateLabel()
            }
        }
    }
    
    func UpdateMonthAsync(moc: NSManagedObjectContext, currencyId: Int32, mainViewController: MainViewController, onCompletition: MSAPIBlock)
    {
        let calendar = NSCalendar.currentCalendar()
        let date = calendar.dateByAddingUnit(.Month, value: -1, toDate: NSDate(), options: [])
        let components = calendar.components([NSCalendarUnit.Day, NSCalendarUnit.Month, NSCalendarUnit.Year], fromDate: date!)
        
        let year =  components.year
        let month = components.month
        let day = components.day
        
        let params: NSDictionary = ["id":String(currencyId),"fromDate":String(year) + "-" + String(month) + "-" + String(day)]
        MobileServiceHelper.GetInstance().GetClient().invokeAPI("CurrencyRates", body: nil, HTTPMethod: "Get", parameters: params as [NSObject : AnyObject], headers: nil)
        {
            myobject, response, error in
            if(myobject != nil)
            {
                let json = JSON(myobject)
                self.SaveList(moc, json: json)
            }
            onCompletition(myobject, response, error)
        }
    }
}
