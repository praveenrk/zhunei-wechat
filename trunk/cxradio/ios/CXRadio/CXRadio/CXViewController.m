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
@property (nonatomic, strong) NSDate *albumDate;

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
    
    _albumDate = [NSDate date];
    
    __weak typeof(self) wSelf = self;
    [_albumTable addPullToRefreshWithActionHandler:^{
        [wSelf loadDataWithDate:wSelf.albumDate completionBlock:nil];
    }];

    [self loadDataWithDate:_albumDate completionBlock:^{
        [_playerView playWithAlbum:_album beginIndex:0];
    }];
    
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(albumDateChangeNotificationAction:)
                                                 name:changeAlbumDateNotification
                                               object:nil];

}

-(void) albumDateChangeNotificationAction:(NSNotification *) notification;
{
    _albumDate = notification.userInfo[@"date"];
    [self loadDataWithDate:_albumDate completionBlock:^{
        [_playerView playWithAlbum:_album beginIndex:0];
    }];
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

-(void) loadDataWithDate:(NSDate *) date completionBlock:(void (^)(void)) block;
{
    
    NSDateFormatter *formatter = [[NSDateFormatter alloc] init];
    formatter.dateFormat = @"yyyy-MM-dd";
    
    NSString *dateString = [formatter stringFromDate:date == nil ? [NSDate date] : date];
    
    [[XPHttpClient sharedInstance] getAudioListWithDateString:dateString CompletionBlock:^(CXAlbum *album, NSError *error) {
        LogDebug(@"%@", album);
        
        self.album = album;
        _naviationTitleLabel.text = self.album.title;
        [_albumTable reloadData];
    
        [_albumTable.pullToRefreshView stopAnimating];
        
        if (block) {
            block();
        }
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
