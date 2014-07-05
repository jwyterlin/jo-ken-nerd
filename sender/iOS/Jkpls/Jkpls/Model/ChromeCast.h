//
//  ChromeCast.h
//  Jkpls
//
//  Created by Fábio Nogueira  on 05/07/14.
//  Copyright (c) 2014 Jhonathan Wyterlin. All rights reserved.
//

#import <Foundation/Foundation.h>

#import "GoogleCast.h"

@interface ChromeCast : NSObject <GCKDeviceScannerListener, GCKDeviceManagerDelegate, UIActionSheetDelegate>

- (void)startScanner;
- (void)showActionSheetOnView:(UIView *)view;

@end
