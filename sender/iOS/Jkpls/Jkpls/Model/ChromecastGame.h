//
//  ChromecastGame.h
//  Jkpls
//
//  Created by Fábio Nogueira  on 03/10/14.
//  Copyright (c) 2014 Little Red Club. All rights reserved.
//

#import "Game.h"
#import "ChromeCast.h"

#define TOGGLE_CHROMECAST_BUTTON_NOTIFICATION @"toggleChromecastButtonNotification"
#define CHANGE_CHROMECAST_IMAGE_STATUS_NOTIFICATION @"changeChromecastImageStatusNotification"
#define STATUS_USER_CHROMECAST_NOTIFICATION @"statusUserChromecastNotification"

@interface ChromecastGame : Game <ChromeCastDelegate>

@property (nonatomic, strong) NSTimer *timer;
@property (nonatomic) int counter;

@property (nonatomic, strong) ChromeCast *chromeCast;

- (void)chromeCastButtonTouched:(id)sender;

@end
