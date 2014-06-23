//
//  CRAudioListVC.m
//  cxradio
//
//  Created by Peter on 14-5-10.
//  Copyright (c) 2014年 CathAssist. All rights reserved.
//

#import "CRTrackListVC.h"
#import "CRChannelVC.h"
#import "CRTrackListCell.h"

@interface CRTrackListVC ()<UITableViewDelegate, UITableViewDataSource>
@property (strong, nonatomic) IBOutlet UITableView *tableView;

@property (strong, nonatomic) CRChannelModel* channel;

@end

@implementation CRTrackListVC

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
    // Do any additional setup after loading the view.
    self.tableView.delegate = self;
    self.channel = [CRChannelModel alloc];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void) resetChannel:(CRChannelModel*)channel
{
    self.channel = channel;
    [self.tableView reloadData];
    
    if(self.channel.tracks.count > 0)
    {
        CRChannelVC* vc = (CRChannelVC*)self.mainVC;
        
        [self.channel setTrackWithIndex:0];
        [vc.delegateTrack trackChanged];
    }
}
/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/


#pragma mark UITableViewDelegate Methods

- (NSString *)tableView:(UITableView *)tableView titleForHeaderInSection:(NSInteger)section;
{
    return @"  ";
}

- (NSInteger) tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{    
    return self.channel.tracks.count;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath;
{
    return 44;
}

- (UITableViewCell *) tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    static NSString* identifier = @"CRTrackListCell";
    CRTrackListCell* cell = [tableView dequeueReusableCellWithIdentifier:identifier];
    if (!cell) {
        cell = [[CRTrackListCell alloc] initWithStyle:UITableViewCellStyleSubtitle reuseIdentifier:identifier];
        cell.backgroundColor = [UIColor clearColor];
    }
    
    CRTrackModel* track = [self.channel.tracks objectAtIndex:indexPath.item];
    [cell setTrackModel:track];
    
    return cell;
}


- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath;
{
    CRChannelVC* vc = (CRChannelVC*)self.mainVC;

    [self.channel setTrackWithIndex:indexPath.item];
    [vc.delegateTrack trackChanged];
    [vc closeRightMenu];
    return;
}

@end
