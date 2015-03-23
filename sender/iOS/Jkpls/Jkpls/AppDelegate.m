//
//  AppDelegate.m
//  Jkpls
//
//  Created by Jhonathan Wyterlin on 05/07/14.
//  Copyright (c) 2014 Jhonathan Wyterlin. All rights reserved.
//

#import "AppDelegate.h"
#import <GoogleAnalytics-iOS-SDK/GAI.h>

@interface AppDelegate ()

- (void)_setupAnalytics;
- (void)_setupStyleSheet;

@end

@implementation AppDelegate

- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions {
    [self _setupStyleSheet];
    [self _setupAnalytics];
    return YES;
}
							
- (void)applicationWillResignActive:(UIApplication *)application {}

- (void)applicationDidEnterBackground:(UIApplication *)application {}

- (void)applicationWillEnterForeground:(UIApplication *)application {}

- (void)applicationDidBecomeActive:(UIApplication *)application {}

- (void)applicationWillTerminate:(UIApplication *)application {}

#pragma mark - Private Methods - 


- (void)_setupAnalytics {
    GAI *gai = [GAI sharedInstance];
    gai.trackUncaughtExceptions = YES;
    gai.dispatchInterval = 20;
    [gai trackerWithTrackingId:@"UA-59217890-1"];
}

- (void)_setupStyleSheet {
    UIImage *backgroundImage = [UIImage imageNamed:@"navigation_bg"];
    [[UINavigationBar appearance] setBackgroundImage:backgroundImage
                                       forBarMetrics:UIBarMetricsDefault];
    [[UIToolbar appearance] setBackgroundImage:backgroundImage
                            forToolbarPosition:UIBarPositionAny
                                    barMetrics:UIBarMetricsDefault];
    [[UIApplication sharedApplication] setStatusBarStyle:UIStatusBarStyleLightContent];
}

@end
