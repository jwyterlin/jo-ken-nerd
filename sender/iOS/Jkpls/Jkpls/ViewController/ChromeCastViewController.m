//
//  ChromeCastViewController.m
//  Jkpls
//
//  Created by Fábio Nogueira  on 05/07/14.
//  Copyright (c) 2014 Jhonathan Wyterlin. All rights reserved.
//

#import "ChromeCastViewController.h"

@interface ChromeCastViewController ()

@property (nonatomic, strong) GCKDeviceScanner *deviceScanner;
@property (nonatomic, strong) NSMutableArray *devices;

- (void)_scanCasts;

@end

@implementation ChromeCastViewController

@synthesize tableView = _tableView;

#pragma mark - Getter Methods -

-(GCKDeviceScanner *)deviceScanner {

    if ( ! _deviceScanner ) {
        _deviceScanner = [[GCKDeviceScanner alloc] init];
    }
    
    return _deviceScanner;
    
}

-(void)viewDidLoad {
    
    [super viewDidLoad];
    
    [self _scanCasts];
    
    UIActionSheet *sheet = [[UIActionSheet alloc] initWithTitle:@"Chrome Cast"
                                                       delegate:self
                                              cancelButtonTitle:@"Cancelar"
                                         destructiveButtonTitle:nil
                                              otherButtonTitles:nil, nil];
    
    for ( GCKDevice *device in self.deviceScanner.devices ) {
        [sheet addButtonWithTitle:device.friendlyName];
        //[self.devices addObject:device];
    }
    
}

#pragma Mark - Private Methods -

- (void)_scanCasts {
    
    //Initialize device scanner
    [self.deviceScanner addListener:self];
    [self.deviceScanner startScan];
    
}

#pragma mark - GCKDeviceScannerListener -

- (void)deviceDidComeOnline:(GCKDevice *)device {
    [self dismissViewControllerAnimated:YES  completion:nil];
}

- (void)deviceDidGoOffline:(GCKDevice *)device {
    NSLog(@"device disappeared!!!");
}

@end
