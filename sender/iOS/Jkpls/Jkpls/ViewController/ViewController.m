//
//  ViewController.m
//  Jkpls
//
//  Created by Jhonathan Wyterlin on 05/07/14.
//  Copyright (c) 2014 Jhonathan Wyterlin. All rights reserved.
//

#import "ViewController.h"
#import "TutorialViewController.h"

@interface ViewController ()

@end

@implementation ViewController

#pragma mark - View Lifecycle

- (void)viewDidLoad {
    [super viewDidLoad];
}

#pragma mark - IBAction Methods

-(IBAction)showTutorial:(id)sender {
    
    TutorialViewController *tutorialVC = [[TutorialViewController alloc] initWithNibName:@"TutorialViewController" bundle:nil];
    
    [self.navigationController pushViewController:tutorialVC animated:YES];
    
}

@end
