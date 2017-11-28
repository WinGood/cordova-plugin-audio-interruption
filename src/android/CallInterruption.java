package com.wingood.cordova.audiointerruption;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;

import java.util.Date;

/**
 * Created by zzzaki on 29/11/2017.
 */
public class CallInterruption extends BroadcastReceiver {

  private static int lastState = TelephonyManager.CALL_STATE_IDLE;
  private static Date callStartTime;
  private static boolean isIncoming;
  private static String savedNumber;
  private CallbackContext callbackContext;

  public void setCallbackContext(CallbackContext callbackContext) {
    this.callbackContext = callbackContext;
    this.sendMessage("setCallbackContext");
  }

  @Override
  public void onReceive(Context context, Intent intent) {
    this.sendMessage(intent.getAction());
    //We listen to two intents.  The new outgoing call only tells us of an outgoing call.  We use it to get the number.
    if (intent.getAction().equals("android.intent.action.NEW_OUTGOING_CALL")) {
      savedNumber = intent.getExtras().getString("android.intent.extra.PHONE_NUMBER");
    }
    else{
      String stateStr = intent.getExtras().getString(TelephonyManager.EXTRA_STATE);
      String number = intent.getExtras().getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
      int state = 0;
      if(stateStr.equals(TelephonyManager.EXTRA_STATE_IDLE)){
        state = TelephonyManager.CALL_STATE_IDLE;
      }
      else if(stateStr.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)){
        state = TelephonyManager.CALL_STATE_OFFHOOK;
      }
      else if(stateStr.equals(TelephonyManager.EXTRA_STATE_RINGING)){
        state = TelephonyManager.CALL_STATE_RINGING;
      }


      onCallStateChanged(context, state, number);
    }
  }

  protected void sendMessage(String message) {
    if (callbackContext == null) return;
    PluginResult result = new PluginResult(PluginResult.Status.OK, message);
    result.setKeepCallback(true);
    callbackContext.sendPluginResult(result);
  }

  public void onCallStateChanged(Context context, int state, String number) {
    if(lastState == state){
      //No change, debounce extras
      return;
    }
    switch (state) {
      case TelephonyManager.CALL_STATE_RINGING:
        isIncoming = true;
        callStartTime = new Date();
        savedNumber = number;
        sendMessage("INCOMING_CALL_STARTED");
        break;
      case TelephonyManager.CALL_STATE_OFFHOOK:
        //Transition of ringing->offhook are pickups of incoming calls.  Nothing done on them
        if(lastState != TelephonyManager.CALL_STATE_RINGING){
          isIncoming = false;
          callStartTime = new Date();
          sendMessage("OUTCOMING_CALL_STARTED");
        }
        break;
      case TelephonyManager.CALL_STATE_IDLE:
        //Went to idle-  this is the end of a call.  What type depends on previous state(s)
        if(lastState == TelephonyManager.CALL_STATE_RINGING){
          //Ring but no pickup-  a miss
          sendMessage("MISSED_CALL");
        }
        else if(isIncoming){
          sendMessage("INCOMING_CALL_ENDED");
        }
        else{
          sendMessage("OUTCOMING_CALL_ENDED");
        }
        break;
    }
    lastState = state;
  }

}