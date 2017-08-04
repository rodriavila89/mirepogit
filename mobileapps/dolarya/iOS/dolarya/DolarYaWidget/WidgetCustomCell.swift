//
//  WidgetCustomCell.swift
//  dolarya
//
//  Created by Ramiro Rinaldi on 11/17/15.
//  Copyright © 2015 El Cronista. All rights reserved.
//

import UIKit

class WidgetCustomCell: UITableViewCell {

    @IBOutlet weak var Name: UILabel!
    @IBOutlet weak var Rate: UILabel!
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
