//
//  XPAudioListTable.m
//  XPBroadcast
//
//  Created by tashigaofei on 13-11-22.
//  Copyright (c) 2013年 ZhaoYanJun. All rights reserved.
//

#import "XPAudioListTable.h"

#import "XPAudioListCell.h"

@interface XPAudioListTable()<UITableViewDelegate, UITableViewDataSource>

@property (nonatomic, strong) NSMutableArray * tableDataSource;

@end


@implementation XPAudioListTable

- (id)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        self.dataSource = self;
        self.delegate = self;
        self.backgroundColor = themeBgColor;
    }
    return self;
}


-(void) updateWithData:(NSArray*) dataArray;
{
    [self updateWithData:dataArray shouldAppend:YES];
}

-(void) updateWithData:(NSArray*) dataArray shouldAppend:(BOOL) append;
{
    if (_tableDataSource == nil) {
        _tableDataSource = [NSMutableArray new];
        [self updateWithData:dataArray shouldAppend:YES];
    }
    
    if (append) {
        [_tableDataSource addObjectsFromArray:dataArray];
    }else{
        for (XPTrack *audio in dataArray) {
            if ([_tableDataSource containsObject:audio] == NO) {
                [_tableDataSource insertObject:audio atIndex:0];
            }
        }
    }
    
    [self reloadData];
}

//-(NSArray *) sortAudio:(NSArray *) dataArray;
//{
//    return [dataArray sortedArrayUsingComparator:^NSComparisonResult(XPTrack *obj1, XPTrack *obj2) {
//        return obj1.audioID.intValue < obj2.audioID.intValue;
//    }];
//}

#pragma mark UITableViewDelegate Methods

- (NSInteger) tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    
    return [_tableDataSource count];
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath;
{
    return 44;
}

- (UITableViewCell *) tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    static NSString *identifier = @"UITableViewCell";
    XPAudioListCell *cell = [tableView dequeueReusableCellWithIdentifier:identifier];
    if (!cell) {
        cell = [[XPAudioListCell alloc] initWithStyle:UITableViewCellStyleSubtitle reuseIdentifier:identifier];
    }
    
    XPTrack *audio = _tableDataSource[indexPath.row];
    cell.textLabel.text = audio.title;
    cell.textLabel.font = [UIFont boldSystemFontOfSize:14.0];
    cell.textLabel.textColor = [UIColor colorWithWhite:0.2 alpha:0.9];
//    if ([audio.photoUrl length]) {
//        [cell.imageView setImageWithURL:[NSURL URLWithString:audio.photoUrl] placeholderImage:nil];
//    }else{
//        [cell.imageView setImage:nil];
//    }
    
    return cell;
}


- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath;
{
    [self deselectRowAtIndexPath:indexPath animated:YES];
    if ([_tableDelegate respondsToSelector:@selector(audioListTable:didSelectAudio:)]) {
        [_tableDelegate audioListTable:self didSelectAudio:_tableDataSource[indexPath.row]];
    }
}

@end
