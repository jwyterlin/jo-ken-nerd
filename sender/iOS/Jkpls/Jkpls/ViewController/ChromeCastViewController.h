//
//  ChromeCastViewController.h
//  Jkpls
//
//  Created by Fábio Nogueira  on 05/07/14.
//  Copyright (c) 2014 Jhonathan Wyterlin. All rights reserved.
//

#import <UIKit/UIKit.h>

#import "GoogleCast.h"

@interface ChromeCastViewController : UIViewController <GCKDeviceScannerListener, UIActionSheetDelegate>

@property(nonatomic,strong) IBOutlet UITableView *tableView;

@end
