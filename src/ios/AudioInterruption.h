#import <Cordova/CDV.h>
#import <CoreTelephony/CTCallCenter.h>
#import <CoreTelephony/CTCall.h>

@interface AudioInterruption : CDVPlugin {
    NSString *successCallbackID;
    CDVPluginResult *plresult;
}

@property (nonatomic, strong) CTCallCenter *objCallCenter;

- (void) addListener:(CDVInvokedUrlCommand*)command;

@end