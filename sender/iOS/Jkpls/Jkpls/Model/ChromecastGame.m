//
//  ChromecastGame.m
//  Jkpls
//
//  Created by Fábio Nogueira  on 03/10/14.
//  Copyright (c) 2014 Little Red Club. All rights reserved.
//

#import "ChromecastGame.h"

@implementation ChromecastGame

#pragma mark - Override Methods -

- (void)initialize {
    [super initialize];
//    self.chromeCast = [ChromeCast new];
//    self.chromeCast.delegate = self;
}

- (void)startGameWithChoice:(NSString *)choice {//    NSString *action = @"choice";
//    NSString *message = [NSString stringWithFormat:@"%i",sender.tag];
//    NSDictionary *jsonDict = @{ @"action": action, @"value": message};
//    NSString *jsonString = [GCKJSONUtils writeJSON:jsonDict];
//    
//    if ( [self.chromeCast isConnected] ) {
//        if ( [self.chromeCast sendTextMessage:jsonString] ) {
//            [self.activityIndicator startAnimating];
//            self.lbResultGame.text = @"Aguardando o oponente";
//        } else {
//            [self showMessage:@"Falha na comunicação. Por favor tente novamente."];
//        }
//    }
}

- (NSString *)resultGame {
    return nil;
}

@end
