//
//  SNYoutubeViewController.h
//  MobAudio
//
//  Created by Shahmeer Navid on 5/10/14.
//  Copyright (c) 2014 Shahmeer Navid. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface SNYoutubeViewController : UITableViewController <UITableViewDelegate, UISearchBarDelegate, UISearchDisplayDelegate, UIAlertViewDelegate>

@property IBOutlet UISearchBar* search;
@property IBOutlet UISearchDisplayController* results;
@property IBOutlet UITableView* youtubeList;


-(IBAction)cancel:(id)sender;

@end
