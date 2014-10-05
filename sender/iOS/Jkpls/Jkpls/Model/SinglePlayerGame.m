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
    NSString *title = @"Resultado";
    
    if ([self.resultGame isEqual:WIN]) {
        title = @"Vencedor";
    } else if ([self.resultGame isEqual:LOSE]) {
        title = @"Perdedor";
    } else if ([self.resultGame isEqual:DRAW]) {
        title = @"Empate";
    }
    
    return title;
}

- (NSString *)messageResultGame {
    NSString *message = nil;
    NSString *nameOfMyChoice = [GameLogic realNameOfChoice:self.myChoice];
    NSString *nameOfOtherChoice = [GameLogic realNameOfChoice:self.otherChoice];
    
    if ([self.resultGame isEqual:WIN]) {
        message = [NSString stringWithFormat:@"\n %@ ganha de %@", nameOfMyChoice, nameOfOtherChoice];
    } else if ([self.resultGame isEqual:LOSE]) {
        message = [NSString stringWithFormat:@"\n %@ perde para %@", nameOfMyChoice, nameOfOtherChoice];
    } else if ([self.resultGame isEqual:DRAW]) {
        message = @"\n Jogue novamente!";
    }
    
    return message;
}
 
@end
