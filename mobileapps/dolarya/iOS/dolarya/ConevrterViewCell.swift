//
//  ConevrterViewCell.swift
//  dolarya
//
//  Created by Ramiro Rinaldi on 10/23/15.
//  Copyright © 2015 El Cronista. All rights reserved.
//

import UIKit

class ConevrterViewCell: UITableViewCell {

    @IBOutlet weak var CurrencyName: UILabel!
    @IBOutlet weak var CurrencyAmount: UILabel!
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
