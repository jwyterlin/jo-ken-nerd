//
//  MainInterfaceController.m
//  Jkpls
//
//  Created by Jhonathan Wyterlin on 24/04/15.
//  Copyright (c) 2015 Little Red Club. All rights reserved.
//

#import "MainInterfaceController.h"

// Model
#import "Choice.h"

@interface MainInterfaceController ()

@end

@implementation MainInterfaceController

-(void)awakeWithContext:(id)context {
    [super awakeWithContext:context];
    
    // Configure interface objects here.
}

-(void)willActivate {
    
    // This method is called when watch view controller is about to be visible to user
    [super willActivate];
    
    
}

-(void)didDeactivate {
    // This method is called when watch view controller is no longer visible
    [super didDeactivate];
}

-(IBAction)playButtonPressed:(id)sender {
    
    Choice *lizard = [Choice new];
    lizard.name = @"Lizard";
    lizard.image = [UIImage imageNamed:@"lizard"];
    
    Choice *paper = [Choice new];
    paper.name = @"Paper";
    paper.image = [UIImage imageNamed:@"paper"];
    
    Choice *rock = [Choice new];
    rock.name = @"Rock";
    rock.image = [UIImage imageNamed:@"rock"];
    
    Choice *scissor = [Choice new];
    scissor.name = @"Scissor";
    scissor.image = [UIImage imageNamed:@"scissor"];
    
    Choice *spock = [Choice new];
    spock.name = @"Spock";
    spock.image = [UIImage imageNamed:@"spock"];
    
    NSArray *options = [NSArray arrayWithObjects:lizard,paper,rock,scissor,spock,nil];
    
    NSMutableArray *controllerNames = [NSMutableArray new];
    
    for ( int x = 0; x < options.count; x++ )
        [controllerNames addObject:@"choicesList"];
    
    NSArray *contexts = options;
    
    [self presentControllerWithNames:controllerNames contexts:contexts];
    
}

@end



