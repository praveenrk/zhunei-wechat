//
//  CXAlbum.m
//  CXRadio
//
//  Created by tashigaofei on 14-3-7.
//  Copyright (c) 2014年 zhaoyanjun. All rights reserved.
//

#import "CXAlbum.h"

@implementation CXAlbum

- (void)setValue:(id)value forKey:(NSString *)key
{
    if ([key isEqualToString:@"items"]) {
        self.items = [NSMutableArray array];
        for (NSDictionary *dict in value){
            XPTrack *track = [[XPTrack alloc] initWithDictionary:dict];
            [self.items addObject:track];
        }
        
        return;
    }
    
    [super setValue:value forKey:key];
    
}


//===========================================================
//  Keyed Archiving
//
//===========================================================
- (void)encodeWithCoder:(NSCoder *)encoder
{
    [encoder encodeObject:self.title forKey:@"title"];
    [encoder encodeObject:self.date forKey:@"date"];
    [encoder encodeObject:self.logo forKey:@"logo"];
    [encoder encodeObject:self.items forKey:@"items"];
}

- (id)initWithCoder:(NSCoder *)decoder
{
    self = [super init];
    if (self) {
        self.title = [decoder decodeObjectForKey:@"title"];
        self.date = [decoder decodeObjectForKey:@"date"];
        self.logo = [decoder decodeObjectForKey:@"logo"];
        self.items = [decoder decodeObjectForKey:@"items"];
    }
    return self;
}
//===========================================================
// - (NSArray *)keyPaths
//
//===========================================================
- (NSArray *)keyPaths
{
    NSArray *result = [NSArray arrayWithObjects:
                       @"title",
                       @"date",
                       @"logo",
                       @"items",
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
