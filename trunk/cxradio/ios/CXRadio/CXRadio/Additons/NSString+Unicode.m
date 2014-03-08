//
//  NSString+Unicode.m
//  BaiduVideo-iPad
//
//  Created by tashigaofei on 13-12-30.
//  Copyright (c) 2013年 Baidu. All rights reserved.
//

#import "NSString+Unicode.h"

@implementation NSString (Unicode)

-(NSString *) hexToUnicodeString;
{
    NSString *convertedString = [self mutableCopy];
    CFStringRef transform = CFSTR("Any-Hex/C");
    CFStringTransform((__bridge CFMutableStringRef)convertedString, NULL, transform, YES);

    return convertedString;
}

@end


@implementation NSArray (Unicode)

- (NSString *)descriptionWithLocale:(id)locale indent:(NSUInteger)level
{
    return [NSString stringWithFormat:@"[%@]", [self componentsJoinedByString:@","]];
}

@end


@implementation NSDictionary (Unicode)

- (NSString *)descriptionWithLocale:(id)locale indent:(NSUInteger)level
{
    NSMutableString *objectDescription = [NSMutableString string];
    
    for (NSString *key in self.allKeys) {
        [objectDescription appendFormat:@"%@ : %@\n", key, self[key]];
    }
    return [NSString stringWithFormat:@"{%@}", objectDescription];
}

@end

