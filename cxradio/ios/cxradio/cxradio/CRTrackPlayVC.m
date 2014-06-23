//
//  CRViewController.m
//  cxradio
//
//  Created by Peter on 14-5-1.
//  Copyright (c) 2014年 CathAssist. All rights reserved.
//

#import "CRTrackPlayVC.h"
#import "RMDateSelectionViewController.h"
#import "Model/CRChannelModel.h"
#import "AFNetworking.h"
#import "UIViewController+AMSlideMenu.h"
#import "CRChannelVC.h"
#import "STKAudioPlayer.h"
#import <CoreGraphics/CoreGraphics.h>
#import "MBProgressHUD.h"
#import "CRAppDelegate.h"
#import <MediaPlayer/MediaPlayer.h>

@interface CRTrackPlayVC () <RMDateSelectionViewControllerDelegate,CRTrackDelegate,STKAudioPlayerDelegate>
{
    STKAudioPlayer* trackPlayer;
    NSTimer* timerProgress;                 //执行每1秒刷新一下进度
    NSDateFormatter* dateFormatter;
    NSDate* dateCurrent;
    CRChannelModel* channel;                //
}
@property (weak, nonatomic) IBOutlet UIButton* btnPlayPause;             //播放按钮
@property (weak, nonatomic) IBOutlet UIButton* btnCurDay;

@property (weak, nonatomic) IBOutlet UIImageView* imageViewHandle;      //CD上的把手
@property (weak, nonatomic) IBOutlet UIImageView* imageViewIcon;        //当前的图标
@property (weak, nonatomic) IBOutlet UIImageView* imageViewTimeMask;

@property (weak, nonatomic) IBOutlet UILabel* labelCurTime;             //当前时间
@property (weak, nonatomic) IBOutlet UILabel* labelCurAudio;            //当前播放的音频名称
@property (weak, nonatomic) IBOutlet UISlider* sliderDuration;

@property (nonatomic, strong) UIImage* imageIcon;
@property (strong, nonatomic) IBOutlet UIScrollView *scrollViewMain;

@property (weak, nonatomic) IBOutlet UIButton* btnRadioRight;
@property (weak, nonatomic) IBOutlet UIButton* btnSoftRight;

@end

@implementation CRTrackPlayVC

- (void)viewDidLoad
{
    [super viewDidLoad];
	// Do any additional setup after loading the view, typically from a nib.
    //初始化播放器
    trackPlayer = [[STKAudioPlayer alloc] init];
    trackPlayer.delegate = self;
    
    timerProgress = [NSTimer scheduledTimerWithTimeInterval:1 target: self selector: @selector(timerProgressUpdate) userInfo: nil repeats: YES];
    
    
    //设置日期格式化方式
    dateFormatter = [[NSDateFormatter alloc] init];
    [dateFormatter setLocale:[NSLocale currentLocale]];
    [dateFormatter setDateFormat:@"YYYY-MM-dd"];
    
    //获取当前时间
    dateCurrent = [NSDate date];
    
    self.title = @"晨星生命之音";
    self.imageIcon = [UIImage imageNamed:@"MyIcon"];        //初始化图标
    
    [self.btnRadioRight setTitle:NSLocalizedString(@"Radio Right", nil) forState:UIControlStateNormal];
    [self.btnSoftRight setTitle:NSLocalizedString(@"Soft Right", nil) forState:UIControlStateNormal];
    
    self.imageViewIcon.image = self.imageIcon;
    self.imageViewIcon.layer.masksToBounds = YES;
    self.imageViewIcon.layer.cornerRadius = 64;
    
    
    
    [self.btnPlayPause setBackgroundImage:[UIImage imageNamed:@"play_ctrl"] forState:UIControlStateNormal];
    [self.btnPlayPause setBackgroundImage:[UIImage imageNamed:@"pause_ctrl"] forState:UIControlStateSelected];
    
    {
        //设置当前时间下的Mask
        CGSize imageSize = CGSizeMake(250, 250);
        UIGraphicsBeginImageContextWithOptions(imageSize, 0, [UIScreen mainScreen].scale);
        [[UIColor colorWithRed:0x0 green:0x0 blue:0x0 alpha:0.6] set];
        UIRectFill(CGRectMake(0, 0, imageSize.width, imageSize.height));
        
        [[UIColor colorWithRed:0x0 green:0x0 blue:0x0 alpha:0] set];
        UIRectFill(CGRectMake(0, 0, imageSize.width, imageSize.height*0.75));
        UIImage* imageTimeMask = UIGraphicsGetImageFromCurrentImageContext();
        UIGraphicsEndImageContext();
        
        self.imageViewTimeMask.image = imageTimeMask;
        self.imageViewTimeMask.layer.masksToBounds = YES;
        self.imageViewTimeMask.layer.cornerRadius = 65;
    }
    
    [self setCurrentDate:dateCurrent];
}

- (void) viewDidAppear:(BOOL)animated
{
    [super viewDidAppear:animated];
    
    //设置窗口大小
    self.scrollViewMain.contentSize = CGSizeMake(320, 550);
    
    [self setPlayingState:NO];
    
    //注册进程挂起和进程激活消息，用于控制动画的显示
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(applicationWillResignActive) name:@"ApplictionWillResignActive" object:nil];
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(applicationDidBecomeActive) name:@"applicationDidBecomeActive" object:nil];
    
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(remoteControlReceivedWithEvent:) name:@"remoteControlReceivedWithEvent" object:nil];
}

- (void)applicationWillResignActive
{
    [self setPlayingState:false];
}

- (void)applicationDidBecomeActive
{
    if(trackPlayer.state == STKAudioPlayerStatePlaying
       || trackPlayer.state == STKAudioPlayerStateBuffering)
    {
        [self setPlayingState:true];
    }
}

- (void)remoteControlReceivedWithEvent:(NSNotification*) notification
{
    UIEvent* e = [notification object];
    
    if (e.type == UIEventTypeRemoteControl)
    {
        switch (e.subtype)
        {
            case UIEventSubtypeRemoteControlTogglePlayPause:
                if(trackPlayer.state == STKAudioPlayerStatePlaying)
                {
                    [trackPlayer pause];
                    [self setPlayingState:NO];
                }
                else
                {
                    [trackPlayer resume];
                    [self setPlayingState:YES];
                }
                NSLog(@"RemoteControlEvents: pause");
                break;
            case UIEventSubtypeRemoteControlNextTrack:
                [self setCurrentTrack:channel.nextTrack];
                NSLog(@"RemoteControlEvents: playModeNext");
                break;
            case UIEventSubtypeRemoteControlPreviousTrack:
                [self setCurrentTrack:channel.prevTrack];
                NSLog(@"RemoteControlEvents: playPrev");
                break;
            default:
                break;
        }
    }
}

- (void) setPlayingState:(BOOL)_playing
{
    if(self.btnPlayPause.selected == _playing)
        return;
    self.btnPlayPause.selected = _playing;
    
    //当前播放时间
//    self.labelCurTime.text = !_playing ? @"10:11" : @"11:00:21";
    
    //调整Icon的状态(旋转/停止)
    if(_playing)
    {
        CABasicAnimation* rotationAnimation;
        rotationAnimation = [CABasicAnimation animationWithKeyPath:@"transform.rotation.z"];
        rotationAnimation.toValue = [NSNumber numberWithFloat: M_PI * 2.0 ];
        rotationAnimation.duration = 10;
        rotationAnimation.cumulative = YES;
        rotationAnimation.repeatCount = 100000;
    
        [_imageViewIcon.layer addAnimation:rotationAnimation forKey:@"rotationAnimation"];
    }
    else
    {
        [self.imageViewIcon.layer removeAllAnimations];
    }
    
    
    
    //调整handle的状态
    [UIView beginAnimations:@"handleOn" context:nil];
    [UIView setAnimationDuration:1];
    [UIView setAnimationBeginsFromCurrentState:YES];
    
    self.imageViewHandle.center = CGPointMake(self.imageViewHandle.frame.size.width, 0);
    self.imageViewHandle.transform = CGAffineTransformRotate(self.imageViewHandle.transform, _playing ? 0.4 : -0.4);
    
    [UIView commitAnimations];
}

- (void) setCurrentDate:(NSDate*)_date
{
    NSString* strDate = [dateFormatter stringFromDate:_date];
    
    NSString* strUrl = [NSString stringWithFormat:@"http://cathassist.org/radio/getradio.php?channel=cx&date=%@",strDate];
    NSLog(@"Fetch channel from:%@",strUrl);
    
    
    MBProgressHUD* hud = [MBProgressHUD showHUDAddedTo:self.view animated:YES];
    hud.dimBackground = YES;
    hud.labelText = NSLocalizedString(@"Loading",nil);
    [hud show:true];
    
//    [AFJSONRequestOperation addAcceptableContentTypes:[NSSet setWithObject:@"text/html"]];
    AFHTTPRequestOperationManager* manager = [AFHTTPRequestOperationManager manager];
    [manager GET:strUrl parameters:nil success:^(AFHTTPRequestOperation *operation, id responseObject)
    {
        if(responseObject!=nil)
        {
            NSDictionary* dict = responseObject;
            
            CRChannelModel* model = [[CRChannelModel alloc] initWithDictionary:dict];
            NSLog(@"Load channel:%@",model.title);
            channel = model;
            CRChannelVC* mainVC = (CRChannelVC*)[self mainSlideMenu];
            mainVC.delegateTrack = self;
            [mainVC setCurrentChannel:model];
            
            dateCurrent = [dateFormatter dateFromString:model.date];
            [self.btnCurDay setTitle:model.date forState:UIControlStateNormal];
            [hud removeFromSuperview];
        }
    }
    failure:^(AFHTTPRequestOperation *operation, NSError *error)
    {
        NSLog(@"Error: %@", error);
        [hud removeFromSuperview];
    }];
}

- (void) setCurrentTrack:(CRTrackModel*) track
{
    if(track)
    {
        self.labelCurAudio.text = track.title;
        [trackPlayer play:track.src];
        [self setPlayingState:YES];
        
        //更新锁屏时的歌曲信息
        if (NSClassFromString(@"MPNowPlayingInfoCenter")) {
            NSMutableDictionary *dict = [[NSMutableDictionary alloc] init];
            
            [dict setObject:track.title forKey:MPMediaItemPropertyTitle];
            [dict setObject:channel.title forKey:MPMediaItemPropertyArtist];
//            [dict setObject:@"专辑名" forKey:MPMediaItemPropertyAlbumTitle];
            
            UIImage* newImage = [UIImage imageNamed:@"MyIcon"];
            [dict setObject:[[MPMediaItemArtwork alloc] initWithImage:newImage]
                     forKey:MPMediaItemPropertyArtwork];
            
            [[MPNowPlayingInfoCenter defaultCenter] setNowPlayingInfo:dict];
        }
    }
}


- (IBAction)btnPlayPauseClicked:(UIButton*)sender
{
    if(!sender.isSelected)
    {
        if(trackPlayer.currentlyPlayingQueueItemId == nil)
        {
            [self setCurrentTrack:[channel firstTrack]];
        }
        else
        {
            [trackPlayer resume];
        }
    }
    else
    {
        [trackPlayer pause];
    }
    
    [self setPlayingState:!sender.isSelected];
}

- (IBAction)btnPrevClicked:(id)sender
{
    [self setCurrentTrack:channel.prevTrack];
}
- (IBAction)btnNextClicked:(id)sender
{
    [self setCurrentTrack:channel.nextTrack];
}

- (IBAction)btnPrevDayClicked:(id)sender
{
    NSCalendar* calendar = [[NSCalendar alloc] initWithCalendarIdentifier:NSGregorianCalendar];
    
    NSDateComponents* adcomps = [[NSDateComponents alloc] init];
    [adcomps setYear:0];
    [adcomps setMonth:0];
    [adcomps setDay:-1];
    
    NSDate* newdate = [calendar dateByAddingComponents:adcomps toDate:dateCurrent options:0];
    
    [self setCurrentDate:newdate];
}
- (IBAction)btnNextDayClicked:(id)sender
{
    NSCalendar* calendar = [[NSCalendar alloc] initWithCalendarIdentifier:NSGregorianCalendar];
    
    NSDateComponents* adcomps = [[NSDateComponents alloc] init];
    [adcomps setYear:0];
    [adcomps setMonth:0];
    [adcomps setDay:1];
    
    NSDate* newdate = [calendar dateByAddingComponents:adcomps toDate:dateCurrent options:0];
    
    [self setCurrentDate:newdate];
}
- (IBAction)btnCurDayClicked:(id)sender
{
    RMDateSelectionViewController* dateSelectionVC = [RMDateSelectionViewController dateSelectionController];
    dateSelectionVC.delegate = self;
    dateSelectionVC.datePicker.datePickerMode = UIDatePickerModeDate;
    dateSelectionVC.datePicker.date = dateCurrent;
    
    [dateSelectionVC show];
}

- (IBAction)btnDownloadClicked:(id)sender
{
    
}

- (IBAction)btnClearClicked:(id)sender
{
    
}



- (IBAction)sliderDurationChanged:(id)sender
{
    [trackPlayer seekToTime:self.sliderDuration.value];
}

- (IBAction)btnRadioRightClicked:(id)sender
{
    [[UIApplication sharedApplication] openURL:[NSURL URLWithString:@"http://www.cathassist.org/3rd/aboutcx.html"]];
}

- (IBAction)btnSoftRightClicked:(id)sender
{
    [[UIApplication sharedApplication] openURL:[NSURL URLWithString:@"http://www.cathassist.org/3rd/aboutus.html"]];
}


- (void)dateSelectionViewController:(RMDateSelectionViewController *)vc didSelectDate:(NSDate *)aDate
{
    //选择了新的日期
    [self setCurrentDate:aDate];
}

- (void)dateSelectionViewControllerDidCancel:(RMDateSelectionViewController *)vc
{
    //Do something else
}

- (void)trackChanged
{
    [self setCurrentTrack:channel.currentTrack];
}


/// Raised when an item has started playing
-(void) audioPlayer:(STKAudioPlayer*)audioPlayer didStartPlayingQueueItemId:(NSObject*)queueItemId;
{
    self.sliderDuration.maximumValue = audioPlayer.duration;
    self.sliderDuration.value = audioPlayer.progress;
}
/// Raised when an item has finished buffering (may or may not be the currently playing item)
/// This event may be raised multiple times for the same item if seek is invoked on the player
-(void) audioPlayer:(STKAudioPlayer*)audioPlayer didFinishBufferingSourceWithQueueItemId:(NSObject*)queueItemId;
{
    
}
/// Raised when the state of the player has changed
-(void) audioPlayer:(STKAudioPlayer*)audioPlayer stateChanged:(STKAudioPlayerState)state previousState:(STKAudioPlayerState)previousState;
{
    if(state == STKAudioPlayerStatePlaying)
    {
        self.sliderDuration.maximumValue = audioPlayer.duration;
        self.sliderDuration.value = audioPlayer.progress;
        [self setPlayingState:YES];
    }
    else if (state == STKAudioPlayerStateStopped ||
             state == STKAudioPlayerStateError)
    {
        [self setPlayingState:NO];
    }
}
/// Raised when an item has finished playing
-(void) audioPlayer:(STKAudioPlayer*)audioPlayer didFinishPlayingQueueItemId:(NSObject*)queueItemId withReason:(STKAudioPlayerStopReason)stopReason andProgress:(double)progress andDuration:(double)duration;
{
    if(((stopReason == STKAudioPlayerStopReasonNone) && (trackPlayer.currentlyPlayingQueueItemId == nil)) || stopReason == STKAudioPlayerStopReasonEof)
    {
        [self setCurrentTrack:[channel nextTrack]];
    }
}
/// Raised when an unexpected and possibly unrecoverable error has occured (usually best to recreate the STKAudioPlauyer)
-(void) audioPlayer:(STKAudioPlayer*)audioPlayer unexpectedError:(STKAudioPlayerErrorCode)errorCode
{
    
}

-(void) timerProgressUpdate
{
    self.sliderDuration.maximumValue = trackPlayer.duration;
    self.sliderDuration.value = trackPlayer.progress;
    int progress = (int)trackPlayer.progress;
    
    NSString* strTime = @"00:00";
    if(trackPlayer.duration > 60*60)
    {
        strTime = [NSString stringWithFormat:@"%.2d:%.2d:%.2d",progress/(60*60),(progress/60)%60,progress%60];
    }
    else
    {
        strTime = [NSString stringWithFormat:@"%.2d:%.2d",progress/60,progress%60];
    }
    
    self.labelCurTime.text = strTime;
}

@end
