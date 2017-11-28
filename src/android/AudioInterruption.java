package com.wingood.cordova.audiointerruption;

import org.apache.cordova.*;

import android.content.Context;
import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;

import org.json.JSONArray;
import org.json.JSONException;

public class AudioInterruption extends CordovaPlugin {
    AudioStateListener listener;
    CallInterruption listener2;
    AudioManager audioManager;

    @Override
    public boolean execute(String action, JSONArray data, CallbackContext callbackContext) throws JSONException {
        if (action.equals("addListener")) {
            prepareListener(callbackContext);
            return true;
        } else {
            return false;
        }
    }

    private void prepareListener(CallbackContext callbackContext) {
        if (listener == null) {
            listener2 = new CallInterruption();
            listener2.setCallbackContext(callbackContext);
            // listener = new AudioStateListener();
            // listener.setCallbackContext(callbackContext);

            // Context context = this.cordova.getActivity().getApplicationContext();
            // audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            // TODO add error handler
            // audioManager.requestAudioFocus(listener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        }
    }
}

class AudioStateListener implements OnAudioFocusChangeListener {
    private CallbackContext callbackContext;

    public void setCallbackContext(CallbackContext callbackContext) {
        this.callbackContext = callbackContext;
    }

    @Override
    public void onAudioFocusChange(int focusChange) {
        String status = "";
        switch (focusChange) {
            case AudioManager.AUDIOFOCUS_LOSS:
                status = "INTERRUPTION_BEGIN";
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                status = "INTERRUPTION_BEGIN";
                break;
            case AudioManager.AUDIOFOCUS_GAIN:
                status = "INTERRUPTION_ENDED";
                break;
        }

        if(status != null && !status.isEmpty()) {
            PluginResult result = new PluginResult(PluginResult.Status.OK, status);
            result.setKeepCallback(true);

            callbackContext.sendPluginResult(result);
        }
    }
}