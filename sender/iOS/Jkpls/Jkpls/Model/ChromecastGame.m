//
//  ChromecastGame.m
//  Jkpls
//
//  Created by Fábio Nogueira  on 03/10/14.
//  Copyright (c) 2014 Little Red Club. All rights reserved.
//

#import "ChromecastGame.h"

@interface ChromeCast ()

- (void)_toggleChromecastButtonNotification;
- (void)_showMessage:(NSString *)message;
- (void)_sendNamePlayerToChromeCastWithAction:(NSString *)action;
- (void)_loading:(NSTimer *)timer;

- (void)getGameResult:(NSNotification *)notification;

@end

@implementation ChromecastGame

#pragma mark - Override Methods -

- (void)initialize {
    [super initialize];
    self.chromeCast = [ChromeCast new];
    self.chromeCast.delegate = self;
    
    [self performSelector:@selector(_toggleChromecastButtonNotification) withObject:nil afterDelay:0.7f];
}

- (void)startGameWithChoice:(NSString *)choice {
    NSString *action = @"choice";
    NSDictionary *jsonDict = @{ @"action": action, @"value": choice};
    NSString *jsonString = [GCKJSONUtils writeJSON:jsonDict];
    
    if ( [self.chromeCast isConnected] ) {
        if ( [self.chromeCast sendTextMessage:jsonString] ) {
            [[NSNotificationCenter defaultCenter] postNotificationName:STATUS_USER_CHROMECAST_NOTIFICATION
                                                                object:NSLocalizedString(@"aguardando_oponente", nil)];
        }
    } else {
        [self _showMessage:NSLocalizedString(@"falha_comunicacao", nil)];
    }
}

- (NSString *)resultGame {
    return self.messageResultGame;
}

#pragma mark - Private Methods - 

- (void)_toggleChromecastButtonNotification {
    [[NSNotificationCenter defaultCenter] postNotificationName:TOGGLE_CHROMECAST_BUTTON_NOTIFICATION object:nil];
}

- (void)_showMessage:(NSString *)message {
    UIAlertView *alert = [[UIAlertView alloc] initWithTitle:message
                                                    message:nil
                                                   delegate:nil
                                          cancelButtonTitle:nil
                                          otherButtonTitles:@"OK", nil];
    
    [alert show];
}

- (void)_sendNamePlayerToChromeCastWithAction:(NSString *)action {
    if ( [self.chromeCast isConnected] ) {
        
        NSDictionary *jsonDict = @{ @"action": action, @"name": self.playerName};
        NSString *jsonString = [GCKJSONUtils writeJSON:jsonDict];
        if ( ! [self.chromeCast sendTextMessage:jsonString] ) {
            [self _showMessage:NSLocalizedString(@"falha_comunicacao", nil)];
        }
    }
}

-(void)_loading:(NSTimer *)timer {
    self.counter++;
    UIImage *image;
    
    if (self.counter == 1) {
        image = [UIImage imageNamed:@"cast_on0.png"];
    } else if (self.counter == 2) {
        image = [UIImage imageNamed:@"cast_on1.png"];
    } else if (self.counter == 3) {
        image = [UIImage imageNamed:@"cast_on2.png"];
        self.counter = 0;
    }
    
    [[NSNotificationCenter defaultCenter] postNotificationName:CHANGE_CHROMECAST_IMAGE_STATUS_NOTIFICATION object:image];
}

#pragma mark - Public Methods - 

- (void)chromeCastButtonTouched:(id)sender {
    if (self.chromeCast.isConnected) {
        [self.chromeCast disconnectDevice];
    } else {
        [self.chromeCast showActionSheetOnView:sender];
    }
}

#pragma mark - ChromeCastDelegate Methods -

- (void)didStartScanner {
    self.timer = [NSTimer scheduledTimerWithTimeInterval:0.5f
                                                  target:self
                                                selector:@selector(_loading:)
                                                userInfo:nil
                                                 repeats:YES];
}

- (void)didConnect:(GCKDeviceManager *)device {
    [self.timer invalidate];
    self.timer = nil;
    
    [[NSNotificationCenter defaultCenter] postNotificationName:CHANGE_CHROMECAST_IMAGE_STATUS_NOTIFICATION
                                                        object:[UIImage imageNamed:@"cast_on.png"]];
    
    [self _showMessage:NSLocalizedString(@"conexao_sucesso", nil)];
    
    [self _sendNamePlayerToChromeCastWithAction:@"connect"];
    
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(getGameResult:) name:kGameResultWasReceived object:nil];
}

- (void)didFailWithError:(NSError *)error deviceManager:(GCKDeviceManager *)device {
    [self.timer invalidate];
    self.timer = nil;
    
    [[NSNotificationCenter defaultCenter] postNotificationName:CHANGE_CHROMECAST_IMAGE_STATUS_NOTIFICATION
                                                        object:[UIImage imageNamed:@"cast_off.png"]];
    
    [self initialize];
    
    [self _showMessage:[NSString stringWithFormat:@"%@ %@", NSLocalizedString(@"falha_conexao", nil), error.localizedDescription ]];
}

- (void)didDisconnectWithError:(NSError *)error deviceManager:(GCKDeviceManager *)device {
    [self.timer invalidate];
    self.timer = nil;

    [[NSNotificationCenter defaultCenter] postNotificationName:CHANGE_CHROMECAST_IMAGE_STATUS_NOTIFICATION
                                                        object:[UIImage imageNamed:@"cast_off.png"]];
    
    [self initialize];
    
    [self _showMessage:NSLocalizedString(@"desconectado", <#comment#>)];
}

#pragma mark - Notifications Methods -

- (void)getGameResult:(NSNotification *)notification {
    NSDictionary *dictionary = notification.object;
    
    if ( dictionary == nil ) {
        return;
    }
    
    NSLog( @"dictionary: %@", dictionary );
    
    if ( dictionary[@"result"] ) {
        
        NSString *stringResult = dictionary[@"result"];
        
        if ( [stringResult isEqualToString:@"win"] ) {
            // Ganhou
            self.messageResultGame = NSLocalizedString(@"parabens_ganhou", nil);
        } else if ( [stringResult isEqualToString:@"draw"] ) {
            // Empatou
            self.messageResultGame = NSLocalizedString(@"empate", nil);
        } else if ( [stringResult isEqualToString:@"loses"] ) {
            // Perdeu
            self.messageResultGame = NSLocalizedString(@"perdeu", nil);
        }
        
    } else if ( dictionary[@"sucess"] ) {
        
        // Sala gerada com sucesso
        self.messageResultGame = NSLocalizedString(@"bem_vindo", nil);
        
    } else if ( dictionary[@"error"] ) {
        
        NSString *stringError = dictionary[@"error"];
        
        if ( [stringError isEqualToString:@"room_full"] ) {
            self.messageResultGame = NSLocalizedString(@"sala_cheia", nil);
        } else {
            self.messageResultGame = @"";
        }
        
    } else {
        self.messageResultGame = @"";
    }
}

@end
