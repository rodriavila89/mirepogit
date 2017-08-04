//
//  AfipEstimationController.swift
//  dolarya
//
//  Created by Ramiro Rinaldi on 10/20/15.
//  Copyright © 2015 El Cronista. All rights reserved.
//

import UIKit
import Google

class AfipEstimationController: UIViewController {
    let managedObjectContext = (UIApplication.sharedApplication().delegate as! AppDelegate).managedObjectContext
    var menuTransitionManager = MenuTransitionManager()
    var currencyRates = [CurrencyRate]()
    let minimunPesos = 11176.0
    let maximunDollars = 2000.0

    @IBOutlet weak var _amountToChange: UITextField!
    @IBOutlet weak var _pesos: UILabel!
    @IBOutlet weak var _dolares: UILabel!
    @IBOutlet weak var _percepcion: UILabel!
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
        let toolbar = Common.GetToolBar("doneWithNumberPad", target: self)
        _amountToChange.inputAccessoryView = toolbar
        
        currencyRates = CurrencyRate.GetLatestUpdatesList(managedObjectContext)
    }
    
    func doneWithNumberPad()
    {
        _amountToChange.resignFirstResponder()
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    override func viewWillAppear(animated: Bool) {
        self.navigationItem.title = ""
        
        let tracker = GAI.sharedInstance().defaultTracker
        tracker.set(kGAIScreenName, value: "EstimacionAFIP")
        
        let builder = GAIDictionaryBuilder.createScreenView()
        tracker.send(builder.build() as [NSObject : AnyObject])
    }
    
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        let configurationController = segue.destinationViewController as! NavigationController
        configurationController.transitioningDelegate = self.menuTransitionManager
    }

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */
    @IBAction func onEditingEnd(sender: UITextField)
    {
        let rate = currencyRates.filter() { Int(($0 as CurrencyRate).currencyId) == 1 }[0]
        var pesos: Double = 0
        var dolares: Double = 0
        var percepcion: Double = 0
        let amountToChange = Double(sender.text!)
        if(amountToChange >= self.minimunPesos)
        {
            pesos = amountToChange! * 0.2
            percepcion = pesos * 0.2
            dolares = pesos / Double(rate.sell)
            if(dolares > self.maximunDollars)
            {
                dolares = self.maximunDollars
                pesos = dolares * Double(rate.buy)
                percepcion = pesos * 0.2
            }
        }
        else
        {
            let alert = UIAlertView()
            alert.title = "Minimo no alcanzado"
            alert.message = "Su nivel de ingresos no alcanza al mínimo exigido"
            alert.addButtonWithTitle("Ok")
            alert.show()
        }
        _pesos.text = String(format: "%.0f", pesos)
        _dolares.text = String(format: "%.0f", dolares)
        _percepcion.text = String(format: "%.0f", percepcion)
    }

}
