//
//  XPHttpClient.m
//  XPStocks
//
//  Created by tashigaofei on 13-9-25.
//  Copyright (c) 2013年 Baidu. All rights reserved.
//

#import "XPHttpClient.h"

#import "AFJSONRequestOperation.h"
#import "AFNetworkActivityIndicatorManager.h"

@implementation XPHttpClient

#pragma  mark Initialization

+ (XPHttpClient *)sharedInstance;
{
    static XPHttpClient * sharedInstance;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        sharedInstance = [[XPHttpClient alloc] initWithBaseURL:[NSURL URLWithString:@""]];
    });
    
    return sharedInstance;
}


- (id)initWithBaseURL:(NSURL *)url {
    self = [super initWithBaseURL:url];
    if (!self) {
        return nil;
    }
    
//    [self registerHTTPOperationClass:[AFJSONRequestOperation class]];
//    [AFJSONRequestOperation addAcceptableContentTypes:[NSSet setWithObject:@"application/x-javascript"]];
    
    // Accept HTTP Header; see http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.1
	[self setDefaultHeader:@"Accept" value:@"application/json"];
    
    if ([[url scheme] isEqualToString:@"https"] && [[url host] isEqualToString:@"alpha-api.app.net"]) {
        self.defaultSSLPinningMode = AFSSLPinningModePublicKey;
    } else {
        self.defaultSSLPinningMode = AFSSLPinningModeNone;
    }
    
    return self;
}


-(void) setShowNetworkActivity:(BOOL)showNetworkActivity
{
    _showNetworkActivity = showNetworkActivity;
//    [[AFNetworkActivityIndicatorManager sharedManager] setEnabled:_showNetworkActivity];
   
}



@end
