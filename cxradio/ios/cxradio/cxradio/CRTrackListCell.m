//
//  CRTrackListCell.m
//  cxradio
//
//  Created by Peter on 14-5-13.
//  Copyright (c) 2014年 CathAssist. All rights reserved.
//

#import "CRTrackListCell.h"
#import "Model/CRTrackModel.h"

@interface CRTrackListCell()
{
    CRTrackModel* _track;
}

@end

@implementation CRTrackListCell

- (id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier
{
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {
        // Initialization code
        UILabel* title = [[UILabel alloc] initWithFrame:CGRectMake(10, 10, 150, 24)];
        title.font = [UIFont boldSystemFontOfSize:14.0];
        title.textColor = [UIColor colorWithWhite:0.2 alpha:0.9];
        
        FFCircularProgressView* progress = [[FFCircularProgressView alloc] initWithFrame:CGRectMake(165, 10, 24, 24)];
        
        self.labelTitle = title;
        self.progressView = progress;
        
        [self addSubview:title];
        [self addSubview:progress];
    }
    return self;
}

- (void)setTrackModel:(CRTrackModel*)track
{
    _track = track;
    self.labelTitle.text = track.title;
}

- (void)awakeFromNib
{
    // Initialization code
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated
{
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

@end
