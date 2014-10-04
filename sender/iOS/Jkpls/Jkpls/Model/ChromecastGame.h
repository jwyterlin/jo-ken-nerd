//
//  ChromecastGame.h
//  Jkpls
//
//  Created by Fábio Nogueira  on 03/10/14.
//  Copyright (c) 2014 Little Red Club. All rights reserved.
//

#import "Game.h"
#import "ChromeCast.h"

@interface ChromecastGame : Game <ChromeCastDelegate>

@property (nonatomic, strong) NSTimer *timer;
@property (nonatomic) int counter;

@property (nonatomic, strong) ChromeCast *chromeCast;

- (void)chromeCastButtonTouched:(id)sender;

@end
