//
//  GameLogic.m
//  Jkpls
//
//  Created by Fábio Nogueira  on 04/10/14.
//  Copyright (c) 2014 Little Red Club. All rights reserved.
//

#import "GameLogic.h"
#import "Game.h"

@implementation GameLogic

+ (NSString *)ResultBetweenMyChoice:(int)myChoice andOtherChoice:(int)otherChoice {
    if (myChoice == otherChoice) {
        return DRAW;
    }
    
    switch (myChoice) {
        case CHOICE_ROCK:
            if (otherChoice == CHOICE_SCISSOR || otherChoice == CHOICE_LIZARD) {
                return WIN;
            } else if (otherChoice == CHOICE_PAPER || otherChoice == CHOICE_SPOCK) {
                return LOSE;
            }
            break;
            
        case CHOICE_PAPER:
            if (otherChoice == CHOICE_ROCK || otherChoice == CHOICE_SPOCK) {
                return WIN;
            } else if (otherChoice == CHOICE_SCISSOR || otherChoice == CHOICE_LIZARD) {
                return LOSE;
            }
            break;
            
            
        case CHOICE_SCISSOR:
            if (otherChoice == CHOICE_PAPER || otherChoice == CHOICE_LIZARD) {
                return WIN;
            } else if (otherChoice == CHOICE_ROCK || otherChoice == CHOICE_SPOCK) {
                return LOSE;
            }
            break;
            
        case CHOICE_LIZARD:
            if (otherChoice == CHOICE_PAPER || otherChoice == CHOICE_SPOCK) {
                return WIN;
            } else if (otherChoice == CHOICE_ROCK || otherChoice == CHOICE_SCISSOR) {
                return LOSE;
            }
            break;
            
            
        case CHOICE_SPOCK:
            if (otherChoice == CHOICE_ROCK || otherChoice == CHOICE_SCISSOR) {
                return WIN;
            } else if (otherChoice == CHOICE_PAPER || otherChoice == CHOICE_LIZARD) {
                return LOSE;
            }
            break;
    }
    
    return DRAW;
}

+ (NSString *)realNameOfChoice:(NSInteger)choice {
    NSString *name = nil;
    switch (choice) {
        case CHOICE_ROCK:
            name = @"Pedra";
            break;
        case CHOICE_PAPER:
            name = @"Papel";
            break;
        case CHOICE_SCISSOR:
            name = @"Tesoura";
            break;
        case CHOICE_LIZARD:
            name = @"Largarto";
            break;
        case CHOICE_SPOCK:
            name = @"Spock";
        default:
        break;
    }
    return name;
}

@end
