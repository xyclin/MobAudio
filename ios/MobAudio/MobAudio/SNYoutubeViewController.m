//
//  SNYoutubeViewController.m
//  MobAudio
//
//  Created by Shahmeer Navid on 5/10/14.
//  Copyright (c) 2014 Shahmeer Navid. All rights reserved.
//

#import "SNYoutubeViewController.h"
#import "SVProgressHUD.h"
#import "AFNetworking.h"
#import "SNYoutubeCell.h"
#import "SNAddController.h"

@implementation SNYoutubeViewController

NSArray* youtube;
NSMutableArray* thumbs;
AFHTTPRequestOperationManager *manager;
NSString *nextPage;
BOOL loading;
NSInteger selectedVid;
NSString* query;

- (id)initWithStyle:(UITableViewStyle)style
{
    self = [super initWithStyle:style];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad{
    [super viewDidLoad];
    nextPage = @"";
    loading = false;
    query = @"";
    manager = [AFHTTPRequestOperationManager manager];
    thumbs = [[NSMutableArray alloc] init];
    
    [self updateVids];
    // Uncomment the following line to preserve selection between presentations.
    // self.clearsSelectionOnViewWillAppear = NO;
    
    // Uncomment the following line to display an Edit button in the navigation bar for this view controller.
    // self.navigationItem.rightBarButtonItem = self.editButtonItem;
}

- (void)didReceiveMemoryWarning{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#pragma mark - Table view data source

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView{
    // Return the number of sections.
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    // Return the number of rows in the section.
    return [youtube count];
}

-(IBAction)cancel:(id)sender{
    [self dismissViewControllerAnimated:YES completion:nil];
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    SNYoutubeCell *cell = [tableView dequeueReusableCellWithIdentifier:@"YoutubeCells" forIndexPath:indexPath];
    
    // Configure the cell...
    NSDictionary* snippet = [((NSDictionary*)[youtube objectAtIndex:indexPath.row]) objectForKey:@"snippet"];
    cell.name.text = [snippet objectForKey:@"title"];
    cell.description.text = [snippet objectForKey:@"description"];
    
    cell.thumbnail.image = [thumbs objectAtIndex:indexPath.row];
    
    return cell;
}


-(void)loadMore{
    loading = true;
    [SVProgressHUD showWithStatus:@"Loading Videos"];
    
    NSString* parameters = [NSString stringWithFormat:@"http://192.241.208.189:54321/youtube/list?part=snippet&order=rating&type=video&videoDefinition=standard&videoEmbeddable=true&q=%@&pageToken=%@&maxResults=10", query, nextPage];
    
    [manager GET:parameters parameters:nil success:^(AFHTTPRequestOperation *operation, id responseObject) {
        youtube =  [youtube arrayByAddingObjectsFromArray:[((NSDictionary*)responseObject) objectForKey:@"items"]];
        [self createThumbnailHolder];
        nextPage = [((NSDictionary*)responseObject) objectForKey:@"nextPageToken"];
        [SVProgressHUD dismiss];
        [[self youtubeList] reloadData];
        loading = false;
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [SVProgressHUD dismiss];
        loading = false;
        NSLog(@"error %@", error);
        [[[UIAlertView alloc] initWithTitle:@"Error" message:@"Check Internet Connection" delegate:self cancelButtonTitle:@"Cancel" otherButtonTitles:@"Retry", nil] show];
    }];
    
}

-(void) createThumbnailHolder{
    thumbs = nil;
    thumbs = [[NSMutableArray alloc] init];
    for(int i = 0 ; i < [youtube count]; i++){
        NSDictionary* snippet = [((NSDictionary*)[youtube objectAtIndex:i]) objectForKey:@"snippet"];
        NSString * imageUrl = [[[snippet objectForKey:@"thumbnails"] objectForKey:@"default"] objectForKey:@"url"];
        [thumbs addObject: [UIImage imageWithData:[NSData dataWithContentsOfURL:[NSURL URLWithString:imageUrl]]]];
    }
}

-(void) updateVids{
    
    [SVProgressHUD showWithStatus:@"Loading Videos"];
    
    
    NSString* parameters = [NSString stringWithFormat:@"http://192.241.208.189:54321/youtube/list?part=snippet&order=rating&type=video&videoDefinition=standard&videoEmbeddable=true&q=%@&pageToken=%@&maxResults=10", query, nextPage];
    
    [manager GET:parameters parameters:nil success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        youtube =  [((NSDictionary*)responseObject) objectForKey:@"items"];
        
        [self createThumbnailHolder];
        
        
        [SVProgressHUD dismiss];
        [[self youtubeList] reloadData];
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [SVProgressHUD dismiss];
        loading = false;
        NSLog(@"error %@", error);
        [[[UIAlertView alloc] initWithTitle:@"Error" message:@"Check Internet Connection" delegate:self cancelButtonTitle:@"Cancel" otherButtonTitles:@"Retry", nil] show];
    }];
}

- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex{
    if(buttonIndex == 1){
        NSLog(@"updating");
        [self updateVids];
    }
}

- (void)viewWillDisappear:(BOOL)animated{
    youtube = nil;
    nextPage = @"";
    [super viewWillDisappear:animated];
}

- (NSIndexPath*)tableView:(UITableView *)tableView willSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    selectedVid = indexPath.row;
    return indexPath;
}

- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender{
    if([segue.identifier isEqualToString:@"toAdd"]){
        SNAddController *controller = (SNAddController*)segue.destinationViewController;
        controller.vid = [[[youtube objectAtIndex:selectedVid] objectForKey:@"id"] objectForKey:@"videoId"];
        NSLog(@"%@", controller.vid);
    }
}

-(void) scrollViewDidScroll:(UIScrollView *)scrollView {
    
    if (self.tableView.contentOffset.y >= (self.tableView.contentSize.height - self.tableView.bounds.size.height)){
        
        if(!loading) [self loadMore];
    }
    
}

- (void)searchBarCancelButtonClicked:(UISearchBar *)searchBar{
    [self.view endEditing:YES];
}

- (void)searchBarSearchButtonClicked:(UISearchBar *)searchBar{
    [self updateVids];
    [searchBar setShowsCancelButton:false animated:true];
    [self.view endEditing:YES];
}
- (void)searchBar:(UISearchBar *)searchBar textDidChange:(NSString *)searchText{
    query = [searchText stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
}
- (void)searchBarTextDidBeginEditing:(UISearchBar *)searchBar{
    [searchBar setShowsCancelButton:true animated:true];
}


@end
