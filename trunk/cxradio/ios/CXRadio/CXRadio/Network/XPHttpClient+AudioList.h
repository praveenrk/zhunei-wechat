//
//  XPHttpClient+ShowList.h
//  XPBroadcast
//
//  Created by tashigaofei on 13-11-21.
//  Copyright (c) 2013年 ZhaoYanJun. All rights reserved.
//

#import "XPHttpClient.h"

@interface XPHttpClient (AudioList)

-(void) getAudioListWithPage:(int) page CompletionBlock:(void (^)(NSArray *audioArray, NSError* error)) block;
@end
