//
//  NSObject+iOS7Compatibility.m
//  BaiduVideo-iPad
//
//  Created by tashigaofei on 14-1-14.
//  Copyright (c) 2014年 Baidu. All rights reserved.
//

#import "NSObject+iOS7Compatibility.h"

@implementation NSObject (iOS7Compatibility)

@end


@implementation UIViewController(Layout)

-(int) edgesForExtendedLayout;
{
    return 0;
}

-(BOOL) extendedLayoutIncludesOpaqueBars;
{
    return NO;
}


-(BOOL) automaticallyAdjustsScrollViewInsets;
{
    return NO;
}

@end


@implementation UIView(Layout)



@end

