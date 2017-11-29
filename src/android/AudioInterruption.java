package com.wingood.cordova.audiointerruption;

import org.apache.cordova.*;
import android.content.Context;
import android.media.AudioManager;

import org.json.JSONArray;
import org.json.JSONException;

public class AudioInterruption extends CordovaPlugin {
    private CallbackContext callbackContext;
    private Context mContext;
    private boolean mAudioFocusGranted = false;
    private boolean mListenerRegistered = false;
    private AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener;

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

            mOnAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
                @Override
                public void onAudioFocusChange(int focusChange) {
                    switch (focusChange) {
                        case AudioManager.AUDIOFOCUS_LOSS:
                            sendStatusInJS("INTERRUPTION_BEGIN");
                            break;
                        case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                            sendStatusInJS("INTERRUPTION_BEGIN");
                            break;
                        case AudioManager.AUDIOFOCUS_GAIN:
                            sendStatusInJS("INTERRUPTION_ENDED");
                            break;
                        case AudioManager.AUDIOFOCUS_REQUEST_FAILED:
                            sendErrorInJS("AUDIOFOCUS_REQUEST_FAILED");
                            break;
                    }
                }
            };

            this.mListenerRegistered = true;
            this.requestAudioFocus();
        }
    }

    private void requestAudioFocus() {
        if (!mAudioFocusGranted) {
            // TODO add support for Android O, AudioFocusRequest.Builder
            AudioManager am = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
            int result = am.requestAudioFocus(mOnAudioFocusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
            if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                mAudioFocusGranted = true;
            } else {
                sendErrorInJS("AUDIOFOCUS_REQUEST_FAILED");
            }
        }
    }

    private void sendStatusInJS(String status) {
        if (this.callbackContext == null) return;
        PluginResult result = new PluginResult(PluginResult.Status.OK, status);
        result.setKeepCallback(true);
        this.callbackContext.sendPluginResult(result);
    }

    private void sendErrorInJS(String error) {
        if (this.callbackContext == null) return;
        PluginResult result = new PluginResult(PluginResult.Status.ERROR, error);
        result.setKeepCallback(true);
        this.callbackContext.sendPluginResult(result);
    }
}