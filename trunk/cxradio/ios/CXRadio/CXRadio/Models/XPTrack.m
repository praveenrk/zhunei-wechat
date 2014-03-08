//
//  XPShow.m
//  XPBroadcast
//
//  Created by tashigaofei on 13-11-21.
//  Copyright (c) 2013年 ZhaoYanJun. All rights reserved.
//

#import "XPTrack.h"

@implementation XPTrack

- (void)setValue:(id)value forKey:(NSString *)key
{
    if ([key isEqualToString:@"description"]) {
        self.showDescription = value;
        return;
    }
    
    if ([key isEqualToString:@"photo"]) {
        self.photoUrl = value;
        return;
    }
    
    [super setValue:value forKey:key];
    
}

- (BOOL)isEqual:(XPTrack *) anObject
{
	if (self == anObject)
		return YES;
	
	if ([anObject isKindOfClass:[XPTrack class]]) {
        return [self.audioID isEqualToString:anObject.audioID];
    }
    
    return NO;
}

- (NSURL *)audioFileURL
{
    return [NSURL URLWithString: [self audioURL]];
}


#pragma  mark  Default

//===========================================================
// - (NSArray *)keyPaths
//
//===========================================================
- (NSArray *)keyPaths
{
    NSArray *result = [NSArray arrayWithObjects:
                       @"audioID",
                       @"catID",
                       @"catName",
                       @"title",
                       @"showDescription",
                       @"audioURL",
                       @"photoUrl",
                       @"pubDate",
                       nil];
    
    return result;
}

//===========================================================
// - (NSString *)descriptionForKeyPaths
//
//===========================================================
- (NSString *)descriptionForKeyPaths
{
    NSMutableString *desc = [NSMutableString string];
    [desc appendString:@"\n\n"];
    [desc appendFormat:@"Class name: %@\n", NSStringFromClass([self class])];
    
    NSArray *keyPathsArray = [self keyPaths];
    for (NSString *keyPath in keyPathsArray) {
        [desc appendFormat: @"%@: %@\n", keyPath, [self valueForKey:keyPath]];
    }
    
    return [NSString stringWithString:desc];
}
- (NSString *)description 
{
    return [self descriptionForKeyPaths]; 
}
@end
