//
//  CRChannelViewController.h
//  cxradio
//
//  Created by Peter on 14-5-10.
//  Copyright (c) 2014年 CathAssist. All rights reserved.
//

#import "AMSlideMenuMainViewController.h"
#import "Model/CRChannelModel.h"

@protocol CRTrackDelegate
- (void)trackChanged;
@end

@interface CRChannelVC : AMSlideMenuMainViewController

@property(nonatomic, retain) NSObject<CRTrackDelegate>* delegateTrack;

- (void)setCurrentChannel:(CRChannelModel*) channel;

@end
