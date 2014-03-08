//
//  XPPlayerView.m
//  XPBroadcast
//
//  Created by tashigaofei on 13-11-26.
//  Copyright (c) 2013年 ZhaoYanJun. All rights reserved.
//

#import "XPPlayerView.h"
#import "XPExpandedSlider.h"

static void *kStatusKVOKey = &kStatusKVOKey;
static void *kDurationKVOKey = &kDurationKVOKey;
static void *kBufferingRatioKVOKey = &kBufferingRatioKVOKey;

@interface XPPlayerView()<UIActionSheetDelegate>
{
    DOUAudioStreamer *_streamer;
    NSUInteger _currentIndex;
    double _downloadProgress;
}

@property (nonatomic, strong) UIButton *buttonPlayPause;
@property (nonatomic, strong) UIButton *buttonNext;
@property (nonatomic, strong) UIButton *buttonLast;

@property (nonatomic, strong) UIButton *buttonNextDay;
@property (nonatomic, strong) UIButton *buttonLastDay;
@property (nonatomic, strong) UIButton *dayButton;

@property (nonatomic, strong) UILabel *labelTitle;
@property (nonatomic, strong) UILabel *labelInfo;
@property (nonatomic, strong) UILabel *labelMisc;

@property (nonatomic, strong) XPExpandedSlider *sliderProgress;
@property (nonatomic, strong) XPExpandedSlider *sliderVolume;

@property (nonatomic, strong) CXAlbum *album;

@property (nonatomic, strong) UIImageView *albumCoverImageView;


@end

@implementation XPPlayerView

- (id)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        
        UIImageView *bgCDImageView = [[UIImageView alloc] initWithFrame:CGRectMake(0, 0, 256, 256)];
        bgCDImageView.image = [UIImage imageNamed:@"cd_bg"];
        bgCDImageView.center = CGPointMake(screenWidth/2, 190);
        [self addSubview:bgCDImageView];
       
        _albumCoverImageView = [[UIImageView alloc] initWithFrame:CGRectMake(0, 0, 256/2, 256/2)];
        _albumCoverImageView.layer.masksToBounds = YES;
        _albumCoverImageView.layer.cornerRadius = 256/2/2;
        _albumCoverImageView.center = CGPointMake(screenWidth/2, 190);
        [self addSubview:_albumCoverImageView];
        
        CABasicAnimation *rotationAnimation;
        rotationAnimation = [CABasicAnimation animationWithKeyPath:@"transform.rotation.z"];
        rotationAnimation.toValue = [NSNumber numberWithFloat:M_PI * 2.0];
        rotationAnimation.duration = 2.0;
        rotationAnimation.cumulative = NO;
        rotationAnimation.repeatCount = MAXFLOAT;
        rotationAnimation.removedOnCompletion = NO;
        rotationAnimation.fillMode = kCAFillModeBackwards;
        rotationAnimation.timingFunction = [CAMediaTimingFunction functionWithName:kCAMediaTimingFunctionLinear];
        [_albumCoverImageView.layer addAnimation:rotationAnimation forKey:@"rotationAnimation"];
        self.layer.speed = 0;
        
        _buttonPlayPause = [[UIButton alloc] initWithFrame:CGRectMake(0, 0, 40, 40)];
        _buttonPlayPause.center = CGPointMake(screenWidth/2, 190);
        [_buttonPlayPause setBackgroundImage:[UIImage imageNamed:@"pause"] forState:UIControlStateNormal];
        [_buttonPlayPause setBackgroundImage:[UIImage imageNamed:@"play"] forState:UIControlStateSelected];
        [_buttonPlayPause addTarget:self action:@selector(actionPlayPause:) forControlEvents:UIControlEventTouchUpInside];
        [self addSubview:_buttonPlayPause];
        
        _buttonLast = [[UIButton alloc] initWithFrame:CGRectMake(0, 0, 35, 35)];
        _buttonLast.center = CGPointMake(25, 355);
        [_buttonLast setBackgroundImage:[UIImage imageNamed:@"last"] forState:UIControlStateNormal];
        [_buttonLast addTarget:self action:@selector(actionLast:) forControlEvents:UIControlEventTouchUpInside];
        [self addSubview:_buttonLast];
        
        _buttonNext = [[UIButton alloc] initWithFrame:CGRectMake(0, 0, 35, 35)];
        _buttonNext.center = CGPointMake(295, 355);
        [_buttonNext setBackgroundImage:[UIImage imageNamed:@"next"] forState:UIControlStateNormal];
        [_buttonNext addTarget:self action:@selector(actionNext:) forControlEvents:UIControlEventTouchUpInside];
        [self addSubview:_buttonNext];
        
        _labelTitle = [[UILabel alloc] initWithFrame:CGRectMake(0, 0, 200, 30)];
        _labelTitle.center = CGPointMake(screenWidth/2, 355);
        _labelTitle.backgroundColor = [UIColor clearColor];
        _labelTitle.textColor = [UIColor blackColor];
        _labelTitle.textAlignment = NSTextAlignmentCenter;
        _labelTitle.font = [UIFont systemFontOfSize:18.0];
        [self addSubview:_labelTitle];
        
        _labelInfo = [[UILabel alloc] initWithFrame:CGRectMake(0, 0, 200, 30)];
        _labelInfo.center = CGPointMake(screenWidth/2, 375);
        _labelInfo.textAlignment = NSTextAlignmentCenter;
        _labelInfo.backgroundColor = [UIColor clearColor];
        _labelInfo.textColor = [UIColor colorWithWhite:0.1 alpha:0.8];
        _labelInfo.font = themeFont;
        [self addSubview:_labelInfo];
        
        _labelMisc = [[UILabel alloc] initWithFrame:CGRectMake(0, 0, 300, 30)];
        _labelMisc.center = CGPointMake(screenWidth/2, 130);
        _labelMisc.backgroundColor = [UIColor clearColor];
        _labelMisc.font = themeFont;
        _labelMisc.adjustsFontSizeToFitWidth = YES;
//        [self addSubview:_labelMisc];
        
        _sliderProgress = [[XPExpandedSlider alloc ] initWithFrame:CGRectMake(0, 0, 300, 30)];
        _sliderProgress.center = CGPointMake(screenWidth/2, 400);
        _sliderProgress.backgroundColor = [UIColor clearColor];
        [self addSubview:_sliderProgress];
        [_sliderProgress addTarget:self action:@selector(actionSliderProgress:) forControlEvents:UIControlEventValueChanged];
        UITapGestureRecognizer *progressGesture = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(sliderTapped:)];
        [_sliderProgress addGestureRecognizer:progressGesture];
        
//        _sliderVolume = [[XPExpandedSlider alloc ] initWithFrame:CGRectMake(0, 0, 120, 30)];
//        _sliderVolume.center = CGPointMake(screenWidth/2, 200);
//        _sliderVolume.backgroundColor = [UIColor clearColor];
//        [self addSubview:_sliderVolume];
//        [_sliderVolume addTarget:self action:@selector(actionSliderVolume:) forControlEvents:UIControlEventValueChanged];
//        UITapGestureRecognizer *volumeGesture = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(sliderTapped:)];
//        [_sliderVolume addGestureRecognizer:volumeGesture];

        
        
        _buttonLastDay = [[UIButton alloc] initWithFrame:CGRectMake(0, 0, 60, 35)];
        _buttonLastDay.center = CGPointMake(106/2, 430);
        [_buttonLastDay setTitleColor:[UIColor blueColor] forState:UIControlStateNormal];
        _buttonLastDay.titleLabel.font = [UIFont systemFontOfSize:15];
        [_buttonLastDay setTitle:@"上一天" forState:UIControlStateNormal];
        [_buttonLastDay addTarget:self action:@selector(actionLast:) forControlEvents:UIControlEventTouchUpInside];
        [self addSubview:_buttonLastDay];
        
        _buttonNextDay = [[UIButton alloc] initWithFrame:CGRectMake(0, 0, 60, 35)];
        _buttonNextDay.center = CGPointMake(530/2, 430);
        [_buttonNextDay setTitleColor:[UIColor blueColor] forState:UIControlStateNormal];
        [_buttonNextDay setTitle:@"下一天" forState:UIControlStateNormal];
        _buttonNextDay.titleLabel.font = [UIFont systemFontOfSize:15];
        [_buttonNextDay addTarget:self action:@selector(actionNext:) forControlEvents:UIControlEventTouchUpInside];
        [self addSubview:_buttonNextDay];
        
        _dayButton = [[UIButton alloc] initWithFrame:CGRectMake(0, 0, 150, 35)];
        _dayButton.center = CGPointMake(320/2, 430);
        [_dayButton setTitleColor:[UIColor blueColor] forState:UIControlStateNormal];
        _dayButton.titleLabel.font = [UIFont systemFontOfSize:15];
        [_dayButton addTarget:self action:@selector(dateButtonAction:) forControlEvents:UIControlEventTouchUpInside];
        [self addSubview:_dayButton];
        
        
    }
    
    return self;
}


-(void) dateButtonAction:(UIButton *) sender;
{
    UIActionSheet *sheet = [[UIActionSheet alloc] initWithTitle:@"\n\n\n\n\n\n\n\n\n\n\n" delegate:self
                                              cancelButtonTitle:nil
                                         destructiveButtonTitle:@"取消"
                                              otherButtonTitles:@"确定", nil];
    
    [sheet showInView:self];
    
    UIDatePicker *datePicker = [[UIDatePicker alloc] initWithFrame:CGRectMake(0, 0, 320, 100)];
    NSDateFormatter *formatter = [[NSDateFormatter alloc] init];
    formatter.dateFormat = @"yyyy-MM-dd";
    datePicker.date = [formatter dateFromString:self.album.date];
    datePicker.center = CGPointMake(screenWidth/2, 100);
    datePicker.datePickerMode = UIDatePickerModeDate;
    datePicker.tag = 10000+1;
    [sheet addSubview:datePicker];
    
    
}

#define Sheet_OK 1

- (void)actionSheet:(UIActionSheet *)actionSheet clickedButtonAtIndex:(NSInteger)buttonIndex;
{
    UIDatePicker *datePicker = (UIDatePicker *) [actionSheet viewWithTag:10000 + 1];
    if (buttonIndex == Sheet_OK) {
        [[NSNotificationCenter defaultCenter] postNotificationName:changeAlbumDateNotification
                                                            object:nil userInfo:@{@"date": datePicker.date}];
    }
    
}

- (void)sliderTapped:(UITapGestureRecognizer *) gesture {
    
    if (gesture.state == UIGestureRecognizerStateEnded ) {
        UISlider* slider = (UISlider*) gesture.view;
        if (slider.highlighted)
            return;
        
        CGPoint point  = [gesture locationInView: slider];
        CGFloat percentage = point.x / slider.bounds.size.width;
        CGFloat delta = percentage * (slider.maximumValue - slider.minimumValue);
        CGFloat value = slider.minimumValue + delta;
        [slider setValue:value animated:YES];
        [slider sendActionsForControlEvents:UIControlEventValueChanged];

    }
    
}

-(void) playWithAlbum:(CXAlbum *) album beginIndex:(NSUInteger) index;
{
    
    self.album = album;
    
    [_dayButton setTitle:self.album.date forState:UIControlStateNormal];
    
    [_albumCoverImageView setImageWithURL:[NSURL URLWithString:self.album.logo]];
    
    _currentIndex = index < [self.album.items count] ? index : 0;
    [self _resetStreamer];
    
    [NSTimer scheduledTimerWithTimeInterval:1.0 target:self selector:@selector(_timerAction:) userInfo:nil repeats:YES];
    [_sliderVolume setValue:[DOUAudioStreamer volume]];
}

- (void)_resetStreamer
{
    if (_streamer != nil) {
        [_streamer pause];
        [_streamer removeObserver:self forKeyPath:@"status"];
        [_streamer removeObserver:self forKeyPath:@"duration"];
        [_streamer removeObserver:self forKeyPath:@"bufferingRatio"];
        _streamer = nil;
    }
    
    XPTrack *track = [self.album.items objectAtIndex:_currentIndex];
    NSString *title = [NSString stringWithFormat:@"%@", track.title];
    [_labelTitle setText:title];
    
    _streamer = [DOUAudioStreamer streamerWithAudioFile:track];
    [_streamer addObserver:self forKeyPath:@"status" options:NSKeyValueObservingOptionNew context:kStatusKVOKey];
    [_streamer addObserver:self forKeyPath:@"duration" options:NSKeyValueObservingOptionNew context:kDurationKVOKey];
    [_streamer addObserver:self forKeyPath:@"bufferingRatio" options:NSKeyValueObservingOptionNew context:kBufferingRatioKVOKey];
    
    [_streamer play];
    
    [self _updateBufferingStatus];
    [self _setupHintForStreamer];
}

- (void)_setupHintForStreamer
{
    
    NSUInteger nextIndex = _currentIndex + 1;
    if (nextIndex >= [self.album.items count]) {
        nextIndex = 0;
    }
    
    [DOUAudioStreamer setHintWithAudioFile:[self.album.items objectAtIndex:nextIndex]];
}


- (void)_updateBufferingStatus
{
    double downloadSize =  ((double)[_streamer receivedLength] / 1024 / 1024 );
    double totalSize =  ((double)[_streamer expectedLength] / 1024 / 1024);
    
    _downloadProgress = downloadSize / totalSize;
    
    [_labelMisc setText:[NSString stringWithFormat:@"Received %.2f/%.2f MB (%.2f %%), Speed %.2f MB/s",
                         downloadSize,
                         totalSize,
                         [_streamer bufferingRatio] * 100.0,
                         (double)[_streamer downloadSpeed] / 1024 / 1024]];
    
    if ([_streamer bufferingRatio] >= 1.0) {
        NSLog(@"sha256: %@", [_streamer sha256]);
    }
}

- (void)observeValueForKeyPath:(NSString *)keyPath ofObject:(id)object change:(NSDictionary *)change context:(void *)context
{
    if (context == kStatusKVOKey) {
        [self performSelector:@selector(_updateStatus)
                     onThread:[NSThread mainThread]
                   withObject:nil
                waitUntilDone:NO];
    }
    else if (context == kDurationKVOKey) {
        [self performSelector:@selector(_timerAction:)
                     onThread:[NSThread mainThread]
                   withObject:nil
                waitUntilDone:NO];
    }
    else if (context == kBufferingRatioKVOKey) {
        [self performSelector:@selector(_updateBufferingStatus)
                     onThread:[NSThread mainThread]
                   withObject:nil
                waitUntilDone:NO];
    }
    else {
        [super observeValueForKeyPath:keyPath ofObject:object change:change context:context];
    }
}

- (void)_timerAction:(id)timer
{
    if ([_streamer duration] == 0.0) {
        [_sliderProgress setValue:0.0f animated:NO];
    }
    else {
        [_sliderProgress setValue:[_streamer currentTime] / [_streamer duration] animated:YES];
    }
}

- (void)_updateStatus
{
    switch ([_streamer status]) {
        case DOUAudioStreamerPlaying:{
            [_labelInfo setText:@"playing"];
//            [_buttonPlayPause setTitle:@"Pause" forState:UIControlStateNormal];
            self.layer.speed = 1.0;
            self.layer.beginTime = 0.0;
            CFTimeInterval pausedTime = [self.layer timeOffset];
            CFTimeInterval timeSincePause = [self.layer convertTime:CACurrentMediaTime() fromLayer:nil] - pausedTime;
            self.layer.beginTime = timeSincePause;
            
            break;
        }
            
        case DOUAudioStreamerPaused:
            [_labelInfo setText:@"paused"];
//            [_buttonPlayPause setTitle:@"Play" forState:UIControlStateNormal];
            CFTimeInterval pausedTime = [self.layer convertTime:CACurrentMediaTime() fromLayer:nil];
            self.layer.speed = 0.0;
            self.layer.timeOffset = pausedTime;
            break;
            
        case DOUAudioStreamerIdle:
            [_labelInfo setText:@"idle"];
//            [_buttonPlayPause setTitle:@"Play" forState:UIControlStateNormal];
            break;
            
        case DOUAudioStreamerFinished:
            [_labelInfo setText:@"finished"];
            [self actionNext:nil];
            break;
            
        case DOUAudioStreamerBuffering:
            [_labelInfo setText:@"buffering"];
            break;
            
        case DOUAudioStreamerError:
            [_labelInfo setText:@"error"];
            break;
    }
}


- (void) actionPlayPause:(UIButton *)sender
{

    sender.selected = !sender.isSelected;
    
    if ([_streamer status] == DOUAudioStreamerPaused ||
        [_streamer status] == DOUAudioStreamerIdle) {
        [_streamer play];
    }
    else {
        [_streamer pause];
    }
}

- (void) actionNext:(id)sender
{
    if (++_currentIndex >= [self.album.items count]) {
        _currentIndex = 0;
    }
    
    [self _resetStreamer];
}

- (void) actionLast:(id)sender
{
    if (--_currentIndex >= [self.album.items count]) {
        _currentIndex = 0;
    }
    
    [self _resetStreamer];

}

- (void) actionSliderProgress:(UISlider *) sender
{
    if (sender.value < _downloadProgress) {
        [_streamer setCurrentTime:[_streamer duration] * [sender value]];
    }
}

- (void) actionSliderVolume:(UISlider *) sender
{
    [DOUAudioStreamer setVolume:[sender value]];
}


@end
