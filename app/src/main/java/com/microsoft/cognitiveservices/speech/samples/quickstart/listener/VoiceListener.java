package com.microsoft.cognitiveservices.speech.samples.quickstart.listener;


import com.microsoft.cognitiveservices.speech.samples.quickstart.VoiceManagerFragment;

/**
 * Created by Admin on 11/14/2017.
 */

public interface VoiceListener {

    public void onVoiceStateListener(VoiceManagerFragment.VOICE_STATE voiceState);

}
