//
//  CXViewController.m
//  CXRadio
//
//  Created by tashigaofei on 14-3-7.
//  Copyright (c) 2014年 zhaoyanjun. All rights reserved.
//

#import "CXViewController.h"

#import "XPHttpClient+Album.h"

@interface CXViewController ()

@end

@implementation CXViewController

- (void)viewDidLoad
{
    [super viewDidLoad];
    
    self.view.backgroundColor = themeBgColor;
    
    [[XPHttpClient sharedInstance] getAudioListWithDateString:nil CompletionBlock:^(CXAlbum *album, NSError *error) {
        
        NSLog(@"%@", album);
    }];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

@end
