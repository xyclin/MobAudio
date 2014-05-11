//
//  SNAddController.m
//  MobAudio
//
//  Created by Shahmeer Navid on 5/10/14.
//  Copyright (c) 2014 Shahmeer Navid. All rights reserved.
//

#import "SNAddController.h"
#import "AFNetworking.h"
#import "SVProgressHUD.h"
#import "SNHostLoungController.h"


@implementation SNAddController

CLLocationManager* locationManager;
AFHTTPRequestOperationManager *manager;
NSDictionary* mob;


- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    manager = [AFHTTPRequestOperationManager manager];
    mob = [NSDictionary dictionaryWithObjectsAndKeys:self.vid, @"url", nil];
    
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (IBAction)cancel:(id)sender{
    [self dismissViewControllerAnimated:YES completion:nil];
}

- (IBAction)submit:(id)sender{
    [SVProgressHUD showWithStatus:@"Creating Mob"];
    locationManager = [[CLLocationManager alloc] init];
    //locationManager.delegate = self;
    locationManager.desiredAccuracy = kCLLocationAccuracyBest;
    locationManager.distanceFilter = kCLDistanceFilterNone;
    [locationManager startUpdatingLocation];
    [locationManager stopUpdatingLocation];
    CLLocation *location = [locationManager location];
    
    NSString *name = self.name.text;
    //NSLog(@"%@", name);
    NSString *youtubeUrl = [NSString stringWithFormat:@"http://youtube.com/watch?v=%@", self.vid];
    [manager GET:@"http://192.241.208.189:54321/youtube/dl" parameters:[NSDictionary dictionaryWithObjectsAndKeys:youtubeUrl, @"url", nil] success:^(AFHTTPRequestOperation *operation, id mp3Url) {
        
        NSString *urlForMp3 = [mp3Url objectForKey:@"upload"];
        mob = [NSDictionary dictionaryWithObjectsAndKeys:urlForMp3, @"url", name, @"name", @"lol", @"time", [NSNumber numberWithDouble:location.coordinate.latitude], @"lat", [NSNumber numberWithDouble:location.coordinate.longitude], @"lon", nil];
        
        NSLog(@"%@", mob);
       
        [manager POST:@"http://192.241.208.189:54321/create" parameters:mob  success:^(AFHTTPRequestOperation *operation, id responseObject) {

            mob = responseObject;
            
            [SVProgressHUD dismiss];
            
            [self performSegueWithIdentifier:@"toHostLounge" sender:self];
            
        } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
            [SVProgressHUD dismiss];
            NSLog(@"error %@", error);
            [[[UIAlertView alloc] initWithTitle:@"Error" message:@"Check Internet Connection" delegate:self cancelButtonTitle:@"Cancel" otherButtonTitles:@"Retry", nil] show];
        }];
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [SVProgressHUD dismiss];
        NSLog(@"error %@", error);
        [[[UIAlertView alloc] initWithTitle:@"Error" message:@"Check Internet Connection" delegate:self cancelButtonTitle:@"Cancel" otherButtonTitles:@"Retry", nil] show];
    }];
    
}

- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender{
    if([segue.identifier isEqualToString:@"toHostLounge"]){
        SNHostLoungController *controller = (SNHostLoungController*)segue.destinationViewController;
        NSLog(@"the mob %@", mob);
        controller.mob = mob;
    }
}

- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex{
    if(buttonIndex == 1){
        [self submit:nil];
    }
}

@end
