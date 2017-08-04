//
//  ViewController.swift
//  dolarya
//
//  Created by Ramiro Rinaldi on 10/14/15.
//  Copyright (c) 2015 El Cronista. All rights reserved.
//

import UIKit
import Charts
import Social
import Google

class MainViewController: UITableViewController, NSFetchedResultsControllerDelegate, ChartViewDelegate {
    // Retreive the managedObjectContext from AppDelegate
    let managedObjectContext = (UIApplication.sharedApplication().delegate as! AppDelegate).managedObjectContext
    var menuTransitionManager = MenuTransitionManager()
    //var configuration = Configuration()
    
    @IBOutlet weak var LastUpdateLabel: UILabel!
    @IBOutlet weak var HeaderView: UIView!
    override func viewDidLoad() {
        super.viewDidLoad()
        
        NSNotificationCenter.defaultCenter().addObserver(self, selector: "applicationDidBecomeActive:", name: UIApplicationDidBecomeActiveNotification, object: nil)
        
        APIHandler().UpdateWeekAsync(managedObjectContext, mainViewController: self)
        UpdateLastUpdateLabel()
        do {
            try fetchedResultsController.performFetch()
        } catch {
            print("An error occurred")
        }
        
    }
    
    func applicationDidBecomeActive(notification: NSNotification)
    {
        APIHandler().UpdateWeekAsync(managedObjectContext, mainViewController: self)
    }
    
    override func viewWillAppear(animated: Bool) {
        self.navigationItem.title = ""
        
        let tracker = GAI.sharedInstance().defaultTracker
        tracker.set(kGAIScreenName, value: "Cotizaciones")
        
        let builder = GAIDictionaryBuilder.createScreenView()
        tracker.send(builder.build() as [NSObject : AnyObject])
    }
    
    
    func UpdateLastUpdateLabel()
    {
        let latestUpdate = CurrencyRate.GetLatestUpdate(managedObjectContext)
        if(latestUpdate != nil)
        {
            let formatter = NSDateFormatter()
            formatter.dateFormat = "hh:mm"
            formatter.timeZone = NSTimeZone(abbreviation: "GMT")
            LastUpdateLabel.text = formatter.stringFromDate(latestUpdate!)
        }
    }
    
    override func numberOfSectionsInTableView(tableView: UITableView) -> Int {
        if let sections = fetchedResultsController.sections {
            return sections.count
        }
        
        return 0
    }
    
    override func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        if let sections = fetchedResultsController.sections {
            let currentSection = sections[section]
            return currentSection.numberOfObjects
        }
        return 0
    }
    
    override func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCellWithIdentifier("CustomRow", forIndexPath: indexPath) as! CurrencyRateViewCell
        let currencyRate = fetchedResultsController.objectAtIndexPath(indexPath) as! CurrencyRate
        cell.SetupCell(currencyRate, target:self)
        
        cell.HideChart()
        return cell
    }
    
    override func tableView(tableView: UITableView, titleForHeaderInSection section: Int) -> String? {
        if let sections = fetchedResultsController.sections {
            let currentSection = sections[section]
            return currentSection.name
        }
        
        return nil
    }
    
    override func tableView(tableView: UITableView, heightForRowAtIndexPath indexPath: NSIndexPath) -> CGFloat {
        return UITableViewAutomaticDimension
    }
    
    override func tableView(tableView: UITableView, estimatedHeightForRowAtIndexPath indexPath: NSIndexPath) -> CGFloat {
        return 310
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        let configurationController = segue.destinationViewController as! NavigationController
        configurationController.transitioningDelegate = self.menuTransitionManager
    }
    
    func ConfigureChart(currencyRate: CurrencyRate, isWeek: Bool, cell: CurrencyRateViewCell) -> Bool
    {
        let data = GetData(currencyRate, isWeek: isWeek)
        SetupChart(cell.ChartContainer, data: data)
        if(!isWeek && data.xValCount < 15)
        {
            return true
        }
        return false
    }
    
    func GetData(currencyRate: CurrencyRate, isWeek: Bool) -> LineChartData
    {
        let rates: [CurrencyRate]
        if(isWeek)
        {
            rates = currencyRate.GetLatestWeek(managedObjectContext)
        }
        else
        {
            rates = currencyRate.GetLatestMonth(managedObjectContext)
        }
        var xVals = [String]()
        var entryList = [ChartDataEntry]()
        let formatter = NSDateFormatter()
        formatter.dateStyle = NSDateFormatterStyle.ShortStyle
        formatter.timeStyle = .NoStyle
        formatter.timeZone = NSTimeZone(abbreviation: "UTC")
        var index = 0
        for rate in rates
        {
            entryList.append(ChartDataEntry(value: Double(rate.buy), xIndex: index, data: rate))
            xVals.append(formatter.stringFromDate(rate.lastUpdated))
            index++
        }
        let lineDataSet = LineChartDataSet(yVals: entryList, label: currencyRate.name)
        
        lineDataSet.lineWidth = 2
        lineDataSet.circleRadius = 6
        lineDataSet.colors = [UIColor.whiteColor()]
        lineDataSet.circleColors = [UIColor(red: 0.21, green: 0.42, blue: 0.46, alpha: 1)]
        lineDataSet.circleHoleColor = UIColor(red: 0.21, green: 0.42, blue: 0.46, alpha: 1)
        lineDataSet.highlightColor = UIColor.whiteColor()
        lineDataSet.drawValuesEnabled = false
        let data = LineChartData(xVals: xVals, dataSet: lineDataSet)
        return data
    }
    
    func SetupChart(chart: LineChartView, data: LineChartData)
    {
        chart.noDataText = "You need to provide data for the chart."
        chart.descriptionText = ""
        chart.drawGridBackgroundEnabled = false
        chart.dragEnabled = true
        chart.scaleXEnabled = true
        chart.scaleYEnabled = true
        chart.pinchZoomEnabled = true
        chart.backgroundColor = UIColor(red: 0.80, green: 0.89, blue: 0.91, alpha: 1)
        //chart.viewPortHandler.restrainViewPort(offsetLeft: 10, offsetTop: 0, offsetRight: 10, offsetBottom: 0)
        chart.setViewPortOffsets(left: 6, top: 0, right: 6, bottom: 0)
        chart.data = data
        chart.legend.enabled = false
        chart.leftAxis.enabled = true
        //chart.leftAxis.labelCount = 0
        //chart.leftAxis.resetCustomAxisMin()
        //chart.leftAxis.resetCustomAxisMax()
        chart.leftAxis.startAtZeroEnabled = false
        //chart.leftAxis.spaceTop = 30
        chart.leftAxis.drawAxisLineEnabled = false
        chart.rightAxis.enabled = false
        chart.xAxis.enabled = false
        chart.xAxis.avoidFirstLastClippingEnabled = true
        chart.fitScreen()
        chart.delegate = self
        
        //chart.invalidateIntrinsicContentSize()
        chart.animate(xAxisDuration: 1)
        
    }
    
    func ExpandButtonViewTapped(sender: UITapGestureRecognizer)
    {
        self.tableView.beginUpdates()
        let cell = GetCellFromGesture(sender)
        let currencyRate = GetCurrencyRateFromGesture(sender)
        if(cell.ExpandableView.hidden)
        {
            ConfigureChart(currencyRate, isWeek: true, cell: cell)
            cell.ShowChart()
        }
        else
        {
            cell.HideChart()
        }
        self.tableView.endUpdates()
    }
    
    func didSwipe(sender: UISwipeGestureRecognizer)
    {
        let cell = GetCellFromGesture(sender)
        if(sender.direction == UISwipeGestureRecognizerDirection.Right)
        {
            cell.ShowShareActions()
        }
        else if(sender.direction == UISwipeGestureRecognizerDirection.Left)
        {
            cell.HideShareActions()
        }
        
    }
    
    func SharerTapped(sender: UITapGestureRecognizer)
    {
        let cell = GetCellFromGesture(sender)
        if(cell.ShareActionsContraint.constant == 110)
        {
            cell.HideShareActions()
        }
        else
        {
            cell.ShowShareActions()
        }
    }
    
    func GetCellFromGesture(gesture: UIGestureRecognizer) -> CurrencyRateViewCell
    {
        let tapLocation = gesture.locationInView(self.tableView)
        let indexPath = self.tableView.indexPathForRowAtPoint(tapLocation)
        let cell = self.tableView.cellForRowAtIndexPath(indexPath!) as! CurrencyRateViewCell
        return cell
    }
    
    func GetCurrencyRateFromGesture(gesture: UIGestureRecognizer) -> CurrencyRate
    {
        let tapLocation = gesture.locationInView(self.tableView)
        let indexPath = self.tableView.indexPathForRowAtPoint(tapLocation)
        let currencyRate = fetchedResultsController.objectAtIndexPath(indexPath!) as! CurrencyRate
        return currencyRate
    }
    
    func WhatsappTapped(sender: UITapGestureRecognizer)
    {
        let rate = GetCurrencyRateFromGesture(sender)
        let textToShare = GetShareText(rate)
        let encodedTextToShare = textToShare.stringByAddingPercentEncodingWithAllowedCharacters(.URLHostAllowedCharacterSet())!
        let whatsappURL = NSURL(string: "whatsapp://send?text=" + encodedTextToShare)!
        if(UIApplication.sharedApplication().canOpenURL(whatsappURL))
        {
            UIApplication.sharedApplication().openURL(whatsappURL)
        }
    }
    
    func TwitterTapped(sender: UITapGestureRecognizer)
    {
        let rate = GetCurrencyRateFromGesture(sender)
        let textToShare = GetShareText(rate)
        if SLComposeViewController.isAvailableForServiceType(SLServiceTypeTwitter)
        {
            let tweetSheet = SLComposeViewController(forServiceType: SLServiceTypeTwitter)
            tweetSheet.setInitialText(textToShare)
            tweetSheet.addURL(NSURL(string: "http://bit.ly/iAppDolar"))
            self.presentViewController(tweetSheet, animated: true, completion: nil)
        }
        else
        {
            print("error")
        }
    }
    
    func FacebookTapped(sender: UITapGestureRecognizer)
    {
        let rate = GetCurrencyRateFromGesture(sender)
        let textToShare = GetShareText(rate)
        if SLComposeViewController.isAvailableForServiceType(SLServiceTypeFacebook)
        {
            let fbSheet = SLComposeViewController(forServiceType: SLServiceTypeFacebook)
            fbSheet.setInitialText(textToShare)
            fbSheet.addURL(NSURL(string: "http://bit.ly/iAppDolar"))
            self.presentViewController(fbSheet, animated: true, completion: nil)
        }
        else
        {
            print("error")
        }
    }
    
    func WeekTapped(sender: UITapGestureRecognizer)
    {
        self.tableView.beginUpdates()
        let cell = GetCellFromGesture(sender)
        let currencyRate = GetCurrencyRateFromGesture(sender)
        ConfigureChart(currencyRate, isWeek: true, cell: cell)
        cell.MarkerView.hidden = true
        self.tableView.endUpdates()
    }
    
    func MonthTapped(sender: UITapGestureRecognizer)
    {
        let cell = GetCellFromGesture(sender)
        let currencyRate = GetCurrencyRateFromGesture(sender)
        cell.MarkerView.hidden = true
        if(self.ConfigureChart(currencyRate, isWeek: false, cell: cell))
        {
            cell.SpinnerView.hidden = false
            cell.Spinner.hidden = false
            
            cell.Spinner.startAnimating()
            APIHandler().UpdateMonthAsync(managedObjectContext, currencyId: currencyRate.currencyId, mainViewController: self)
            {
                myobject, response, error in
                self.ConfigureChart(currencyRate, isWeek: false, cell: cell)
                cell.MarkerView.hidden = true
                cell.SpinnerView.hidden = true
            }
        }
    }
    
    func GetShareText(rate: CurrencyRate) -> String
    {
        var textToShare = rate.name! + ": $" + String(rate.buy)
        textToShare += "/$" + String(rate.sell) + ". "
        textToShare += "Descargá la app DólarYa de Cronista.com ingresando en: http://bit.ly/iAppDolar"
        return textToShare
    }
    
    func chartValueSelected(chartView: ChartViewBase, entry: ChartDataEntry, dataSetIndex: Int, highlight: ChartHighlight)
    {
        let cell = GetCellFromChartView(chartView)
        if(cell != nil)
        {
            let markerPosition = chartView.getMarkerPosition(entry: entry,  highlight: highlight)
            let rate = entry.data as! CurrencyRate
            
            let formatter = NSDateFormatter()
            formatter.dateFormat = "dd/MM/yyyy"
            formatter.timeZone = NSTimeZone(abbreviation: "UTC")
            
            // Adding top marker
            cell!.MarkerRates.text = String(format: "$%.2f", rate.buy) + "/" + String(format: "$%.2f", rate.sell)
            cell!.MarkerDate.text = formatter.stringFromDate(rate.lastUpdated)
            cell!.MarkerView.center = CGPointMake(markerPosition.x + 39, markerPosition.y + 50)
            cell!.MarkerView.hidden = false
        }
        
    }
    func chartValueNothingSelected(chartView: ChartViewBase)
    {
        let cell = GetCellFromChartView(chartView)
        cell?.MarkerView.hidden = true
    }
    
    func chartScaled(chartView: ChartViewBase, scaleX: CGFloat, scaleY: CGFloat)
    {
        let cell = GetCellFromChartView(chartView)
        cell?.MarkerView.hidden = true
    }
    
    func chartTranslated(chartView: ChartViewBase, dX: CGFloat, dY: CGFloat)
    {
        let cell = GetCellFromChartView(chartView)
        cell?.MarkerView.hidden = true
    }
    
    func GetCellFromChartView(chartView: ChartViewBase) -> CurrencyRateViewCell?
    {
        var cell: CurrencyRateViewCell? = nil
        var currentView: UIView? = chartView
        while(currentView != nil)
        {
            if(currentView!.isMemberOfClass(CurrencyRateViewCell))
            {
                cell = currentView as? CurrencyRateViewCell
                break
            }
            currentView = currentView?.superview
        }
        if(cell != nil)
        {
            cell!.MarkerView.hidden = true
        }
        return cell
    }
    
    var _frc: NSFetchedResultsController?
    var fetchedResultsController: NSFetchedResultsController {
        get {
            if _frc != nil {
                return _frc!
            }
            let fetchRequest = CurrencyRate.GetLatestUpdates(self.managedObjectContext)
            _frc = NSFetchedResultsController(fetchRequest: fetchRequest, managedObjectContext: self.managedObjectContext, sectionNameKeyPath: nil, cacheName: nil)
            _frc!.delegate = self
            return _frc!
        }
    }
}