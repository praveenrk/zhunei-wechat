//
//  CRChannelModel.m
//  cxradio
//
//  Created by Peter on 14-5-11.
//  Copyright (c) 2014年 CathAssist. All rights reserved.
//

#import "CRChannelModel.h"

@interface CRChannelModel()
{
    NSInteger curTrack;
}

@end

@implementation CRChannelModel

- (id)initWithDictionary:(NSDictionary*) dict
{
    self = [super self];
    if(self)
    {
        NSAssert([dict isKindOfClass:[NSDictionary class]], @"Error dict in channelmodel");
        [self setValuesForKeysWithDictionary:dict];
    }
    
    curTrack = 0;
    return self;
}


- (void)setValue:(id)value forUndefinedKey:(NSString *)key
{
    if ([key isEqualToString:@"items"])
    {
        self.tracks = [NSMutableArray array];
        for (NSDictionary* dict in value)
        {
            CRTrackModel* track = [[CRTrackModel alloc] initWithDictionary:dict];
            [self.tracks addObject:track];
        }
        
        return;
    }
    
    [super setValue:value forKey:key];
}


- (CRTrackModel*)currentTrack;
{
    if(curTrack < self.tracks.count)
    {
        return [self.tracks objectAtIndex:curTrack];
    }
    return nil;
}

- (CRTrackModel*)firstTrack
{
    curTrack = 0;
    if(curTrack<self.tracks.count)
    {
        return [self.tracks objectAtIndex:curTrack];
    }
    return nil;
}

- (CRTrackModel*)nextTrack
{
    if(curTrack < self.tracks.count)
    {
        curTrack++;
        if(curTrack < self.tracks.count)
        {
            return [self.tracks objectAtIndex:curTrack];
        }
    }
    
    return nil;
}

- (CRTrackModel*)prevTrack
{
    curTrack--;
    if(curTrack < self.tracks.count)
    {
        return [self.tracks objectAtIndex:curTrack];
    }
    
    return nil;
}

- (CRTrackModel*)setTrackWithIndex:(NSInteger) index
{
    if(index < self.tracks.count)
    {
        curTrack = index;
        return [self.tracks objectAtIndex:curTrack];
    }
    else
    {
        curTrack = 0;
    }
    
    return nil;
}

@end
