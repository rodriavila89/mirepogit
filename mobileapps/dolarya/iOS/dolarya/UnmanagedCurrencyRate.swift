//
//  UnmanagedCurrencyRate.swift
//  dolarya
//
//  Created by Ramiro Rinaldi on 10/26/15.
//  Copyright © 2015 El Cronista. All rights reserved.
//

import UIKit

class UnmanagedCurrencyRate: NSObject {
    var currencyId: Int
    var name: String
    var buy: Float
    var sell: Float
    var currentValue: Float
    var isSelected: Bool
    
    init(currencyId: Int, name: String, buy: Float, sell: Float, isSelected: Bool)
    {
        self.currencyId = currencyId
        self.name = name
        self.buy = buy
        self.sell = sell
        self.currentValue = 0
        self.isSelected = isSelected
    }
}
