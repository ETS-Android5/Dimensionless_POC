package com.microsoft.cognitiveservices.speech.samples.quickstart;

import android.app.Activity;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.support.v4.app.Fragment;

import com.microsoft.cognitiveservices.speech.samples.quickstart.listener.VoiceListener;

import java.util.ArrayList;

/**
 * Created by Admin on 11/14/2017.
 */

public abstract class VoiceManagerFragment extends Fragment implements VoiceListener {

    private static final int REQ_CODE_SPEECH_INPUT = 100;

    private VOICE_STATE currentVoiceState = VOICE_STATE.INACTIVE;

    //DefaultVoiceAPI defaultVoiceAPI;


    public abstract void onMicStateListener(VOICE_STATE voice_state);


    public enum VOICE_STATE {


        INACTIVE(1),
        ACTIVE(2),
        PROCESS(3),
        SPEAKING(4),
        LISTENING(5);

        private int value;

        private VOICE_STATE(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }


    }

    public final VOICE_STATE getVoiceState() {

        return currentVoiceState;
    }


    public final void holdMic(String title) {
    }

    public final void tabMic(String title) {
        currentVoiceState = VOICE_STATE.ACTIVE;
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
       // super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == Activity.RESULT_OK && null != data) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    //mVoiceInputTv.setText(result.get(0));
                   // Toast.makeText(getActivity(),result.get(0),Toast.LENGTH_SHORT).show();

                  /*  if (defaultVoiceAPI == null) {
                        defaultVoiceAPI = new DefaultVoiceAPI(this, this);
                    }
                    defaultVoiceAPI.processText(result.get(0));*/

                }
                break;
            }

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
       /* if (defaultVoiceAPI != null) {
            defaultVoiceAPI.destroyTTS();
        }*/
    }

    @Override
    public void onVoiceStateListener(final VOICE_STATE voiceState) {

        currentVoiceState = voiceState;
        if(getActivity()!=null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    onMicStateListener(voiceState);
                }
            });
        }
    }
}