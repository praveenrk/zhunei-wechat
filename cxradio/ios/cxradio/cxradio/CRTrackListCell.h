//
//  CRTrackListCell.h
//  cxradio
//
//  Created by Peter on 14-5-13.
//  Copyright (c) 2014年 CathAssist. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "FFCircularProgressView.h"
#import "Model/CRTrackModel.h"

@interface CRTrackListCell : UITableViewCell

@property (weak, nonatomic) IBOutlet UILabel *labelTitle;

@property (weak, nonatomic) IBOutlet FFCircularProgressView *progressView;


- (void)setTrackModel:(CRTrackModel*)track;

@end
