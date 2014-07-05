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

-(void)startScanner;
-(void)showActionSheetOnView:(UIView *)view;

@end

//@protocol ChromeCastDelegate <NSObject>
//
//@optional
//
//-(void)
//
//@end
