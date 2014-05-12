//
//  SNHostLoungController.m
//  MobAudio
//
//  Created by Shahmeer Navid on 5/11/14.
//  Copyright (c) 2014 Shahmeer Navid. All rights reserved.
//

#import "SNHostLoungController.h"
#import "SocketIO.h"
#import "SVProgressHUD.h"
#import <AVFoundation/AVFoundation.h>


@implementation SNHostLoungController

SocketIO  *io;
AVPlayer *myAudioPlayer;
bool playing;

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad{
    
    [super viewDidLoad];
    [SVProgressHUD show];
    io = [[SocketIO alloc] initWithDelegate:self];
    [io connectToHost:@"192.241.208.189" onPort:54321];
    NSLog(@"the url: %@", self.mob);
    NSError* error = nil;
    playing = false;
    myAudioPlayer = [AVPlayer playerWithURL:[NSURL URLWithString:[self.mob objectForKey:@"url"]]];
    
    if(error) [[[UIAlertView alloc] initWithTitle:@"Error" message:[error localizedDescription] delegate:self cancelButtonTitle:@"OK" otherButtonTitles: nil] show];
    
}

- (void) socketIO:(SocketIO *)socket didReceiveEvent:(SocketIOPacket *)packet{

}

- (void) socketIODidConnect:(SocketIO *)socket{
    [SVProgressHUD dismiss];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}


-(IBAction)cancel:(id)sender{
    [SVProgressHUD showWithStatus:@"Closing"];
    [myAudioPlayer pause];
    [io sendEvent:@"destroy" withData:self.mob];
    [SVProgressHUD dismiss];
    [io disconnect];
    [self dismissViewControllerAnimated:YES completion:nil];
    
}

-(IBAction)play:(id)sender{
    [io sendEvent:@"play" withData:self.mob];
    [myAudioPlayer play];
}

@end
