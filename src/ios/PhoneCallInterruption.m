#import "PhoneCallInterruption.h"
#import <CoreTelephony/CTCallCenter.h>
#import <CoreTelephony/CTCall.h>

@implementation PhoneCallInterruption

- (void)pluginInitialize
{
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(CallStateDidChange:) name:@"CTCallStateDidChange" object:nil];
    
    self.objCallCenter = [[CTCallCenter alloc] init];
    self.objCallCenter.callEventHandler = ^(CTCall* call) {
        // anounce that we've had a state change in our call center
        NSDictionary *dict = [NSDictionary dictionaryWithObject:call.callState forKey:@"callState"];
        [[NSNotificationCenter defaultCenter] postNotificationName:@"CTCallStateDidChange" object:nil userInfo:dict];
    };
}

- (void)addListener:(CDVInvokedUrlCommand*)command
{
    NSString* msg = [NSString stringWithFormat: @"Hello"];

    successCallbackID = command.callbackId;
    
    [self sendDataToJS:@{@"status": msg}];
}


// Send any data back to JS env through subscribe callback
- (void) sendDataToJS: (NSDictionary*) dict {
    NSData *jsonData = [NSJSONSerialization dataWithJSONObject:dict options: 0 error: nil];
    NSString *jsonString = [[NSString alloc] initWithData:jsonData encoding:NSUTF8StringEncoding];
    
    plresult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:jsonString];
    [plresult setKeepCallbackAsBool:YES];
    [self.commandDelegate sendPluginResult:plresult callbackId:successCallbackID];
}

- (void)CallStateDidChange:(NSNotification *)notification
{
    NSLog(@"Notification : %@", notification);
    NSString *callInfo = [[notification userInfo] objectForKey:@"callState"];
    
    if([callInfo isEqualToString: CTCallStateDialing])
    {
        //The call state, before connection is established, when the user initiates the call.
        NSLog(@"Call is dailing");
    }
    if([callInfo isEqualToString: CTCallStateIncoming])
    {
        //The call state, before connection is established, when a call is incoming but not yet answered by the user.
        NSLog(@"Call is Coming");
    }
    
    if([callInfo isEqualToString: CTCallStateConnected])
    {
        //The call state when the call is fully established for all parties involved.
        NSLog(@"Call Connected");
    }
    
    if([callInfo isEqualToString: CTCallStateDisconnected])
    {
        //The call state Ended.
        NSLog(@"Call Ended");
    }
    
}

@end
