//
//  CRAudioListVC.h
//  cxradio
//
//  Created by Peter on 14-5-10.
//  Copyright (c) 2014年 CathAssist. All rights reserved.
//

#import "AMSlideMenuRightTableViewController.h"
#import "Model/CRChannelModel.h"

@interface CRTrackListVC : AMSlideMenuRightTableViewController

- (void) resetChannel:(CRChannelModel*)channel;

@end
