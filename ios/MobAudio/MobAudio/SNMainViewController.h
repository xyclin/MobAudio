//
//  SNMainViewController.h
//  MobAudio
//
//  Created by Shahmeer Navid on 5/10/14.
//  Copyright (c) 2014 Shahmeer Navid. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "AFNetworking.h"
#import <CoreLocation/CoreLocation.h>


@interface SNMainViewController : UITableViewController <UITableViewDataSource, UITableViewDelegate, CLLocationManagerDelegate, UIAlertViewDelegate>

@property IBOutlet UITableView* mobsList;

@end
