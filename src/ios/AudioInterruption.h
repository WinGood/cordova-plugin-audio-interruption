#import <Cordova/CDV.h>
#import <AVFoundation/AVFoundation.h>

@interface AudioInterruption : CDVPlugin {
    NSString *successCallbackID;
    CDVPluginResult *plresult;
}

- (void) addListener:(CDVInvokedUrlCommand*)command;

@end