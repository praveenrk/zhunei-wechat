//
//  XPHttpClient+ShowList.m
//  XPBroadcast
//
//  Created by tashigaofei on 13-11-21.
//  Copyright (c) 2013年 ZhaoYanJun. All rights reserved.
//

#import "XPHttpClient+AudioList.h"

static const NSString * const url = @"http://app.21sq.org/radio/dongwuLists?catid=631";

@implementation XPHttpClient (AudioList)

-(void) getAudioListWithPage:(int) page CompletionBlock:(void (^)(NSArray *audioArray, NSError* error)) block;
{
    NSString *apiUrl = [NSString stringWithFormat:@"%@&page=%d",url,page];
    
    LogDebug(@"get Api : %@", apiUrl);
    
    [self getPath:apiUrl parameters:nil success:^(AFHTTPRequestOperation *operation, NSData *responseObject) {
        if (responseObject == nil) {
            block(nil, [NSError errorWithDomain:@"responseObject is nil" code:0 userInfo:@{@"op": operation}]);
            return;
        }
        
        NSString *stringData = [[NSString alloc] initWithData:responseObject encoding:NSUTF8StringEncoding];
        NSArray *jsonObjectArray = [stringData objectFromJSONString][@"audioList"];
        NSMutableArray *audioArray = [NSMutableArray array];
        for (NSDictionary *dic in jsonObjectArray) {
            XPTrack *audio = [[XPTrack alloc] initWithDictionary:dic];
            [audioArray addObject:audio];
        }
        
        block(audioArray, nil);
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        NSAssert(error != nil, @"error");
        NSLog(@"%@", error);
        block(nil, error);
    }];

}
@end
