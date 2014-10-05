//
//  GameLogic.h
//  Jkpls
//
//  Created by Fábio Nogueira  on 04/10/14.
//  Copyright (c) 2014 Little Red Club. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface GameLogic : NSObject

+ (NSString *)ResultBetweenMyChoice:(int)myChoice andOtherChoice:(int)otherChoice;
+ (NSString *)realNameOfChoice:(NSInteger)choice;

@end
