package com.wingood.cordova.audiointerruption;

import org.apache.cordova.*;
import android.content.Context;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Date;

public class AudioInterruption extends CordovaPlugin {
    private CallbackContext callbackContext;
    private Context mContext;
    private boolean mListenerRegistered = false;
    private PhoneCallStateListener listener;

    @Override
    public boolean execute(String action, JSONArray data, CallbackContext callbackContext) throws JSONException {
        if (action.equals("addListener")) {
            addListener(callbackContext);
            return true;
        } else {
            return false;
        }
    }

    private void addListener(CallbackContext callbackContext) {
        if (!mListenerRegistered) {
            Context context = this.cordova.getActivity().getApplicationContext();

            this.callbackContext = callbackContext;
            this.mContext = context;

            listener = new PhoneCallStateListener();
            listener.setCallbackContext(this.callbackContext);
            TelephonyManager telephonyManager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
            telephonyManager.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
            this.mListenerRegistered = true;
        }
    }
}


class PhoneCallStateListener extends PhoneStateListener {
    private CallbackContext callbackContext;
    private static int lastState = TelephonyManager.CALL_STATE_IDLE;
    private static Date callStartTime;
    private static boolean isIncoming;
    private static String savedNumber;

    public void setCallbackContext(CallbackContext callbackContext) {
        this.callbackContext = callbackContext;
    }

    public void onCallStateChanged(int state, String number) {
        if(lastState == state){
            return;
        }

        String status = "";

        switch (state) {
            case TelephonyManager.CALL_STATE_RINGING:
                isIncoming = true;
                callStartTime = new Date();
                savedNumber = number;
                status = "INTERRUPTION_BEGIN"; // incoming call
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:
                if (lastState != TelephonyManager.CALL_STATE_RINGING) {
                    isIncoming = false;
                    callStartTime = new Date();
                    status = "INTERRUPTION_BEGIN"; // outgoing call
                }
                break;
            case TelephonyManager.CALL_STATE_IDLE:
                if (lastState == TelephonyManager.CALL_STATE_RINGING) {
                    status = "INTERRUPTION_ENDED"; // missed call
                } else if (isIncoming) {
                    status = "INTERRUPTION_ENDED"; // incoming ended
                } else {
                    status = "INTERRUPTION_ENDED"; // outgoing ended
                }
                break;
        }

        lastState = state;

        if (!status.isEmpty()) {
            this.sendStateInJS(status);
        }
    }

    private void sendStateInJS(String status) {
        if (this.callbackContext == null) return;
        PluginResult result = new PluginResult(PluginResult.Status.OK, status);
        result.setKeepCallback(true);
        this.callbackContext.sendPluginResult(result);
    }
}