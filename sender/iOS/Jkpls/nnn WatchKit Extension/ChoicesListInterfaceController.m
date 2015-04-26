//
//  InterfaceController.m
//  nnn WatchKit Extension
//
//  Created by Jhonathan Wyterlin on 24/04/15.
//  Copyright (c) 2015 Little Red Club. All rights reserved.
//

#import "ChoicesListInterfaceController.h"

// Model
#import "Choice.h"

@interface ChoicesListInterfaceController()

@end


@implementation ChoicesListInterfaceController

-(void)awakeWithContext:(id)context {
    
    [super awakeWithContext:context];

    Choice *choice = (Choice *)context;
    
    [self.image setImage:choice.image];
    [self.name setText:choice.name];
    
}

-(void)willActivate {
    // This method is called when watch view controller is about to be visible to user
    [super willActivate];
}

-(void)didDeactivate {
    // This method is called when watch view controller is no longer visible
    [super didDeactivate];
}

-(IBAction)optionSelected:(id)sender {
    
    NSLog( @"optionSelected" );
    
}

@end



