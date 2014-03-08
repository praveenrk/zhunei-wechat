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

@property (nonatomic, strong) NSString * audioID;
@property (nonatomic, strong) NSString * catID;
@property (nonatomic, strong) NSString * catName;
@property (nonatomic, strong) NSString * title;
@property (nonatomic, strong) NSString * showDescription;
@property (nonatomic, strong) NSString * audioURL;
@property (nonatomic, strong) NSString * photoUrl;
@property (nonatomic, strong) NSString * pubDate;

@end
