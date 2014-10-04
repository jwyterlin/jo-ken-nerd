//
//  ViewController.m
//  Jkpls
//
//  Created by Jhonathan Wyterlin on 05/07/14.
//  Copyright (c) 2014 Jhonathan Wyterlin. All rights reserved.
//

#import "GameViewController.h"
#import "ChromeCast.h"

@interface GameViewController () {
    NSTimer *timer;
    int counter;
}

@property (nonatomic, strong) UIAlertController *alertController;

@end

@implementation GameViewController

#pragma mark - Getter Methods -

- (UIAlertController *)alertController {
    if (!_alertController) {
        _alertController = [UIAlertController alertControllerWithTitle:@"Resultado"
                                                                message:nil
                                                         preferredStyle:UIAlertControllerStyleAlert];
        [_alertController addAction:[UIAlertAction actionWithTitle:@"Cancelar" style:UIAlertActionStyleCancel handler:nil]];
    }
    return _alertController;
}

#pragma mark - View Lifecycle

-(void)viewDidLoad {
    [super viewDidLoad];
    [self.currentGameMode initialize];
}

#pragma mark - Private Methods -

#pragma mark - IBAction Methods

- (IBAction)chromeCastTouched:(id)sender {
    self.lbResultGame.text = @"";
    [self.activityIndicator stopAnimating];
    
    if (self.chromeCast.isConnected) {
        [self.chromeCast disconnectDevice];
    } else {
        [self.chromeCast showActionSheetOnView:self.view];
    }
}

- (IBAction)choseOption:(UIButton *)sender {
    self.lbResultGame.text = @"";
    [self.currentGameMode startGameWithChoice:[NSString stringWithFormat:@"%i",sender.tag]];
    self.alertController.message = [self.currentGameMode resultGame];
    [self presentViewController:self.alertController animated:YES completion:nil];
}

#pragma mark - ChromeCastDelegate Methods -

- (void)didStartScanner {
    timer = [NSTimer scheduledTimerWithTimeInterval:0.5 target:self selector:@selector(loading:) userInfo:nil repeats:YES];
}

- (void)didConnect:(GCKDeviceManager *)device {
    [timer invalidate];
    timer = nil;
    
    [self setImage:[UIImage imageNamed:@"cast_on.png"] onButton:self.chromeCastTouched];
    
    //[self showMessage:@"Conexão concluída com sucesso!"];
    
    [self sendNamePlayerToChromeCastWithAction:@"connect"];
    
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(getGameResult:) name:kGameResultWasReceived object:nil];
}

- (void)didFailWithError:(NSError *)error deviceManager:(GCKDeviceManager *)device {
    [timer invalidate];
    timer = nil;
    
    [self setImage:[UIImage imageNamed:@"cast_off.png"] onButton:self.chromeCastTouched];
    
    self.chromeCast = nil;
    
//    [self initChromeCast];
    
    [self showMessage:[NSString stringWithFormat:@"Falha na conexão: %@", error.localizedDescription ]];
}

- (void)didDisconnectWithError:(NSError *)error deviceManager:(GCKDeviceManager *)device {
    [timer invalidate];
    timer = nil;
    
    [self setImage:[UIImage imageNamed:@"cast_off.png"] onButton:self.chromeCastTouched];
    
    self.chromeCast = nil;
    
//    [self initChromeCast];
    
    [self showMessage:@"Desconectado"];
}

#pragma mark - Helper Methods

-(void)loading:(NSTimer *)timer {
    counter++;
    
    if ( counter == 1 ) {
        [self setImage:[UIImage imageNamed:@"cast_on0.png"] onButton:self.chromeCastTouched];
    } else if ( counter == 2 ) {
        [self setImage:[UIImage imageNamed:@"cast_on1.png"] onButton:self.chromeCastTouched];
    } else if ( counter == 3 ) {
        [self setImage:[UIImage imageNamed:@"cast_on2.png"] onButton:self.chromeCastTouched];
        counter = 0;
    }
}

- (void)setImage:(UIImage *)image onButton:(UIButton *)button {
    [button setImage:image forState:UIControlStateNormal];
    [button setImage:image forState:UIControlStateSelected];
    [button setImage:image forState:UIControlStateHighlighted];
}

- (void)showMessage:(NSString *)message {
    UIAlertView *alert = [[UIAlertView alloc] initWithTitle:message message:nil delegate:nil cancelButtonTitle:nil otherButtonTitles:@"OK", nil];
    
    [alert show];
}

- (void)sendNamePlayerToChromeCastWithAction:(NSString *)action {
    if ( [self.chromeCast isConnected] ) {

        NSDictionary *jsonDict = @{ @"action": action, @"name": self.currentGameMode.playerName};
        NSString *jsonString = [GCKJSONUtils writeJSON:jsonDict];
        if ( ! [self.chromeCast sendTextMessage:jsonString] ) {
            [self showMessage:@"Falha na comunicação. Por favor tente novamente."];
        }
    }
}

- (void)chromeCastIsConnected:(NSNotification *)notification {
    [self sendNamePlayerToChromeCastWithAction:@"connect"];
}

- (void)getGameResult:(NSNotification *)notification {
    [self.activityIndicator stopAnimating];
    
    NSDictionary *dictionary = notification.object;

    if ( dictionary == nil ) {
        return;
    }
    
    NSLog( @"dictionary: %@", dictionary );
    
    if ( dictionary[@"result"] ) {
        
        NSString *stringResult = dictionary[@"result"];
        
        if ( [stringResult isEqualToString:@"win"] ) {
            // Ganhou
            self.lbResultGame.text = @"Parabéns! Você ganhou.";
        } else if ( [stringResult isEqualToString:@"draw"] ) {
            // Empatou
            self.lbResultGame.text = @"Empatou";
        } else if ( [stringResult isEqualToString:@"loses"] ) {
            // Perdeu
            self.lbResultGame.text = @"Que pena! Você perdeu.";
        }
        
    } else if ( dictionary[@"sucess"] ) {
        
        // Sala gerada com sucesso
        self.lbResultGame.text = @"Bem-vindo ao Jogo.";
        
    } else if ( dictionary[@"error"] ) {
        
        NSString *stringError = dictionary[@"error"];
        
        if ( [stringError isEqualToString:@"room_full"] ) {
            self.lbResultGame.text = @"Sala cheia.";
        } else {
            self.lbResultGame.text = @"";
        }

    } else {
        self.lbResultGame.text = @"";
    }
}

@end
