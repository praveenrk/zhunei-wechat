//
//  XPHttpClient+ShowList.m
//  XPBroadcast
//
//  Created by tashigaofei on 13-11-21.
//  Copyright (c) 2013年 ZhaoYanJun. All rights reserved.
//

#import "XPHttpClient+Album.h"

static const NSString * const url = @"http://cathassist.org/radio/getradio.php?channel=cx";

@implementation XPHttpClient (Album)

-(void) getAudioListWithDateString:(NSString *) dateString CompletionBlock:(void (^)(CXAlbum *album, NSError* error)) block;
{
    NSString *apiUrl = [NSString stringWithFormat:@"%@&date=%@",url, dateString];
    
    LogDebug(@"get Api : %@", apiUrl);
    
    [self getPath:apiUrl parameters:nil success:^(AFHTTPRequestOperation *operation, NSData *responseObject) {
        if (responseObject == nil) {
            block(nil, [NSError errorWithDomain:@"responseObject is nil" code:0 userInfo:@{@"op": operation}]);
            return;
        }
        
        LogDebug(@"%@", [[NSString alloc] initWithData:responseObject encoding:NSUTF8StringEncoding]);
        
        CXAlbum *album = [[CXAlbum alloc] initWithDictionary:[responseObject objectFromJSONData]];
        
        if(block){
            block(album, nil);
        }
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        NSAssert(error != nil, @"error");
        NSLog(@"%@", error);
        block(nil, error);
    }];
    
}

@end
