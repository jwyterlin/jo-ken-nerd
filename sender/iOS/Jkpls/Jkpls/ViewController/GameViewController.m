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

@interface GameViewController ()

@property (nonatomic, strong) UIAlertController *alertController;

- (void)_addNotificationCenter;

- (void)_changeChromecastImageStatusNotification:(NSNotification *)notification;
- (void)_toggleChromecastButton:(NSNotification *)notification;
- (void)_statusUserChromecastNotification:(NSNotification *)notification;

@end

@implementation GameViewController

#pragma mark - Getter Methods -

- (UIAlertController *)alertController {
    if (!_alertController) {
        _alertController = [UIAlertController alertControllerWithTitle:@"Resultado"
                                                                message:nil
                                                         preferredStyle:UIAlertControllerStyleAlert];
        [_alertController addAction:[UIAlertAction actionWithTitle:@"Cancelar" style:UIAlertActionStyleCancel handler:nil]];
    }
    return _alertController;
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
        [self performSelector:@selector(chromeCastButtonTouched:) withObject:self];
    }
}

- (IBAction)choseOption:(UIButton *)sender {
    [self.activityIndicator startAnimating];
    self.lbResultGame.text = @"";
    [self.currentGameMode startGameWithChoice:[NSString stringWithFormat:@"%li",(long)sender.tag]];
    if ([self.currentGameMode resultGame].length) {
        self.alertController.message = [self.currentGameMode resultGame];
        [self presentViewController:self.alertController animated:YES completion:nil];
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
