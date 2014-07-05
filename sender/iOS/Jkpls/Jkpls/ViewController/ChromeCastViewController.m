//
//  ChromeCastViewController.m
//  Jkpls
//
//  Created by Fábio Nogueira  on 05/07/14.
//  Copyright (c) 2014 Jhonathan Wyterlin. All rights reserved.
//

#import "GoogleCast.h"
#import "ChromeCastViewController.h"

@interface ChromeCastViewController ()

@property (nonatomic, strong) GCKDeviceScanner *deviceScanner;

- (void)_scanCasts;

@end

@implementation ChromeCastViewController

#pragma mark - Getter Methods -

- (GCKDeviceScanner *)deviceScanner {
    if (!_deviceScanner) {
        _deviceScanner = [[GCKDeviceScanner alloc] init];
    }
    return _deviceScanner;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    [self _scanCasts];
}

#pragma Mark - Private Methods -

- (void)_scanCasts {
    //Initialize device scanner
    [self.deviceScanner addListener:self];
    [self.deviceScanner startScan];
}

@end
