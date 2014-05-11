//
//  SNAddController.h
//  MobAudio
//
//  Created by Shahmeer Navid on 5/10/14.
//  Copyright (c) 2014 Shahmeer Navid. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <CoreLocation/CoreLocation.h>

@interface SNAddController : UITableViewController


@property NSString* vid;
@property IBOutlet UITextField *name;

-(IBAction)submit:(id)sender;


@end
