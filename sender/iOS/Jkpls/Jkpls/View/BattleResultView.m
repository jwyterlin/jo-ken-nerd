//
//  BattleResultView.m
//  Jkpls
//
//  Created by Fábio Nogueira  on 10/10/14.
//  Copyright (c) 2014 Little Red Club. All rights reserved.
//

#import "BattleResultView.h"
#import <QuartzCore/QuartzCore.h>

@interface BattleResultView ()

@property (weak, nonatomic) IBOutlet UIView *viewReference;

@end


@implementation BattleResultView

#pragma mark - NSObject Methods -

- (instancetype)init {
    self = [super init];
    if (self) {
        self = [[[NSBundle mainBundle] loadNibNamed:@"BattleResultView" owner:self options:nil] objectAtIndex:0];
        self.alpha = 0.0f;
        self.clipsToBounds = YES;
        self.layer.cornerRadius = 20.f;
    }
    return self;
}

#pragma mark - IBOutlet Methods

- (IBAction)cancelarTouched:(id)sender {
    [UIView animateWithDuration:0.5 animations:^{
        self.alpha = 0.0f;
    }];
}

@end
