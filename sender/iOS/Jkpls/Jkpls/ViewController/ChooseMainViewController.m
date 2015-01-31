//
//  MainViewController.m
//  Jkpls
//
//  Created by Jhonathan Wyterlin on 9/3/14.
//  Copyright (c) 2014 Little Red Club. All rights reserved.
//

#import "ChooseMainViewController.h"
#import <QuartzCore/QuartzCore.h>

#import "GameViewController.h"
#import "SinglePlayerGame.h"
#import "ChromecastGame.h"

@interface ChooseMainViewController () <UIGestureRecognizerDelegate>

@property (nonatomic, strong) UITapGestureRecognizer *dismissKeyboardGesture;

@property (nonatomic, strong) NSTimer *timerChangeName;

- (void)roundingButton:(UIButton *)button;
//- (void)changeName:(NSTimer *)timer;

- (void)_addGesturesRecognizer;
- (void)_dismissKeyboard;

@end

@implementation ChooseMainViewController

#pragma mark - Getter Methods

- (UIGestureRecognizer *)dismissKeyboardGesture {
    if (!_dismissKeyboardGesture) {
        _dismissKeyboardGesture = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(_dismissKeyboard)];
        _dismissKeyboardGesture.delegate = self;
    }
    return _dismissKeyboardGesture;
}

#pragma mark - UIViewController Methods

- (void)viewDidLoad {
    [super viewDidLoad];
    [self roundingButton:self.btnPlayerVsCom];
}

- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
    [self setTitle:@"Jo Ken Nerd"];
    
    [self _addGesturesRecognizer];
}

#pragma mark - Helper Methods

- (void)roundingButton:(UIButton *)button {
    button.layer.cornerRadius = 5.0;
}

#pragma mark - Private Methods -

//- (void)changeName:(NSTimer *)timer {
////    [self sendNamePlayerToChromeCastWithAction:@"update"];
//}

- (void)_addGesturesRecognizer {
    [self.view addGestureRecognizer:self.dismissKeyboardGesture];
}

- (void)_dismissKeyboard {
    [self.view endEditing:YES];
}

#pragma mark - IBAction Methods -

//- (IBAction)changedName:(UITextField *)textfield {
//    if ( self.timerChangeName != nil ) {
//        [self.timerChangeName invalidate];
//        self.timerChangeName = nil;
//    }
//    
//    self.timerChangeName = [NSTimer scheduledTimerWithTimeInterval:1.0 target:self selector:@selector(changeName:) userInfo:nil repeats:NO];
//}

#pragma mark - UIStoryboardSegue Methods -

- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    GameViewController *gameViewController = (GameViewController *)segue.destinationViewController;
    
    NSString *namePlayer = self.nomeTextField.text;
    if ([namePlayer isEqualToString:@""]) {
        namePlayer = [[UIDevice currentDevice] name];
    }
    if ([[segue identifier] isEqualToString:@"singlePlayerSegue"]) {
        [segue.destinationViewController setTitle:@"Single Player"];
        
        SinglePlayerGame *singlePlayerGame = [[SinglePlayerGame alloc] init];
        singlePlayerGame.playerName = namePlayer;
        gameViewController.currentGameMode = singlePlayerGame;
    } else if ([[segue identifier] isEqualToString:@"chromecastSegue"]) {
        [segue.destinationViewController setTitle:@"ChromeCast"];
        
        ChromecastGame *chromecastGame = [[ChromecastGame alloc] init];
        chromecastGame.playerName = namePlayer;
        gameViewController.currentGameMode = chromecastGame;
    }
    [self setTitle:@""];
}

@end
