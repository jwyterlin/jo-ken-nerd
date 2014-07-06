//
//  ViewController.m
//  Jkpls
//
//  Created by Jhonathan Wyterlin on 05/07/14.
//  Copyright (c) 2014 Jhonathan Wyterlin. All rights reserved.
//

#import "ViewController.h"
#import "TutorialViewController.h"
#import "ChromeCast.h"

@interface ViewController () {

    NSTimer *timer;
    int counter;
    NSTimer *timerChangeName;
    
}

@end

@implementation ViewController

@synthesize tfNamePlayer = _tfNamePlayer;

#pragma mark - Getter Methods -

#pragma mark - View Lifecycle

-(void)viewDidLoad {
    
    [super viewDidLoad];
    
    self.chromeCast = [ChromeCast new];
    self.chromeCast.delegate = self;
    
    UITapGestureRecognizer *tap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(toucheOnView)];
    tap.numberOfTapsRequired = 1;
    
    [self.view addGestureRecognizer:tap];
    
}

#pragma mark - IBAction Methods

-(IBAction)showTutorial:(id)sender {
    
    TutorialViewController *tutorialVC = [[TutorialViewController alloc] initWithNibName:kTutorialViewController bundle:nil];
    
    [self.navigationController pushViewController:tutorialVC animated:YES];
}

-(IBAction)chromeCastTouched:(id)sender {
    [self.chromeCast showActionSheetOnView:self.view];
}

-(IBAction)choseOption:(UIButton *)sender {
    
    NSString *message = [NSString stringWithFormat:@"%i",sender.tag];
    
    if ( ! [self.chromeCast sendTextMessage:message] ) {
        [self showMessage:@"Falha na comunicação. Por favor tente novamente."];
    }
    
}

-(IBAction)changedName:(UITextField *)textfield {
    
    NSLog( @"%@", textfield.text );
    
    if ( timerChangeName != nil ) {
        
        [timerChangeName invalidate];
        timerChangeName = nil;
        
    }

    timerChangeName = [NSTimer scheduledTimerWithTimeInterval:1.0 target:self selector:@selector(changeName:) userInfo:nil repeats:NO];

}

#pragma mark - ChromeCastDelegate Methods

-(void)didStartScanner {
    
    timer = [NSTimer scheduledTimerWithTimeInterval:0.5 target:self selector:@selector(loading:) userInfo:nil repeats:YES];
    
}

-(void)didConnect:(GCKDeviceManager *)device {

    [timer invalidate];
    timer = nil;
    
    [self setImage:[UIImage imageNamed:@"cast_on.png"] onButton:self.chromeCastTouched];
    
    [self showMessage:@"Conexão concluída com sucesso!"];
    
    [self sendNamePlayerToChromeCast];

}

-(void)didFailWithError:(NSError *)error deviceManager:(GCKDeviceManager *)device {
    
    [timer invalidate];
    timer = nil;
    
    [self setImage:[UIImage imageNamed:@"cast_off.png"] onButton:self.chromeCastTouched];
    
    [self showMessage:@"Falha na conexão"];
    
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

-(void)setImage:(UIImage *)image onButton:(UIButton *)button {
    
    [button setImage:image forState:UIControlStateNormal];
    [button setImage:image forState:UIControlStateSelected];
    [button setImage:image forState:UIControlStateHighlighted];
    
}

-(void)showMessage:(NSString *)message {
    
    UIAlertView *alert = [[UIAlertView alloc] initWithTitle:message message:nil delegate:nil cancelButtonTitle:nil otherButtonTitles:@"OK", nil];
    
    [alert show];
    
}

-(void)toucheOnView {
    
    [self.tfNamePlayer resignFirstResponder];
    
}

-(void)sendNamePlayerToChromeCast {
    
    if ( [self.chromeCast isConnected] ) {
        
        NSString *namePlayer;
        
        if ( self.tfNamePlayer.text == nil ) {
            
            namePlayer = @"Jogador";
            
        } else {
            
            if ( [self.tfNamePlayer.text isEqualToString:@""] ) {
                namePlayer = @"Jogador";
            } else {
                namePlayer = self.tfNamePlayer.text;
            }
            
        }
        
        NSString *action = @"connect";
        NSDictionary *jsonDict = @{ @"action": action, @"name": namePlayer};
        
        NSString *jsonString = [GCKJSONUtils writeJSON:jsonDict];
        
        if ( ! [self.chromeCast sendTextMessage:jsonString] ) {
            [self showMessage:@"Falha na comunicação. Por favor tente novamente."];
        }
        
    }
    
}

-(void)chromeCastIsConnected:(NSNotification *)notification {
    
    [self sendNamePlayerToChromeCast];
    
}

-(void)changeName:(NSTimer *)__timer {
    NSLog(@"changeName");
    [self sendNamePlayerToChromeCast];
    
}

@end
