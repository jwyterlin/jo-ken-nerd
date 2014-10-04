//
//  ChromeCast.h
//  Jkpls
//
//  Created by Fábio Nogueira  on 05/07/14.
//  Copyright (c) 2014 Jhonathan Wyterlin. All rights reserved.
//

#import <Foundation/Foundation.h>

#import "GoogleCast.h"
#import "TextChannel.h"

@protocol ChromeCastDelegate;

@interface ChromeCast : NSObject <GCKDeviceScannerListener, GCKDeviceManagerDelegate, GCKMediaControlChannelDelegate, UIActionSheetDelegate>

@property (nonatomic, strong) TextChannel *textChannel;
@property(strong,nonatomic) id<ChromeCastDelegate> delegate;

- (void)showActionSheetOnView:(UIView *)view;
- (BOOL)sendTextMessage:(NSString *)message;
- (BOOL)isConnected;
- (void)disconnectDevice;

@end

@protocol ChromeCastDelegate <NSObject>

@optional

- (void)didStartScanner;
- (void)didConnect:(GCKDeviceManager *)device;
- (void)didFailWithError:(NSError *)error deviceManager:(GCKDeviceManager *)device;
- (void)didDisconnectWithError:(NSError *)error deviceManager:(GCKDeviceManager *)device;

@end
