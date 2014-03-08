//
//  XPPlayerView.h
//  XPBroadcast
//
//  Created by tashigaofei on 13-11-26.
//  Copyright (c) 2013年 ZhaoYanJun. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "DOUAudioStreamer.h"

@interface XPPlayerView : UIView
@property (nonatomic, strong) NSArray *tracks;

-(void) playWithTracks:(NSArray *) tracks;

@end
