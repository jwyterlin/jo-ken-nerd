//
//  ViewController.h
//  Jkpls
//
//  Created by Jhonathan Wyterlin on 05/07/14.
//  Copyright (c) 2014 Jhonathan Wyterlin. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "ChromeCast.h"
#import "Game.h"

@interface GameViewController : UIViewController<ChromeCastDelegate>

@property (nonatomic, strong) Game *currentGameMode;

@property (nonatomic, strong) ChromeCast *chromeCast;

@property (weak, nonatomic) IBOutlet UIButton *chromeCastTouched;

@property(nonatomic,strong) IBOutlet UILabel *lbResultGame;
@property(nonatomic,strong) IBOutlet UIActivityIndicatorView *activityIndicator;

- (IBAction)chromeCastTouched:(id)sender;
- (IBAction)choseOption:(UIButton *)sender;

@end
