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

@interface ViewController ()

@property (nonatomic, strong) ChromeCast *chromeCast;

@end

@implementation ViewController

#pragma mark - Getter Methods -

-(ChromeCast *)chromeCast {
    
    if (!_chromeCast) {
        _chromeCast = [[ChromeCast alloc] init];
    }
    
    return _chromeCast;
    
}

#pragma mark - View Lifecycle

-(void)viewDidLoad {
    [super viewDidLoad];
    [self.chromeCast startScanner];
    
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

@end
