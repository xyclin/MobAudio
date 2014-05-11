//
//  SNJoinViewController.h
//  MobAudio
//
//  Created by Shahmeer Navid on 5/11/14.
//  Copyright (c) 2014 Shahmeer Navid. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "SocketIO.h"

@interface SNJoinViewController : UIViewController <SocketIODelegate>

@property NSDictionary *mob;
@property IBOutlet UILabel *name;

-(IBAction)cancel:(id)sender;


@end
