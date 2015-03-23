//
//  SinglePlayerGame.m
//  Jkpls
//
//  Created by Fábio Nogueira  on 03/10/14.
//  Copyright (c) 2014 Little Red Club. All rights reserved.
//

#import "SinglePlayerGame.h"
#import "GameLogic.h"

@interface SinglePlayerGame ()

@end
@implementation SinglePlayerGame

#pragma mark - Override Methods - 

- (void)initialize {
    [super initialize];
}

- (void)startGameWithChoice:(NSString *)choice {
    [super startGameWithChoice:choice];
    
    int otherChoice =  (arc4random() % 5) + 1;
    
    self.myChoice = choice.intValue;
    self.otherChoice = (NSInteger)otherChoice;
    self.resultGame = [GameLogic ResultBetweenMyChoice:choice.intValue andOtherChoice:otherChoice];
}

- (NSString *)titleResultGame {
    NSString *title = NSLocalizedString(@"resultado", nil);
    
    if ([self.resultGame isEqual:WIN]) {
        title = NSLocalizedString(@"vencedor", nil);
    } else if ([self.resultGame isEqual:LOSE]) {
        title = NSLocalizedString(@"perdedor", nil);
    } else if ([self.resultGame isEqual:DRAW]) {
        title = NSLocalizedString(@"empate", nil);
    }
    
    return title;
}

- (NSString *)messageResultGame {
    NSString *message = nil;
    NSString *nameOfMyChoice = [GameLogic realNameOfChoice:self.myChoice];
    NSString *nameOfOtherChoice = [GameLogic realNameOfChoice:self.otherChoice];
    
    if ([self.resultGame isEqual:WIN]) {
        
        NSLocalizedString(@"pedra", nil);
        message = [NSString stringWithFormat:@"%@ %@ %@", nameOfMyChoice, NSLocalizedString(@"ganha", nil), nameOfOtherChoice];
    } else if ([self.resultGame isEqual:LOSE]) {
        message = [NSString stringWithFormat:@"%@ %@ %@", nameOfMyChoice, NSLocalizedString(@"perde", nil), nameOfOtherChoice];
    } else if ([self.resultGame isEqual:DRAW]) {
        message = NSLocalizedString(@"jogue_novamente", nil);
    }
    
    return message;
}
 
@end
