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
#import "Game.h"

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
    
    Choice *rock = [Choice new];
    rock.identifier = [NSNumber numberWithInt:CHOICE_ROCK];
    rock.name = @"Rock";
    rock.image = [UIImage imageNamed:@"rock"];
    
    Choice *paper = [Choice new];
    paper.identifier = [NSNumber numberWithInt:CHOICE_PAPER];
    paper.name = @"Paper";
    paper.image = [UIImage imageNamed:@"paper"];
    
    Choice *scissor = [Choice new];
    scissor.identifier = [NSNumber numberWithInt:CHOICE_SCISSOR];
    scissor.name = @"Scissor";
    scissor.image = [UIImage imageNamed:@"scissor"];
    
    Choice *lizard = [Choice new];
    lizard.identifier = [NSNumber numberWithInt:CHOICE_LIZARD];
    lizard.name = @"Lizard";
    lizard.image = [UIImage imageNamed:@"lizard"];

    Choice *spock = [Choice new];
    spock.identifier = [NSNumber numberWithInt:CHOICE_SPOCK];
    spock.name = @"Spock";
    spock.image = [UIImage imageNamed:@"spock"];
    
    NSArray *options = [NSArray arrayWithObjects:rock,paper,scissor,lizard,spock,nil];
    
    NSMutableArray *controllerNames = [NSMutableArray new];
    
    for ( int x = 0; x < options.count; x++ )
        [controllerNames addObject:@"choicesList"];
    
    NSArray *contexts = options;
    
    [self presentControllerWithNames:controllerNames contexts:contexts];
    
}

@end



