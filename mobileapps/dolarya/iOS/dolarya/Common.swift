//
//  Common.swift
//  dolarya
//
//  Created by Ramiro Rinaldi on 11/11/15.
//  Copyright © 2015 El Cronista. All rights reserved.
//

import UIKit

class Common: NSObject {
    static func GetToolBar(callBack: Selector, target: UIViewController) -> UIToolbar
    {
        let toolbar = UIToolbar(frame: CGRectMake(0, 0, 320, 50))
        toolbar.barStyle = UIBarStyle.BlackTranslucent
        let doneButton = UIBarButtonItem(title: "Listo", style: UIBarButtonItemStyle.Done, target: target, action: callBack)
        doneButton.tintColor = UIColor.whiteColor()
        toolbar.items = [UIBarButtonItem(barButtonSystemItem: UIBarButtonSystemItem.FlexibleSpace, target: nil, action: nil), doneButton]
        return toolbar
    }
}
