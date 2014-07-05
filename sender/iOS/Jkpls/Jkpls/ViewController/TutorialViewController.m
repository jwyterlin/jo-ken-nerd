//
//  TutorialViewController.m
//  Jkpls
//
//  Created by Jhonathan Wyterlin on 05/07/14.
//  Copyright (c) 2014 Jhonathan Wyterlin. All rights reserved.
//

#import "TutorialViewController.h"

@interface TutorialViewController ()

@end

@implementation TutorialViewController

@synthesize tutorialImage = _tutorialImage;

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil {
    
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    
    if ( self ) {
        // Custom initialization
    }
    
    return self;
    
}

- (void)viewDidLoad {
    
    [super viewDidLoad];
    
    // Do any additional setup after loading the view from its nib.
    
}

-(void)viewDidAppear:(BOOL)animated {
    
    [self.tutorialImage sizeToFit];
    
}

- (void)didReceiveMemoryWarning {
    
    [super didReceiveMemoryWarning];
    
    // Dispose of any resources that can be recreated.
    
}

@end
