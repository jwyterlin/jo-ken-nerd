//
//  Game.h
//  Jkpls
//
//  Created by Fábio Nogueira  on 03/10/14.
//  Copyright (c) 2014 Little Red Club. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface Game : NSObject

@property (nonatomic, strong) NSString *playerName;

- (void)initialize;

- (void)startGameWithChoice:(NSString *)choice;

- (NSString *)resultGame;

@end
