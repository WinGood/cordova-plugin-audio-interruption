#import "PhoneCallInterruption.h"
#import <CoreTelephony/CTCallCenter.h>
#import <CoreTelephony/CTCall.h>

@implementation PhoneCallInterruption

- (void)pluginInitialize
{
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(callStateDidChange:) name:@"CTCallStateDidChange" object:nil];
    
    self.objCallCenter = [[CTCallCenter alloc] init];
    self.objCallCenter.callEventHandler = ^(CTCall* call) {
        // anounce that we've had a state change in our call center
        NSDictionary *dict = [NSDictionary dictionaryWithObject:call.callState forKey:@"callState"];
        [[NSNotificationCenter defaultCenter] postNotificationName:@"CTCallStateDidChange" object:nil userInfo:dict];
    };
}

- (void)onCalling:(CDVInvokedUrlCommand*)command
{
    successCallbackID = command.callbackId;
}


// Send any data back to JS env through subscribe callback
- (void) sendStatusNameInJS: (NSString*) status {
    plresult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:status];
    [plresult setKeepCallbackAsBool:YES];
    [self.commandDelegate sendPluginResult:plresult callbackId:successCallbackID];
}

- (void) callStateDidChange:(NSNotification *)notification
{
    NSString *callInfo = [[notification userInfo] objectForKey:@"callState"];
    
    if([callInfo isEqualToString: CTCallStateDialing]) {
        // The call state, before connection is established, when the user initiates the call.
        [self sendStatusNameInJS:@"RINGING"];
    }
    
    if([callInfo isEqualToString: CTCallStateIncoming]) {
        // The call state, before connection is established, when a call is incoming but not yet answered by the user.
        [self sendStatusNameInJS:@"RINGING"];
    }
    
    if([callInfo isEqualToString: CTCallStateConnected]) {
        // The call state when the call is fully established for all parties involved.
        [self sendStatusNameInJS:@"OFFHOOK"];
    }
    
    
    if([callInfo isEqualToString: CTCallStateDisconnected]) {
        // The call state ended.
        [self sendStatusNameInJS:@"IDLE"];
    }
}

@end
