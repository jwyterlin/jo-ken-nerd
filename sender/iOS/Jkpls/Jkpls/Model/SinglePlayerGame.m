//
//  SinglePlayerGame.m
//  Jkpls
//
//  Created by Fábio Nogueira  on 03/10/14.
//  Copyright (c) 2014 Little Red Club. All rights reserved.
//

#import "SinglePlayerGame.h"

@interface SinglePlayerGame ()

@property (nonatomic, strong) NSString *messageResultGame;

@end
@implementation SinglePlayerGame

#pragma mark - Override Methods - 

- (void)initialize {
    [super initialize];
}

- (void)startGameWithChoice:(NSString *)choice {
    [super startGameWithChoice:choice];
    
    // fazer calculo de algum valor aleatorio
    // verificar se a escolha é maior que o número aleatório
    // informar resultado para o usuário
    
    self.messageResultGame = @"Esqueci como que era essa porcaria";
}

- (NSString *)resultGame {
    return self.messageResultGame;
}
 
@end
