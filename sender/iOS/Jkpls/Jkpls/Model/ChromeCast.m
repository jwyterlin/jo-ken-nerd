//
//  ChromeCast.m
//  Jkpls
//
//  Created by Fábio Nogueira  on 05/07/14.
//  Copyright (c) 2014 Jhonathan Wyterlin. All rights reserved.
//

#import "ChromeCast.h"

#define APPID @"YOUR_APP_ID_HERE"

@interface ChromeCast ()

@property (nonatomic, strong) GCKDeviceScanner *deviceScanner;
@property(nonatomic, strong) GCKDeviceManager *deviceManager;

@property (nonatomic, strong) UIActionSheet *actionSheet;
@property(nonatomic,strong) NSMutableArray *devices;

@end

@implementation ChromeCast

#pragma mark - Lifecycle

-(id)init {
    
    self = [super init];
    
    if ( self ) {
        self.devices = [NSMutableArray new];
    }
    
    return self;
    
}

#pragma mark - Getter Methods -

- (GCKDeviceScanner *)deviceScanner {
    if (!_deviceScanner) {
        _deviceScanner = [[GCKDeviceScanner alloc] init];
    }
    return _deviceScanner;
}

- (GCKDeviceManager *)deviceManager {
    if (!_deviceManager) {
        _deviceManager = [[GCKDeviceManager alloc] init];
    }
    return _deviceManager;
}

- (UIActionSheet *)actionSheet {
    if (!_actionSheet) {
        _actionSheet = [[UIActionSheet alloc] initWithTitle:nil
                                                       delegate:self
                                              cancelButtonTitle:nil
                                         destructiveButtonTitle:@"Cancelar"
                                              otherButtonTitles:nil, nil];
    }
    return _actionSheet;
}

#pragma Mark - Private Methods -

- (void)startScanner {
    [self.deviceScanner addListener:self];
    [self.deviceScanner startScan];
}

-(BOOL)listDevicesHasThisDeviceName:(NSString *)name {
    
    for (NSString *element in self.devices ) {
        if ([element isEqualToString:name]) {
            return YES;
        }
    }
    
    return NO;
    
}

#pragma mark - Public Methods - 

- (void)showActionSheetOnView:(UIView *)view {

    int numberOfDevices = self.devices.count;
    
    for( GCKDevice *device in self.deviceScanner.devices ){

        if ( [self listDevicesHasThisDeviceName:device.friendlyName] ) {
            [self.devices addObject:device.friendlyName];
            [self.actionSheet addButtonWithTitle:device.friendlyName];
        }

    }
    
    if ( numberOfDevices != self.devices.count ) {
        [self.actionSheet showInView:view];
    }
    
}

#pragma mark - UIActionSheetDelegate Methods -

- (void)actionSheet:(UIActionSheet *)actionSheet clickedButtonAtIndex:(NSInteger)buttonIndex {
    GCKDevice *selectedDevice = [self.deviceScanner.devices objectAtIndex:buttonIndex];
    
    NSDictionary *info = [[NSBundle mainBundle] infoDictionary];
    self.deviceManager = [[GCKDeviceManager alloc]  initWithDevice:selectedDevice
                                            clientPackageName:[info objectForKey:@"CFBundleIdentifier"]];
    
    self.deviceManager.delegate = self;
    [self.deviceManager connect];
}

#pragma mark - GCKDeviceScannerListener Methods -

- (void)deviceDidComeOnline:(GCKDevice *)device {
    NSLog(@"device found!!!");
}

- (void)deviceDidGoOffline:(GCKDevice *)device {
    NSLog(@"device disappeared!!!");
    
    // TODO:
    // Retirar o device da lista
    
}

#pragma mark - GCKDeviceManagerDelegate Methods -

- (void)deviceManagerDidConnect:(GCKDeviceManager *)deviceManager {
    [self.deviceManager launchApplication:APPID];
}

@end