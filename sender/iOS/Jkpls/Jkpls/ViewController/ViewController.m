//
//  ViewController.m
//  Jkpls
//
//  Created by Jhonathan Wyterlin on 05/07/14.
//  Copyright (c) 2014 Jhonathan Wyterlin. All rights reserved.
//

#import "ViewController.h"
#import "TutorialViewController.h"
#import "ChromeCast.h"

@interface ViewController () {

    NSTimer *timer;
    int counter;
    
}

@end

@implementation ViewController

#pragma mark - Getter Methods -

#pragma mark - View Lifecycle

-(void)viewDidLoad {
    
    [super viewDidLoad];
    
    self.chromeCast = [ChromeCast new];
    self.chromeCast.delegate = self;
    
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(updateListDevices:) name:kUpdatedListDevices object:nil];
    
}

#pragma mark - IBAction Methods

-(IBAction)showTutorial:(id)sender {
    
    TutorialViewController *tutorialVC = [[TutorialViewController alloc] initWithNibName:kTutorialViewController bundle:nil];
    
    [self.navigationController pushViewController:tutorialVC animated:YES];
}

-(IBAction)chromeCastTouched:(id)sender {
    [self.chromeCast showActionSheetOnView:self.view];
}

#pragma mark - Helper Methods

-(void)updateListDevices:(NSNotification *)notification {
    
//    [ChromeCast showActionSheetOnView:self.view];
    
}

#pragma mark - ChromeCastDelegate Methods

-(void)didStartScanner {
    
    timer = [NSTimer scheduledTimerWithTimeInterval:0.5 target:self selector:@selector(loading:) userInfo:nil repeats:NO];
    
}

-(void)didConnect:(GCKDeviceManager *)device {

    [timer invalidate];
    timer = nil;
    
    [self setImage:[UIImage imageNamed:@"cast_on.png"] onButton:self.chromeCastTouched];
    
    [self showMessage:@"Conexão concluída com sucesso!"];

}

-(void)didFailWithError:(NSError *)error deviceManager:(GCKDeviceManager *)device {
    
    [timer invalidate];
    timer = nil;
    
    [self setImage:[UIImage imageNamed:@"cast_off.png"] onButton:self.chromeCastTouched];
    
    [self showMessage:@"Falha na conexão"];
    
}

#pragma mark - Helper Methods

-(void)loading:(NSTimer *)timer {
    
    counter++;
    
    if ( counter == 1 ) {
        [self setImage:[UIImage imageNamed:@"cast_on0.png"] onButton:self.chromeCastTouched];
    } else if ( counter == 2 ) {
        [self setImage:[UIImage imageNamed:@"cast_on1.png"] onButton:self.chromeCastTouched];
    } else if ( counter == 3 ) {
        [self setImage:[UIImage imageNamed:@"cast_on2.png"] onButton:self.chromeCastTouched];
        counter = 0;
    }
    
}

-(void)setImage:(UIImage *)image onButton:(UIButton *)button {
    
    [button setImage:image forState:UIControlStateNormal];
    [button setImage:image forState:UIControlStateSelected];
    [button setImage:image forState:UIControlStateHighlighted];
    
}

-(void)showMessage:(NSString *)message {
    
    UIAlertView *alert = [[UIAlertView alloc] initWithTitle:message message:nil delegate:nil cancelButtonTitle:nil otherButtonTitles:@"OK", nil];
    
    [alert show];
    
}

@end
