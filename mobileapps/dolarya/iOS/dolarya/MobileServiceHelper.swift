//
//  MobileServiceHelper.swift
//  dolarya
//
//  Created by Ramiro Rinaldi on 10/16/15.
//  Copyright © 2015 El Cronista. All rights reserved.
//

import UIKit

class MobileServiceHelper: NSObject {
    private static var _instance: MobileServiceHelper? = nil
    private let _client: MSClient?
    var deviceToken: NSData?
    
    private override init(){
        self._client = MSClient(applicationURLString:"https://cronista-dolarya.azure-mobile.net/", applicationKey:"DlxPsJAXKPpqwUOIxSTrHijfiVxlXb32")
    }
    
    static func GetInstance() -> MobileServiceHelper{
        if(_instance == nil)
        {
            _instance = MobileServiceHelper()
        }
        return _instance!
    }
    
    func GetClient() -> MSClient{
        return _client!
    }
    
}
