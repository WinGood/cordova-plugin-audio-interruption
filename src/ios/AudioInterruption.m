#import "AudioInterruption.h"

@implementation AudioInterruption

- (void) pluginInitialize
{
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(onAudioSessionEvent:) name:AVAudioSessionInterruptionNotification object:nil];
}

- (void) addListener:(CDVInvokedUrlCommand*)command
{
    successCallbackID = command.callbackId;
}

// Send status back to JS env through subscribe callback
- (void) sendStatusNameInJS: (NSString*) status {
    plresult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:status];
    [plresult setKeepCallbackAsBool:YES];
    [self.commandDelegate sendPluginResult:plresult callbackId:successCallbackID];
}

- (void) onAudioSessionEvent: (NSNotification *) notification
{
    if ([notification.name isEqualToString:AVAudioSessionInterruptionNotification]) {
        // Check to see if it was a Begin interruption
        if ([[notification.userInfo valueForKey:AVAudioSessionInterruptionTypeKey] isEqualToNumber:[NSNumber numberWithInt:AVAudioSessionInterruptionTypeBegan]]) {
            [self sendStatusNameInJS:@"INTERRUPTION_BEGIN"];
        } else {
            [self sendStatusNameInJS:@"INTERRUPTION_ENDED"];
        }
    }
}

@end
