//
//  CurrencyRateViewCell.swift
//  dolarya
//
//  Created by Ramiro Rinaldi on 10/26/15.
//  Copyright © 2015 El Cronista. All rights reserved.
//

import UIKit
import Charts

class CurrencyRateViewCell: UITableViewCell {

    @IBOutlet weak var NameView: UILabel!
    @IBOutlet weak var BuyView: UILabel!
    @IBOutlet weak var SellView: UILabel!
    @IBOutlet weak var PercentageChangeView: UILabel!
    @IBOutlet weak var CaretVIew: UIImageView!
    @IBOutlet weak var ExpandableView: UIView!
    @IBOutlet weak var ExpandButtonView: UIImageView!
    @IBOutlet weak var ChartContainer: LineChartView!
    @IBOutlet weak var WeekBtn: UIButton!
    @IBOutlet weak var MonthBtn: UIButton!
    @IBOutlet weak var ExpandableVisibleConstraint: NSLayoutConstraint!
    @IBOutlet weak var ExpandableHiddenContraint: NSLayoutConstraint!
    @IBOutlet weak var MainView: UIView!
    @IBOutlet weak var ContentView: UIView!
    @IBOutlet weak var ShareActions: UIView!
    @IBOutlet weak var FacebookBtn: UIImageView!
    @IBOutlet weak var TwitterBtn: UIImageView!
    @IBOutlet weak var WhatsappBtn: UIImageView!
    @IBOutlet weak var ShareActionsContraint: NSLayoutConstraint!
    @IBOutlet weak var MarkerView: UIView!
    @IBOutlet weak var MarkerRates: UILabel!
    @IBOutlet weak var MarkerDate: UILabel!
    @IBOutlet weak var SharerContainer: UIView!
    @IBOutlet weak var Spinner: UIActivityIndicatorView!
    @IBOutlet weak var SpinnerView: UIView!
    override func awakeFromNib() {
        super.awakeFromNib()
    }

    override func setSelected(selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)
    }
    
    func SetupCell(currencyRate: CurrencyRate, target: UITableViewController)
    {
        NameView.text = currencyRate.name
        BuyView.text = String(format: "%.4f", currencyRate.buy)
        SellView.text = String(format: "%.4f", currencyRate.sell)
        PercentageChangeView.text = String(format: "%.4f", currencyRate.percentageChange)
        CaretVIew.image = UIImage(named: currencyRate.percentageChange >= 0 ? "caret_up" : "caret_down")!
        
        //MARK: Setup events
        let tapGestureRecognizer = UITapGestureRecognizer(target:target, action:Selector("ExpandButtonViewTapped:"))
        ExpandButtonView.addGestureRecognizer(tapGestureRecognizer)
        
        let sharerTap = UITapGestureRecognizer(target:target, action:Selector("SharerTapped:"))
        SharerContainer.addGestureRecognizer(sharerTap)
        
        let swipeRight = UISwipeGestureRecognizer(target:target, action:Selector("didSwipe:"))
        swipeRight.direction = .Right
        addGestureRecognizer(swipeRight)
        
        let swipeLeft = UISwipeGestureRecognizer(target:target, action:Selector("didSwipe:"))
        swipeLeft.direction = .Left
        addGestureRecognizer(swipeLeft)
        
        let whatsappTap = UITapGestureRecognizer(target:target, action:Selector("WhatsappTapped:"))
        WhatsappBtn.addGestureRecognizer(whatsappTap)
        
        let twitterTap = UITapGestureRecognizer(target:target, action:Selector("TwitterTapped:"))
        TwitterBtn.addGestureRecognizer(twitterTap)
        
        let facebookTap = UITapGestureRecognizer(target:target, action:Selector("FacebookTapped:"))
        FacebookBtn.addGestureRecognizer(facebookTap)
        
        let weekTap = UITapGestureRecognizer(target:target, action:Selector("WeekTapped:"))
        WeekBtn.addGestureRecognizer(weekTap)
        
        let monthTap = UITapGestureRecognizer(target:target, action:Selector("MonthTapped:"))
        MonthBtn.addGestureRecognizer(monthTap)
        
        MarkerView.hidden = true
    }
    
    func ShowChart()
    {
        ExpandableVisibleConstraint.priority = 750
        ExpandableHiddenContraint.priority = 250
        ExpandableView.hidden = false
        ExpandButtonView.image = UIImage(named: "close_button")!
    }
    
    func HideChart()
    {
        ExpandableVisibleConstraint.priority = 250
        ExpandableHiddenContraint.priority = 750
        ExpandableView.hidden = true
        MarkerView.hidden = true
        ExpandButtonView.image = UIImage(named: "expand_button")!
    }
    
    func ShowShareActions()
    {
        FacebookBtn.hidden = false
        TwitterBtn.hidden = false
        WhatsappBtn.hidden = false
        ShareActionsContraint.constant = 110
    }
    
    func HideShareActions()
    {
        FacebookBtn.hidden = true
        TwitterBtn.hidden = true
        WhatsappBtn.hidden = true
        ShareActionsContraint.constant = 15
    }
}
