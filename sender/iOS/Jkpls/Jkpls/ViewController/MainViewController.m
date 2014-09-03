//
//  MainViewController.m
//  Jkpls
//
//  Created by Jhonathan Wyterlin on 9/3/14.
//  Copyright (c) 2014 Little Red Club. All rights reserved.
//

#import "MainViewController.h"
#import <QuartzCore/QuartzCore.h>

@interface MainViewController ()

@end

@implementation MainViewController

@synthesize lbSinglePlayer = _lbSinglePlayer;
@synthesize lbMultiPlayer = _lbMultiPlayer;
@synthesize btnPlayerVsCom = _btnPlayerVsCom;
@synthesize btnChromeCast = _btnChromeCast;
@synthesize btnWifi = _btnWifi;
@synthesize btnBluetooth = _btnBluetooth;

-(id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil {
    
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    
    if (self) {
    }
    
    return self;
    
}

-(void)viewDidLoad {
    
    [super viewDidLoad];
    
    [self roundingButton:self.btnPlayerVsCom];
    [self roundingButton:self.btnChromeCast];
    [self roundingButton:self.btnWifi];
    [self roundingButton:self.btnBluetooth];

}

-(void)didReceiveMemoryWarning {
    
    [super didReceiveMemoryWarning];

}

#pragma mark - Helper Methods

-(void)roundingButton:(UIButton *)button {
    
    button.layer.cornerRadius = 5.0;
    
}

@end
