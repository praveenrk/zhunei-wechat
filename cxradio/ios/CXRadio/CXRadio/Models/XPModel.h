//
//  XPModel.h
//  XPStocks
//
//  Created by tashigaofei on 13-9-25.
//  Copyright (c) 2013年 Baidu. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface XPModel : NSObject

- (id)initWithDictionary:(NSDictionary *) dic;
-(NSArray *)keyPaths;

@end
