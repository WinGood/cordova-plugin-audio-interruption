#import <Cordova/CDV.h>
#import <CoreTelephony/CTCallCenter.h>
#import <CoreTelephony/CTCall.h>

@interface PhoneCallInterruption : CDVPlugin {
    NSString *successCallbackID;
    CDVPluginResult *plresult;
}

@property (nonatomic, strong) CTCallCenter *objCallCenter;

- (void) onCalling:(CDVInvokedUrlCommand*)command;

@end