//
//  CXViewController.m
//  CXRadio
//
//  Created by tashigaofei on 14-3-7.
//  Copyright (c) 2014年 zhaoyanjun. All rights reserved.
//

#import "CXViewController.h"

#import "XPHttpClient+Album.h"
#import "SVPullToRefresh.h"

@interface CXViewController ()<UITableViewDelegate, UITableViewDataSource>
{
    UIView *_contentView;
    UITableView *_albumTable;
    
}

@property (nonatomic, strong) CXAlbum *album;

@end

@implementation CXViewController


-(void) loadView;
{
    [super loadView];
    
    CGRect frame = self.view.bounds;
    frame.size.height = screenHeight - 20-44;
    
    _contentView = [[UIView alloc] initWithFrame:frame];
    _contentView.backgroundColor = themeBgColor;
    [self.view addSubview:_contentView];
    
    _albumTable = [[UITableView alloc] initWithFrame:CGRectMake(screenWidth-314/2, 0, 314/2, _contentView.bounds.size.height)
                                               style:UITableViewStylePlain];
    _albumTable.delegate = self;
    _albumTable.dataSource = self;
    _albumTable.backgroundColor = [UIColor lightGrayColor];
    [self.view addSubview:_albumTable];
    
    
}


- (void)viewDidLoad
{
    [super viewDidLoad];
    
    self.view.backgroundColor = themeBgColor;
    
    
    __weak typeof(self) wSelf = self;
    
    [_albumTable addPullToRefreshWithActionHandler:^{
        [wSelf loadData];
    }];
    
//    [_albumTable triggerPullToRefresh];
    

}

-(void) loadData;
{
    [[XPHttpClient sharedInstance] getAudioListWithDateString:nil CompletionBlock:^(CXAlbum *album, NSError *error) {
        LogDebug(@"%@", album);
        
        self.album = album;
        [_albumTable reloadData];
        
        [_albumTable.pullToRefreshView stopAnimating];
    }];
    
}


- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}



#pragma mark UITableViewDelegate Methods

- (NSInteger) tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    
    return [_album.items count];
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath;
{
    return 44;
}

- (UITableViewCell *) tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    static NSString *identifier = @"UITableViewCell";
    UITableViewCell  *cell = [tableView dequeueReusableCellWithIdentifier:identifier];
    if (!cell) {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleSubtitle reuseIdentifier:identifier];
        cell.backgroundColor = [UIColor clearColor];
    }
    
    XPTrack *audio = _album.items[indexPath.row];
    cell.textLabel.text = audio.title;
    cell.textLabel.font = [UIFont boldSystemFontOfSize:14.0];
    cell.textLabel.textColor = [UIColor colorWithWhite:0.2 alpha:0.9];
    
    return cell;
}


- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath;
{
//    [self deselectRowAtIndexPath:indexPath animated:YES];
//    if ([_tableDelegate respondsToSelector:@selector(audioListTable:didSelectAudio:)]) {
//        [_tableDelegate audioListTable:self didSelectAudio:_tableDataSource[indexPath.row]];
//    }
}


@end
