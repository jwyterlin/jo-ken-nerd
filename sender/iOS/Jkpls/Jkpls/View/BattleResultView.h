//
//  BattleResultView.h
//  Jkpls
//
//  Created by Fábio Nogueira  on 10/10/14.
//  Copyright (c) 2014 Little Red Club. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface BattleResultView : UIView

@property (weak, nonatomic) IBOutlet UILabel *titleLabel;
@property (weak, nonatomic) IBOutlet UILabel *descriptionLabel;

@property (weak, nonatomic) IBOutlet UIImageView *myPlayer;
@property (weak, nonatomic) IBOutlet UIImageView *OtherPlayer;

- (IBAction)cancelarTouched:(id)sender;

@end
