//
//  ViewController.m
//  Jkpls
//
//  Created by Jhonathan Wyterlin on 05/07/14.
//  Copyright (c) 2014 Jhonathan Wyterlin. All rights reserved.
//

#import "GameViewController.h"

#import "ChromeCast.h"
#import "SinglePlayerGame.h"
#import "ChromeCastGame.h"
#import "BattleResultView.h"
#import "GameLogic.h"


@interface GameViewController ()

@property (nonatomic, strong) BattleResultView *resultView;

- (void)_addNotificationCenter;
- (void)_changeChromecastImageStatusNotification:(NSNotification *)notification;
- (void)_toggleChromecastButton:(NSNotification *)notification;
- (void)_statusUserChromecastNotification:(NSNotification *)notification;

@end

@implementation GameViewController

#pragma mark - Getter Methods -

- (BattleResultView *)resultView {
    if (!_resultView) {
        _resultView = [[BattleResultView alloc] init];
        _resultView.center = self.view.center;
        [self.view addSubview:_resultView];
    }
    return _resultView;
}

#pragma mark - Private Methods -

- (void)_addNotificationCenter {
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(_toggleChromecastButton:)
                                                 name:TOGGLE_CHROMECAST_BUTTON_NOTIFICATION
                                               object:nil];
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(_changeChromecastImageStatusNotification:)
                                                 name:CHANGE_CHROMECAST_IMAGE_STATUS_NOTIFICATION
                                               object:nil];
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(_statusUserChromecastNotification:)
                                                 name:STATUS_USER_CHROMECAST_NOTIFICATION
                                               object:nil];
}

#pragma mark - View Lifecycle -

- (void)viewDidLoad {
    [super viewDidLoad];
    [self.currentGameMode initialize];
    [self _addNotificationCenter];
}

- (void)viewDidDisappear:(BOOL)animated {
    [super viewDidDisappear:animated];
    self.chromeCastButton.hidden = YES;
    [[NSNotificationCenter defaultCenter] removeObserver:self];
}

#pragma mark - IBAction Methods -

- (IBAction)chromeCastTouched:(id)sender {
    self.lbResultGame.text = @"";
    [self.activityIndicator stopAnimating];
    
    if ([self.currentGameMode respondsToSelector:@selector(chromeCastButtonTouched:)]) {
        if ([self.currentGameMode isKindOfClass:[ChromecastGame class]]) {
            ChromecastGame *multiplayerMode = (ChromecastGame *)self.currentGameMode;
            [multiplayerMode chromeCastButtonTouched:sender];
        }
    }
}

- (IBAction)choseOption:(UIButton *)sender {
    [self.activityIndicator startAnimating];
    self.lbResultGame.text = @"";
    [self.currentGameMode startGameWithChoice:[NSString stringWithFormat:@"%li",(long)sender.tag]];
    if ([self.currentGameMode resultGame].length) {
        
        self.resultView.titleLabel.text = [self.currentGameMode titleResultGame];
        self.resultView.descriptionLabel.text = [self.currentGameMode messageResultGame];
        self.resultView.myPlayer.image = [UIImage imageNamed:[NSString stringWithFormat:@"%@", [GameLogic realNameOfChoice:self.currentGameMode.myChoice]]];
        
        self.resultView.OtherPlayer.image = [UIImage imageNamed:[NSString stringWithFormat:@"%@", [GameLogic realNameOfChoice:self.currentGameMode.otherChoice]]];
        
        [UIView animateWithDuration:0.5 animations:^{
            self.resultView.alpha = 1.0f;
        }];
    }
    [self.activityIndicator stopAnimating];
}

#pragma mark - NSNotification Methods -

- (void)_changeChromecastImageStatusNotification:(NSNotification *)notification {
    if ([notification.object isKindOfClass:[UIImage class]]) {
        UIImage *image = (UIImage *)notification.object;
        [self.chromeCastButton setImage:image forState:UIControlStateNormal];
        [self.chromeCastButton setImage:image forState:UIControlStateSelected];
        [self.chromeCastButton setImage:image forState:UIControlStateHighlighted];
    }
}

- (void)_toggleChromecastButton:(NSNotification *)notification {
    self.chromeCastButton.hidden = NO;
}

- (void)_statusUserChromecastNotification:(NSNotification *)notification {
    if ([notification.object isKindOfClass:[NSString class]]) {
        NSString *statusUser = (NSString *)notification.object;
        self.lbResultGame.text = statusUser;
    }
}

@end
