//
//  ChromeCast.m
//  Jkpls
//
//  Created by Fábio Nogueira  on 05/07/14.
//  Copyright (c) 2014 Jhonathan Wyterlin. All rights reserved.
//

#import "ChromeCast.h"

//#define APPID @"9EE9C969"
//#define NAMESPACE @"urn:x-cast:com.fbvictorhugo.simpledicecast.cast"
#define APPID @"209A5252"
#define NAMESPACE @"urn:x-cast:com.littleredgroup.jkpls"

@interface ChromeCast ()

@property (nonatomic, strong) GCKDeviceScanner *deviceScanner;
@property (nonatomic, strong) GCKDeviceManager *deviceManager;

@property (nonatomic, strong) UIActionSheet *actionSheet;
@property(nonatomic,strong) NSMutableArray *devices;

@end

@implementation ChromeCast

@synthesize delegate = _delegate;

#pragma mark - Lifecycle

-(id)init {
    
    self = [super init];
    
    if ( self ) {
        self.devices = [NSMutableArray new];
        [self startScanner];
    }
    
    return self;
    
}

#pragma mark - Getter Methods -

-(GCKDeviceScanner *)deviceScanner {
    
    if ( ! _deviceScanner ) {
        _deviceScanner = [[GCKDeviceScanner alloc] init];
    }
    
    return _deviceScanner;
    
}

-(GCKDeviceManager *)deviceManager {
    
    if ( ! _deviceManager ) {
        _deviceManager = [[GCKDeviceManager alloc] init];
    }
    
    return _deviceManager;
    
}

-(TextChannel *)textChannel {
    
    if ( ! _textChannel ) {
        
        _textChannel = [[TextChannel alloc] initWithNamespace:NAMESPACE];
        [self.deviceManager addChannel:_textChannel];
        
    }
    
    return _textChannel;
    
}

-(UIActionSheet *)actionSheet {
    
    if ( ! _actionSheet ) {
        
        _actionSheet = [[UIActionSheet alloc] initWithTitle:nil
                                                   delegate:self
                                          cancelButtonTitle:nil
                                     destructiveButtonTitle:@"Cancelar"
                                          otherButtonTitles:nil, nil];
    }
    
    return _actionSheet;
    
}

#pragma Mark - Private Methods -

-(void)startScanner {
    
    [self.deviceScanner addListener:self];
    [self.deviceScanner startScan];
    
    if ( [_delegate respondsToSelector:@selector(didStartScanner)] ) {
        [_delegate didStartScanner];  // Chama o método que o controller do usuário implementou
    }
    
}

-(BOOL)listDevicesHasThisDeviceName:(NSString *)name {
    
    for ( GCKDevice *device in self.devices ) {
        
        if ( [device.friendlyName isEqualToString:name] ) {
            return YES;
        }
        
    }
    
    return NO;
    
}

-(void)removeDeviceFromList:(GCKDevice *)device {
    
    [self.devices removeObject:device];
    
}

#pragma mark - Public Methods - 

-(void)showActionSheetOnView:(UIView *)view {
    
    for ( GCKDevice *device in self.deviceScanner.devices ) {

        if ( ! [self listDevicesHasThisDeviceName:device.friendlyName] ) {
            
            [self.devices     addObject:device];
            [self.actionSheet addButtonWithTitle:device.friendlyName];
            
        }

    }
    
    [self.actionSheet showInView:view];
}

-(BOOL)sendTextMessage:(NSString *)message {
    
    return [self.textChannel sendTextMessage:message];
    
}

-(BOOL)isConnected {
    
    NSLog( @"self.deviceManager: %@", _deviceManager );
    
    if ( _deviceManager == nil ) {
        [self deviceManager];
        NSLog( @"self.deviceManager - 2: %@", self.deviceManager );
    }
    
    return [self.deviceManager isConnected];

}

#pragma mark - UIActionSheetDelegate Methods -

-(void)actionSheet:(UIActionSheet *)actionSheet clickedButtonAtIndex:(NSInteger)buttonIndex {
    
    if ( buttonIndex == 0 ) {
        return;
    }
    
    GCKDevice *selectedDevice = self.deviceScanner.devices[buttonIndex - 1];
    
    NSDictionary *info = [[NSBundle mainBundle] infoDictionary];
    self.deviceManager = [[GCKDeviceManager alloc]  initWithDevice:selectedDevice clientPackageName:[info objectForKey:@"CFBundleIdentifier"]];
    
    self.deviceManager.delegate = self;
    [self.deviceManager connect];
    
}

#pragma mark - GCKDeviceScannerListener Methods -

-(void)deviceDidComeOnline:(GCKDevice *)device {
    
    NSLog( @"device found!!!" );
    
    if ( ! [self listDevicesHasThisDeviceName:device.friendlyName] ) {
        
        [self.devices     addObject:device];
        [self.actionSheet addButtonWithTitle:device.friendlyName];
        
    }
    
}

-(void)deviceDidGoOffline:(GCKDevice *)device {
    
    NSLog( @"device disappeared!!!" );
    
    // Retirar o device da lista
    [self removeDeviceFromList:device];
    
}

#pragma mark - GCKDeviceManagerDelegate Methods -

-(void)deviceManagerDidConnect:(GCKDeviceManager *)deviceManager {

//    if ( ! [self.deviceManager isConnectedToApp] ) {
    
        [self.deviceManager launchApplication:APPID];
        
//    }
    
//    [self.deviceManager joinApplication:APPID];
    
    if ( [_delegate respondsToSelector:@selector(didConnect:)] ) {
        [_delegate didConnect:deviceManager];  // Chama o método que o controller do usuário implementou
    }
    
}

-(void)deviceManager:(GCKDeviceManager *)deviceManager didFailToConnectWithError:(NSError *)error {
    
    if ( [_delegate respondsToSelector:@selector(didFailWithError:deviceManager:)] ) {
        [_delegate didFailWithError:error deviceManager:deviceManager];  // Chama o método que o controller do usuário implementou
    }
    
}

-(void)deviceManager:(GCKDeviceManager *)deviceManager didDisconnectWithError:(NSError *)error {

    if ( [_delegate respondsToSelector:@selector(didFailWithError:deviceManager:)] ) {
        [_delegate didFailWithError:error deviceManager:deviceManager];  // Chama o método que o controller do usuário implementou
    }
    
}

#pragma mark - GCKDeviceManagerDelegate Methods -

- (void)deviceManager:(GCKDeviceManager *)deviceManager
didConnectToCastApplication:(GCKApplicationMetadata *)applicationMetadata
            sessionID:(NSString *)sessionID
  launchedApplication:(BOOL)launchedApplication {
    

}

@end