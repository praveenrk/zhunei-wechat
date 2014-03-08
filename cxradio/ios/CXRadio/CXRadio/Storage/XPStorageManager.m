//
//  XPStorageManager.m
//  XPBroadcast
//
//  Created by tashigaofei on 13-11-21.
//  Copyright (c) 2013年 ZhaoYanJun. All rights reserved.
//

#import "XPStorageManager.h"

@implementation XPStorageManager

+(XPStorageManager *) sharedInstance;
{
    static XPStorageManager *instance;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        instance = [[XPStorageManager alloc] init];
    });
    return instance;
}

- (id)init
{
    self = [super init];
    if (self) {
        LogInfo(@"%@", [self getAudioListStoragePath]);
    }
    return self;
}

-(NSArray *) getAudioList;
{
    NSArray *data = [NSKeyedUnarchiver unarchiveObjectWithFile:[self getAudioListStoragePath]];
    NSAssert(data == nil || [data isKindOfClass:[NSArray class]], @"error");
    
    return data;
}

-(void) storeAudioList:(NSMutableArray *) audioList;
{
    BOOL success = [NSKeyedArchiver archiveRootObject:audioList toFile:[self getAudioListStoragePath]];
    NSAssert(success, @"error");
    success = YES;
}

-(NSString *) getAudioListStoragePath;
{
    NSString *appPath = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES)[0];
    return [NSString stringWithFormat:@"%@/XPAudioList.NSArray", appPath];
}

@end
