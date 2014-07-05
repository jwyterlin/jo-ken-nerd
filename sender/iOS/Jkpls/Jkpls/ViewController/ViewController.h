//
//  ViewController.h
//  Jkpls
//
//  Created by Jhonathan Wyterlin on 05/07/14.
//  Copyright (c) 2014 Jhonathan Wyterlin. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "ChromeCast.h"

@interface ViewController : UIViewController<ChromeCastDelegate>

@property (nonatomic, strong) ChromeCast *chromeCast;
@property (weak, nonatomic) IBOutlet UIButton *chromeCastTouched;

@end
