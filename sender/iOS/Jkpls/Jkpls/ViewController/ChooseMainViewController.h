//
//  MainViewController.h
//  Jkpls
//
//  Created by Jhonathan Wyterlin on 9/3/14.
//  Copyright (c) 2014 Little Red Club. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface ChooseMainViewController : UIViewController

@property (weak, nonatomic) IBOutlet UITextField *nomeTextField;

@property(nonatomic,strong) IBOutlet UILabel *lbSinglePlayer;
@property (weak, nonatomic) IBOutlet UIButton *btnSinglePlayer;

@property(nonatomic,strong) IBOutlet UILabel *lbMultiPlayer;
@property(nonatomic,strong) IBOutlet UIButton *btnPlayerVsCom;

@end
