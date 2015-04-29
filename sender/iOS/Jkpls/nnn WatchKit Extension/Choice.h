//
//  Choice.h
//  Jkpls
//
//  Created by Jhonathan Wyterlin on 25/04/15.
//  Copyright (c) 2015 Little Red Club. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

@interface Choice : NSObject

@property(nonatomic,strong) NSNumber *identifier;
@property(nonatomic,strong) NSString *name;
@property(nonatomic,strong) UIImage *image;

@end
