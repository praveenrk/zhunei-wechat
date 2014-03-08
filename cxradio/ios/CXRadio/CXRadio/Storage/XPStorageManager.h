//
//  XPStorageManager.h
//  XPBroadcast
//
//  Created by tashigaofei on 13-11-21.
//  Copyright (c) 2013年 ZhaoYanJun. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface XPStorageManager : NSObject

+(XPStorageManager *) sharedInstance;
-(NSArray *) getAudioList;
-(void) storeAudioList:(NSMutableArray *) audioList;


@end
