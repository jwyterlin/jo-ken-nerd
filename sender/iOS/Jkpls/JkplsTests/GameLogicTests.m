//
//  GameLogicTests.m
//  Jkpls
//
//  Created by Jhonathan Wyterlin on 16/08/15.
//  Copyright (c) 2015 Little Red Club. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <XCTest/XCTest.h>

#import "Game.h"
#import "GameLogic.h"

@interface GameLogicTests : XCTestCase

@end

@implementation GameLogicTests

- (void)setUp {
    [super setUp];
    // Put setup code here. This method is called before the invocation of each test method in the class.
}

- (void)tearDown {
    // Put teardown code here. This method is called after the invocation of each test method in the class.
    [super tearDown];
}

-(void)testRealNameOfChoiceRock {
    
    NSString *rock = [GameLogic realNameOfChoice:CHOICE_ROCK];
    
    XCTAssert( [rock isEqualToString:NSLocalizedString(@"pedra", nil)], @"Choice ROCK doesn't return 'Rock'" );
    
}

-(void)testRealNameOfChoicePaper {
    
    NSString *paper = [GameLogic realNameOfChoice:CHOICE_PAPER];
    
    XCTAssert( [paper isEqualToString:NSLocalizedString(@"papel", nil)], @"Choice PAPER doesn't return 'Paper'" );
    
}

-(void)testRealNameOfChoiceScissor {
    
    NSString *scissor = [GameLogic realNameOfChoice:CHOICE_SCISSOR];
    
    XCTAssert( [scissor isEqualToString:NSLocalizedString(@"tesoura", nil)], @"Choice SCISSOR doesn't return 'Scissor'" );
    
}

-(void)testRealNameOfChoiceLizard {
    
    NSString *lizard = [GameLogic realNameOfChoice:CHOICE_LIZARD];
    
    XCTAssert( [lizard isEqualToString:NSLocalizedString(@"lagarto", nil)], @"Choice LIZARD doesn't return 'Lizard'" );
    
}

-(void)testRealNameOfChoiceSpock {
    
    NSString *spock = [GameLogic realNameOfChoice:CHOICE_SPOCK];
    
    XCTAssert( [spock isEqualToString:NSLocalizedString(@"spock", nil)], @"Choice SPOCK doesn't return 'Spock'" );
    
}

@end
