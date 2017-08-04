//
//  CurrencyConverterController.swift
//  dolarya
//
//  Created by Ramiro Rinaldi on 10/20/15.
//  Copyright © 2015 El Cronista. All rights reserved.
//

import UIKit
import Google

class CurrencyConverterController: UITableViewController, UIPickerViewDataSource, UIPickerViewDelegate {
    @IBOutlet weak var _selectedOption: UITextField!
    @IBOutlet weak var _picker: UIPickerView!
    @IBOutlet weak var _amountToChange: UITextField!
    let managedObjectContext = (UIApplication.sharedApplication().delegate as! AppDelegate).managedObjectContext
    var menuTransitionManager = MenuTransitionManager()
    
    var currencyRates: [UnmanagedCurrencyRate] = []
    
    override func viewDidLoad() {
        super.viewDidLoad()
        let arsRate = UnmanagedCurrencyRate(currencyId: 0, name: "AR$", buy: 1, sell: 1, isSelected: true)
        currencyRates.append(arsRate)
        for currencyRate in CurrencyRate.GetLatestUpdatesList(managedObjectContext)
        {
            let unmanagedRate = UnmanagedCurrencyRate(currencyId: Int(currencyRate.currencyId), name: currencyRate.name!, buy: currencyRate.buy, sell: currencyRate.sell, isSelected: false)
            currencyRates.append(unmanagedRate)
        }
        // Do any additional setup after loading the view.
        _picker.removeFromSuperview()
        _picker.hidden = false
        _selectedOption.inputView = _picker
        
        let toolbar = Common.GetToolBar("doneWithNumberPad", target: self)
        _amountToChange.inputAccessoryView = toolbar
    }
    
    func doneWithNumberPad()
    {
        _amountToChange.resignFirstResponder()
        if(_amountToChange.text == nil || _amountToChange.text == "")
        {
            _amountToChange.text = "0"
        }
        UpdateValues()
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
    
    override func viewWillAppear(animated: Bool) {
        self.navigationItem.title = ""
        
        let tracker = GAI.sharedInstance().defaultTracker
        tracker.set(kGAIScreenName, value: "Conversor")
        
        let builder = GAIDictionaryBuilder.createScreenView()
        tracker.send(builder.build() as [NSObject : AnyObject])
    }
    
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        let configurationController = segue.destinationViewController as! NavigationController
        configurationController.transitioningDelegate = self.menuTransitionManager
    }
    
    // MARK: - Table view data source
    
    override func numberOfSectionsInTableView(tableView: UITableView) -> Int {
        return 1
    }
    
    override func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return currencyRates.count
    }
    
    
    override func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCellWithIdentifier("ConverterRow", forIndexPath: indexPath) as! ConevrterViewCell
        let item = currencyRates[indexPath.row]
        cell.CurrencyName.text =  item.name
        cell.CurrencyAmount.text = String(format:"%.2f", item.currentValue)
        cell.hidden = item.isSelected
        return cell
    }
    
    override func tableView(tableView: UITableView, heightForRowAtIndexPath indexPath: NSIndexPath) -> CGFloat {
        //let firstKey = Array(currenciesToConvert.keys).sort(<)[indexPath.row]
        let item = currencyRates[indexPath.row]
        return item.isSelected ? 0 : 50
    }
    
    
    func numberOfComponentsInPickerView(pickerView: UIPickerView) -> Int {
        return 1
    }
    
    
    func pickerView(pickerView: UIPickerView, numberOfRowsInComponent component: Int) -> Int {
        return currencyRates.count
    }
    
    func pickerView(pickerView: UIPickerView, titleForRow row: Int, forComponent component: Int) -> String? {
        return currencyRates[row].name
    }
    
    func pickerView(pickerView: UIPickerView, didSelectRow row: Int, inComponent component: Int) {
        let selected = currencyRates[_picker.selectedRowInComponent(0)]
        _selectedOption.text = selected.name
        _selectedOption.endEditing(true)
        UpdateValues()
    }
    
    @IBAction func PickerOpenerPressed(sender: UIButton) {
        if(!_selectedOption.editing)
        {
            _selectedOption.becomeFirstResponder()
        }
        else
        {
            let selected = currencyRates[_picker.selectedRowInComponent(0)]
            _selectedOption.text = selected.name
            _selectedOption.endEditing(true)
        }
    }
    
    func UpdateValues()
    {
        let selectedRate = currencyRates.filter() {
            ($0 as UnmanagedCurrencyRate).name == _selectedOption.text
        }
        let rate = selectedRate[0]
        let pesos = rate.buy * Float(_amountToChange.text!)!
        for value in currencyRates
        {
            value.isSelected = (value.name == _selectedOption.text)
            value.currentValue = pesos / value.buy
        }
        self.tableView.reloadData()
    }
}
