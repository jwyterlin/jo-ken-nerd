//
//  ViewController.m
//  Jkpls
//
//  Created by Jhonathan Wyterlin on 05/07/14.
//  Copyright (c) 2014 Jhonathan Wyterlin. All rights reserved.
//

#import "ViewController.h"
#import "TutorialViewController.h"
#import "ChromeCastViewController.h"

@interface ViewController ()

@property (nonatomic, strong) ChromeCastViewController *chromeCastViewController;

@end

@implementation ViewController

#pragma mark - Getter Methods -

-(ChromeCastViewController *)chromeCastViewController {
    
    if ( ! _chromeCastViewController ) {
        _chromeCastViewController = [[ChromeCastViewController alloc] init];
    }
    
    return _chromeCastViewController;
    
}

#pragma mark - View Lifecycle

-(void)viewDidLoad {
    
    [super viewDidLoad];
    
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(updateListDevices:) name:kUpdatedListDevices object:nil];
    
}

#pragma mark - IBAction Methods

-(IBAction)showTutorial:(id)sender {
    
    TutorialViewController *tutorialVC = [[TutorialViewController alloc] initWithNibName:kTutorialViewController bundle:nil];
    
    [self.navigationController pushViewController:tutorialVC animated:YES];
    
}

-(IBAction)chromeCastTouched:(id)sender {
    
    [self presentViewController:self.chromeCastViewController animated:YES completion:nil];
    
}

#pragma mark - Helper Methods

-(void)updateListDevices:(NSNotification *)notification {
    
//    [ChromeCast showActionSheetOnView:self.view];
    
}

@end
