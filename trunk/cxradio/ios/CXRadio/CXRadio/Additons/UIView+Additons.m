//
//  UIView+Additons.m
//  BaiduVideo-iPad
//
//  Created by tashigaofei on 14-1-24.
//  Copyright (c) 2014年 Baidu. All rights reserved.
//

#import "UIView+Additons.h"

@implementation UIView (Additons)

+(id) getRootView:(UIView *) view rootViewClass:(Class ) viewClass;
{
    if (view == nil || [view isKindOfClass:viewClass]) {
        return view;
    }else{
        return [UIView getRootView:view.superview rootViewClass:viewClass];
    }
}

@end
