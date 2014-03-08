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
#import "XPPlayerView.h"

@interface CXViewController ()<UITableViewDelegate, UITableViewDataSource>
{
    UITableView *_albumTable;
    XPPlayerView  *_playerView;
    UILabel *_naviationTitleLabel;
    
    CGFloat _leftSpot;
    CGFloat _rightSpot;
    
    UIButton *_natigationBarRightButton;
    
}

@property (nonatomic, strong) CXAlbum *album;

@end

@implementation CXViewController


-(void) loadView;
{
    [super loadView];
    self.view.backgroundColor = themeBgColor;
    
    CGRect frame = self.view.bounds;
    frame.size.height = screenHeight - 20;
    
    
    _albumTable = [[UITableView alloc] initWithFrame:CGRectMake(screenWidth-314/2, 0, 314/2, frame.size.height)
                                               style:UITableViewStylePlain];
    _albumTable.delegate = self;
    _albumTable.dataSource = self;
    _albumTable.backgroundColor = [UIColor lightGrayColor];
   [self.view addSubview:_albumTable];
   
    _playerView = [[XPPlayerView alloc] initWithFrame:frame];
    _playerView.backgroundColor = themeBgColor;
    [self.view addSubview:_playerView];

    _naviationTitleLabel = [[UILabel alloc] initWithFrame:CGRectMake(0, 0, 320, 44)];
    _naviationTitleLabel.backgroundColor = [UIColor colorWithWhite:0xf7/255.0 alpha:1.0];
    _naviationTitleLabel.textColor = [UIColor blackColor];
    _naviationTitleLabel.textAlignment = NSTextAlignmentCenter;
    _naviationTitleLabel.font = [UIFont systemFontOfSize:17.0];
    _naviationTitleLabel.userInteractionEnabled = YES;
    [_playerView addSubview:_naviationTitleLabel];
    
    _natigationBarRightButton = [[UIButton alloc] initWithFrame:CGRectMake(0, 0, 30, 30)];
    [_natigationBarRightButton setBackgroundImage:[UIImage imageNamed:@"icon_menu"] forState:UIControlStateNormal];
    _natigationBarRightButton.center = CGPointMake(320-32, 44/2);
    [_natigationBarRightButton addTarget:self action:@selector(rightButtonAction:) forControlEvents:UIControlEventTouchUpInside];
    [_playerView addSubview:_natigationBarRightButton];
    
    _leftSpot = 320/2 - 314/2;
    _rightSpot = 320/2;
    
    UISwipeGestureRecognizer *gesture = [[UISwipeGestureRecognizer alloc] initWithTarget:self
                                                                                  action:@selector(swipeGestureAction:)];
    gesture.direction = UISwipeGestureRecognizerDirectionLeft | UISwipeGestureRecognizerDirectionRight;
    [_playerView addGestureRecognizer:gesture];
    
}


-(void) swipeGestureAction:(UISwipeGestureRecognizer *) gesture;
{
    [self expandOrShrinkListTable];
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    
    __weak typeof(self) wSelf = self;
    
    [_albumTable addPullToRefreshWithActionHandler:^{
        [wSelf loadData];
    }];
    
    [_albumTable triggerPullToRefresh];

}


-(void) rightButtonAction:(UIButton *) sender;
{
    [self expandOrShrinkListTable];
}

-(void) expandOrShrinkListTable;
{
    [UIView animateWithDuration:0.2 delay:0 options:0 animations:^{
        _playerView.center = CGPointMake(_playerView.center.x == _leftSpot ? _rightSpot : _leftSpot,
                                         _playerView.center.y);
        
    } completion:^(BOOL finished) {
        
    }];
 
}

-(void) loadData;
{
    [[XPHttpClient sharedInstance] getAudioListWithDateString:nil CompletionBlock:^(CXAlbum *album, NSError *error) {
        LogDebug(@"%@", album);
        
        self.album = album;
        _naviationTitleLabel.text = self.album.title;
        [_albumTable reloadData];
    
        
        static dispatch_once_t onceToken;
        dispatch_once(&onceToken, ^{
            [_playerView playWithAlbum:_album beginIndex:0];
        });
        
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
    [_albumTable deselectRowAtIndexPath:indexPath animated:YES];
    
    [_playerView playWithAlbum:self.album beginIndex:indexPath.row];
    
    [self expandOrShrinkListTable];
}


@end
