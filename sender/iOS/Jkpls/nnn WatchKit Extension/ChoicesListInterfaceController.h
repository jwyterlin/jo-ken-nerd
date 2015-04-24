//
//  InterfaceController.h
//  nnn WatchKit Extension
//
//  Created by Jhonathan Wyterlin on 24/04/15.
//  Copyright (c) 2015 Little Red Club. All rights reserved.
//

#import <WatchKit/WatchKit.h>
#import <Foundation/Foundation.h>

@interface ChoicesListInterfaceController : WKInterfaceController

@property(nonatomic,strong) IBOutlet WKInterfaceLabel *name;
@property(nonatomic,strong) IBOutlet WKInterfaceImage *image;

@end
