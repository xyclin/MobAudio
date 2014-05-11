//
//  SNMainViewController.m
//  MobAudio
//
//  Created by Shahmeer Navid on 5/10/14.
//  Copyright (c) 2014 Shahmeer Navid. All rights reserved.
//

#import "SNMainViewController.h"
#import "SNMobCell.h"
#import "SocketIOPacket.h"
#import "SNJoinViewController.h"
#import "SVProgressHUD.h"



@implementation SNMainViewController

NSArray *mobs;
AFHTTPRequestOperationManager *manager;
CLLocationManager* locationManager;
NSInteger selectedMob;
UIRefreshControl* refreshControl;

- (id)initWithStyle:(UITableViewStyle)style{
    self = [super initWithStyle:style];
    if (self) {
       
    }
    return self;
}


- (void)viewDidLoad
{
    [super viewDidLoad];
    manager = [AFHTTPRequestOperationManager manager];
    [self updateMobs:true];
    
    refreshControl = [[UIRefreshControl alloc]init];
    [self.mobsList addSubview:refreshControl];
    [refreshControl addTarget:self action:@selector(updateWithoutHUD) forControlEvents:UIControlEventValueChanged];

}

- (void)didReceiveMemoryWarning{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#pragma mark - REST

-(void) updateWithoutHUD{
    [self updateMobs:false];
}

-(void) updateMobs:(BOOL) showHUD{
    
    if(showHUD) [SVProgressHUD showWithStatus:@"Loading Nearby Mobs"];
    
    //get location
    locationManager = [[CLLocationManager alloc] init];
    locationManager.delegate = self;
    locationManager.desiredAccuracy = kCLLocationAccuracyBest;
    locationManager.distanceFilter = kCLDistanceFilterNone;
    [locationManager startUpdatingLocation];
    [locationManager stopUpdatingLocation];
    CLLocation *location = [locationManager location];
    
    NSDictionary *query = [NSDictionary dictionaryWithObjectsAndKeys:[NSNumber numberWithDouble:location.coordinate.latitude] , @"lat", [NSNumber numberWithDouble:location.coordinate.longitude], @"lon", [NSNumber numberWithDouble:5000], @"radius", nil];
    
    [manager POST:@"http://192.241.208.189:54321/list" parameters:query success:^(AFHTTPRequestOperation *operation, id responseObject) {
        mobs =  [((NSDictionary*)responseObject) objectForKey:@"mobs"];
        NSLog(@"%@", mobs);
        if(showHUD) [SVProgressHUD dismiss];
        else [refreshControl endRefreshing];
        [[self mobsList] reloadData];
        
        
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [SVProgressHUD dismiss];
        NSLog(@"error %@", error);
        [[[UIAlertView alloc] initWithTitle:@"Error" message:@"Check Internet Connection" delegate:self cancelButtonTitle:@"Cancel" otherButtonTitles:@"Retry", nil] show];
    }];
}

#pragma mark - Table view data source

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    // Return the number of sections.
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    // Return the number of rows in the section.
    return [mobs count];
}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    SNMobCell *cell = [tableView dequeueReusableCellWithIdentifier:@"MainCells" forIndexPath:indexPath];
    
    // Configure the cell...
    cell.title.text = [((NSDictionary*)[mobs objectAtIndex:indexPath.row]) objectForKey:@"name"];
    cell.date.text = [((NSDictionary*)[mobs objectAtIndex:indexPath.row]) objectForKey:@"time"];
    cell.location.text = @"coming soon";
    
    
    return cell;
}


#pragma mark - table View Delegate


- (NSIndexPath*)tableView:(UITableView *)tableView willSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    selectedMob = indexPath.row;
    return indexPath;
}

- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender{
    if([segue.identifier isEqualToString:@"toJoinRoom"]){
        SNJoinViewController *controller = (SNJoinViewController*)[((UINavigationController *)segue.destinationViewController) topViewController];
        controller.mob = [mobs objectAtIndex:selectedMob];
    }
}

#pragma mark- ui alert delegate

- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex{
    if(buttonIndex == 1){
        NSLog(@"updating");
        [self updateMobs:true];
    }
}

@end
