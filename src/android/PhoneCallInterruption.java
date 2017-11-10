package com.wingood.cordova.callinterruption;

import org.apache.cordova.*;

import android.content.Context;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import org.json.JSONArray;
import org.json.JSONException;

public class PhoneCallInterruption extends CordovaPlugin {
    CallStateListener listener;

    @Override
    public boolean execute(String action, JSONArray data, CallbackContext callbackContext) throws JSONException {
        if (action.equals("onCall")) {
            prepareListener();
            listener.setCallbackContext(callbackContext);
            return true;
        } else {
            return false;
        }
    }

    private void prepareListener() {
        if (listener == null) {
            listener = new CallStateListener();
            TelephonyManager TelephonyMgr = (TelephonyManager) cordova.getActivity().getSystemService(Context.TELEPHONY_SERVICE);
            TelephonyMgr.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
        }
    }
}

class CallStateListener extends PhoneStateListener {
    private CallbackContext callbackContext;
    private int prevState;

    public void setCallbackContext(CallbackContext callbackContext) {
        this.callbackContext = callbackContext;
    }

    public void onCallStateChanged(int state, String incomingNumber) {
        super.onCallStateChanged(state, incomingNumber);

        if (callbackContext == null) return;

        String msg = "";

        switch (state) {
            case TelephonyManager.CALL_STATE_IDLE:
                if ((prevState == TelephonyManager.CALL_STATE_OFFHOOK)) {
                    // A call has now ended
                    prevState = state;
                    msg = "ENDED";
                } else if ((prevState == TelephonyManager.CALL_STATE_RINGING)) {
                    // Rejected or Missed call
                    prevState = state;
                    msg = "ENDED";
                }
            break;

            case TelephonyManager.CALL_STATE_OFFHOOK:
                msg = "OFFHOOK";
                prevState = state;
            break;

            case TelephonyManager.CALL_STATE_RINGING:
                msg = "RINGING";
                prevState = state;
            break;
        }

        if(msg != null && !msg.isEmpty()) {
            PluginResult result = new PluginResult(PluginResult.Status.OK, msg);
            result.setKeepCallback(true);

            callbackContext.sendPluginResult(result);
        }
    }
}
