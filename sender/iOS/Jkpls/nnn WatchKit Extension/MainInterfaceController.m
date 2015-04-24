//
//  MainInterfaceController.m
//  Jkpls
//
//  Created by Jhonathan Wyterlin on 24/04/15.
//  Copyright (c) 2015 Little Red Club. All rights reserved.
//

#import "MainInterfaceController.h"

@interface MainInterfaceController ()

@end

@implementation MainInterfaceController

- (void)awakeWithContext:(id)context {
    [super awakeWithContext:context];
    
    // Configure interface objects here.
}

- (void)willActivate {
    
    // This method is called when watch view controller is about to be visible to user
    [super willActivate];
    
    
}

- (void)didDeactivate {
    // This method is called when watch view controller is no longer visible
    [super didDeactivate];
}

-(IBAction)playButtonPressed:(id)sender {
    
    NSDictionary *lizard = [[NSDictionary alloc] initWithObjectsAndKeys:[UIImage imageNamed:@"lizard"],@"image",@"Lizard",@"name", nil];
    NSDictionary *paper = [[NSDictionary alloc] initWithObjectsAndKeys:[UIImage imageNamed:@"paper"],@"image",@"Paper",@"name", nil];
    NSDictionary *rock = [[NSDictionary alloc] initWithObjectsAndKeys:[UIImage imageNamed:@"rock"],@"image",@"Rock",@"name", nil];
    NSDictionary *scissor = [[NSDictionary alloc] initWithObjectsAndKeys:[UIImage imageNamed:@"scissor"],@"image",@"Scissor",@"name", nil];
    NSDictionary *spock = [[NSDictionary alloc] initWithObjectsAndKeys:[UIImage imageNamed:@"spock"],@"image",@"Spock",@"name", nil];
    
    NSArray *options = [NSArray arrayWithObjects:lizard,paper,rock,scissor,spock,nil];
    
    NSMutableArray *controllerNames = [NSMutableArray new];
    
    for ( int x = 0; x < options.count; x++ )
        [controllerNames addObject:@"choicesList"];
    
    NSArray *contexts = options;
    
    [self presentControllerWithNames:controllerNames contexts:contexts];
    
}

@end



