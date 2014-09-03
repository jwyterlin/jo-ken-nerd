//
//  MainViewController.h
//  Jkpls
//
//  Created by Jhonathan Wyterlin on 9/3/14.
//  Copyright (c) 2014 Little Red Club. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface MainViewController : UIViewController

@property(nonatomic,strong) IBOutlet UILabel *lbSinglePlayer;
@property(nonatomic,strong) IBOutlet UILabel *lbMultiPlayer;

@property(nonatomic,strong) IBOutlet UIButton *btnPlayerVsCom;
@property(nonatomic,strong) IBOutlet UIButton *btnChromeCast;
@property(nonatomic,strong) IBOutlet UIButton *btnWifi;
@property(nonatomic,strong) IBOutlet UIButton *btnBluetooth;

@end
