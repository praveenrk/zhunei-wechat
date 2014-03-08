//
//  XPAudioListTable.h
//  XPBroadcast
//
//  Created by tashigaofei on 13-11-22.
//  Copyright (c) 2013年 ZhaoYanJun. All rights reserved.
//

#import <UIKit/UIKit.h>

@class XPAudioListTable;
@protocol XPAudioListTableDelegate <NSObject>
-(void) audioListTable:(XPAudioListTable *) table didSelectAudio:(XPTrack *) audio;
@end

@interface XPAudioListTable : UITableView

@property (nonatomic, weak) id<XPAudioListTableDelegate> tableDelegate;

-(void) updateWithData:(NSArray*) dataArray;
-(void) updateWithData:(NSArray*) dataArray shouldAppend:(BOOL) append;

@end
