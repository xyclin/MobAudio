//
//  SNJoinViewController.m
//  MobAudio
//
//  Created by Shahmeer Navid on 5/11/14.
//  Copyright (c) 2014 Shahmeer Navid. All rights reserved.
//

#import "SNJoinViewController.h"
#import "SocketIO.h"
#import "SocketIOPacket.h"
#import "SVProgressHUD.h"
#import <AVFoundation/AVFoundation.h>

@implementation SNJoinViewController

SocketIO  *io;
AVAudioPlayer *myAudioPlayer;

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
    [SVProgressHUD showWithStatus:@"Connecting to Host"];
    io = [[SocketIO alloc] initWithDelegate:self];
    [io connectToHost:@"192.241.208.189" onPort:54321];
    myAudioPlayer = [[AVAudioPlayer alloc] initWithContentsOfURL:[NSURL URLWithString:[self.mob objectForKey:@"url"]] error:nil];
    [self setTitle:@"Waiting Room"];
    self.name.text = [self.mob objectForKey:@"name"];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void) socketIO:(SocketIO *)socket didReceiveEvent:(SocketIOPacket *)packet{
    if([packet.name isEqualToString:@"play"]){
        NSLog(@"recieved play");
        [myAudioPlayer play];
    }
}

- (void) socketIODidConnect:(SocketIO *)socket{
    [SVProgressHUD dismiss];
    [io sendEvent:@"subscribe" withData:[NSDictionary dictionaryWithObjectsAndKeys:[self.mob objectForKey:@"mobId"], @"mobId", nil]];
}


- (void)viewWillDisappear:(BOOL)animated{
    [io sendEvent:@"unsubscribe" withData:[NSDictionary dictionaryWithObjectsAndKeys:[self.mob objectForKey:@"mobId"], @"mobId", nil]];
    [io disconnect];
    [super viewWillDisappear:animated];
}

-(IBAction)cancel:(id)sender{
    [self dismissViewControllerAnimated:YES completion:nil];
}

@end
