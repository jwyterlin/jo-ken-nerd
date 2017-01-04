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
#import "Game.h"

@interface ChoicesListInterfaceController()

@property(nonatomic,strong) Choice *choice;

@end


@implementation ChoicesListInterfaceController

-(void)awakeWithContext:(id)context {
    
    [super awakeWithContext:context];

    Choice *choice = (Choice *)context;
    
    self.choice = choice;
    
    [self.image setImage:choice.image];
    [self.name setText:choice.name];
    [self.result setHidden:YES];
    
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
    
    [self.result setHidden:YES];
    
//    NSDictionary *dictionary = [[NSDictionary alloc] initWithObjectsAndKeys:self.choice.identifier,@"choice", nil];
    
#warning TODO: Remove openParentApplication: reply: and use WatchConnectivity framework
    
//    For your watchOS 2 extension, you can use the new WatchConnectivity framework to communicate between your parent app and WatchKit extension.
    
//    [WKInterfaceController openParentApplication:dictionary reply:^(NSDictionary *replyInfo, NSError *error) {
//        
//        if ( replyInfo ) {
//            
//            if ( replyInfo[@"ResultGame"] ) {
//                
//                NSString *stringResult = replyInfo[@"ResultGame"];
//                
//                [self.result setHidden:NO];
//                [self.result setText:stringResult];
//                
//                return;
//                
//            } else {
//                
//                NSLog(@"No ResultGame");
//                
//            }
//            
//        }
//        
//        if ( error ) {
//            NSLog(@"Error occurred: %@", error);
//        } else {
//            NSLog(@"No error, but no data either");
//        }
//        
//    }];
    
}

@end



