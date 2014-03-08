//
//  CXAlbum.h
//  CXRadio
//
//  Created by tashigaofei on 14-3-7.
//  Copyright (c) 2014年 zhaoyanjun. All rights reserved.
//

#import "XPModel.h"

@interface CXAlbum : XPModel

@property (nonatomic, strong) NSString * title;
@property (nonatomic, strong) NSString * date;
@property (nonatomic, strong) NSString * logo;
@property (nonatomic, strong) NSMutableArray * items;

@end
