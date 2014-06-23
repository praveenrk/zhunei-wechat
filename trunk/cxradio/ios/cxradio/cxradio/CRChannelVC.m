//
//  CRChannelViewController.m
//  cxradio
//
//  Created by Peter on 14-5-10.
//  Copyright (c) 2014年 CathAssist. All rights reserved.
//

#import "CRChannelVC.h"
#import "CRTrackListVC.h"

@interface CRChannelVC ()

@end

@implementation CRChannelVC


- (void)viewDidLoad
{
    [super viewDidLoad];
    // Do any additional setup after loading the view.
}


-(NSString *)segueIdentifierForIndexPathInRightMenu:(NSIndexPath *)indexPath
{
    NSString* identifier = @"playSegue";
    
    return identifier;
}

- (void)configureRightMenuButton:(UIButton *)button
{
    CGRect frame = button.frame;
    frame.origin = (CGPoint){0,0};
    frame.size = (CGSize){24,24};
    button.frame = frame;
    
    [button setBackgroundImage:[UIImage imageNamed:@"MenuIcon"] forState:UIControlStateNormal];
}

- (CGFloat)rightMenuWidth
{
    return 200;
}
/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

- (void)setCurrentChannel:(CRChannelModel*) channel
{
    CRTrackListVC* tlVC = (CRTrackListVC*)self.rightMenu;
    [tlVC resetChannel:channel];
}

@end
