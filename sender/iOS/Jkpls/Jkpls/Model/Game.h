//
//  Game.h
//  Jkpls
//
//  Created by Fábio Nogueira  on 03/10/14.
//  Copyright (c) 2014 Little Red Club. All rights reserved.
//

#import <Foundation/Foundation.h>

enum {
    CHOICE_ROCK = 1,
    CHOICE_PAPER = 2,
    CHOICE_SCISSOR = 3,
    CHOICE_LIZARD = 4,
    CHOICE_SPOCK = 5
};
typedef int ChoiceType;

#define WIN @"win"
#define LOSE @"loses"
#define DRAW @"draw"

@interface Game : NSObject

@property (nonatomic, strong) NSString *playerName;

@property (nonatomic) NSInteger myChoice;
@property (nonatomic) NSInteger otherChoice;

@property (nonatomic, strong) NSString *resultGame;
@property (nonatomic, strong) NSString *titleResultGame;
@property (nonatomic, strong) NSString *messageResultGame;

- (void)initialize;

- (void)startGameWithChoice:(NSString *)choice;

- (NSString *)titleResultGame;
- (NSString *)messageResultGame;

@end
