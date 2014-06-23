//
//  CRChannelModel.h
//  cxradio
//
//  Created by Peter on 14-5-11.
//  Copyright (c) 2014年 CathAssist. All rights reserved.
//

#import "CRTrackModel.h"

@interface CRChannelModel : NSObject

- (id)initWithDictionary:(NSDictionary*) dict;
- (CRTrackModel*)currentTrack;
- (CRTrackModel*)firstTrack;
- (CRTrackModel*)nextTrack;
- (CRTrackModel*)prevTrack;
- (CRTrackModel*)setTrackWithIndex:(NSInteger) index;


@property (strong,nonatomic) NSString* title;
@property (strong,nonatomic) NSString* date;
@property (strong,nonatomic) NSString* logo;

@property (strong,nonatomic) NSMutableArray* tracks;

@end
