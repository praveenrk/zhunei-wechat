//
//  XPShow.h
//  XPBroadcast
//
//  Created by tashigaofei on 13-11-21.
//  Copyright (c) 2013年 ZhaoYanJun. All rights reserved.
//

#import "XPModel.h"
#import "DOUAudioStreamer+Options.h"

@interface XPTrack : XPModel<DOUAudioFile>

@property (nonatomic, strong) NSString * title;
@property (nonatomic, strong) NSString * src;

@end
