//
//  ConfigurationController.swift
//  dolarya
//
//  Created by Ramiro Rinaldi on 10/20/15.
//  Copyright © 2015 El Cronista. All rights reserved.
//
import UIKit

class ConfigurationController: UIViewController {
    let managedObjectContext = (UIApplication.sharedApplication().delegate as! AppDelegate).managedObjectContext
    var configuration: Configuration?
    @IBOutlet weak var OpenCloseBtn: UIImageView!
    @IBOutlet weak var HourlyBtn: UIImageView!
    override func viewDidLoad()
    {
        super.viewDidLoad()
        configuration = Configuration.Get(managedObjectContext)!
        OpenCloseBtn.highlighted = configuration!.recieveOpenClose
        HourlyBtn.highlighted = configuration!.recieveHourly
    }
    
    override func viewWillAppear(animated: Bool)
    {
        self.navigationItem.title = ""
    }
    
    override func didReceiveMemoryWarning()
    {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @IBAction func close(sender: UIBarButtonItem)
    {
        self.dismissModal()
    }
    
    @IBAction func onSwipeUp(sender: AnyObject)
    {
        self.dismissModal()
    }
    
    func dismissModal()
    {
        self.dismissViewControllerAnimated(true, completion: nil)
    }
    
    @IBAction func OpenCloseBtnTapped(sender: AnyObject)
    {
        OpenCloseBtn.highlighted = !OpenCloseBtn.highlighted
        configuration!.recieveOpenClose = OpenCloseBtn.highlighted
        Configuration.Save(managedObjectContext, configuration: configuration!)
        suscribe(configuration!)
        
    }
    
    @IBAction func HourlyTapped(sender: AnyObject)
    {
        HourlyBtn.highlighted = !HourlyBtn.highlighted
        configuration!.recieveHourly = HourlyBtn.highlighted
        Configuration.Save(managedObjectContext, configuration: configuration!)
        suscribe(configuration!)
    }
    
    func suscribe(configuration: Configuration)
    {
        var categories = [String]()
        if configuration.recieveOpenClose {
            categories.append("OpenAndClose")
        }
        
        if configuration.recieveHourly {
            categories.append("Hourly")
        }
        
        MobileServiceHelper.GetInstance().GetClient().push.registerNativeWithDeviceToken(MobileServiceHelper.GetInstance().deviceToken, tags: categories, completion: {(error) -> Void in
            if error != nil{
                MobileServiceHelper.GetInstance().GetClient().push.registerNativeWithDeviceToken(MobileServiceHelper.GetInstance().deviceToken, tags: categories, completion: {(error) -> Void in
                    if error != nil{
                        MobileServiceHelper.GetInstance().GetClient().push.registerNativeWithDeviceToken(MobileServiceHelper.GetInstance().deviceToken, tags: categories, completion: {(error) -> Void in
                            if error != nil{
                                print("Error registering for notification: \(error)")
                            }
                            
                        })
                    }
                    
                })
            }
            
        })
    }
}
