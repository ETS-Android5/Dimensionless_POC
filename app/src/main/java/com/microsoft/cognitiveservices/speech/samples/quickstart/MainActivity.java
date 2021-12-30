//
// Copyright (c) Microsoft. All rights reserved.
// Licensed under the MIT license. See LICENSE.md file in the project root for full license information.
//
// <code>
package com.microsoft.cognitiveservices.speech.samples.quickstart;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.CountDownTimer;
import android.speech.RecognitionListener;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.microsoft.cognitiveservices.speech.ResultReason;
import com.microsoft.cognitiveservices.speech.SpeechConfig;
import com.microsoft.cognitiveservices.speech.SpeechRecognitionResult;
import com.microsoft.cognitiveservices.speech.SpeechRecognizer;
import com.microsoft.cognitiveservices.speech.samples.quickstart.backgroundservice.BackgroundHTTPPostAsync;
import com.microsoft.cognitiveservices.speech.samples.quickstart.listener.OnBackgroundAsyncFinishListener;
import com.microsoft.cognitiveservices.speech.samples.quickstart.model.WordsModel;
import com.microsoft.cognitiveservices.speech.samples.quickstart.util.AppConstant;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import static android.Manifest.permission.*;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.ParseException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, OnBackgroundAsyncFinishListener {

    // Replace below with your own subscription key
    private static String speechSubscriptionKey = "Your Key";
    // Replace below with your own service region (e.g., "westus").
    private static String serviceRegion = "Your Region";
    TextView txt,onlyText,txtListening;
    private SpeechRecognizer mSpeechRecognizer;
    private Intent mSpeechRecognizerIntent;
    private boolean mIslistening;
    RelativeLayout rlMicImg;
    String strSpeechText = "";

    ImageView bgImg1;
    ImageView bgImg2;
    ImageView bgImg3;
    ImageView bgImg4;
    ImageView bgImg5;
    Animation animation1;
    Animation animation2;
    boolean flag = false;
    Ringtone r;
    MediaPlayer mPlayer2;
    BackgroundHTTPPostAsync backgroundHTTPPostAsync = null;
    List<WordsModel> wordsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        animation1 = AnimationUtils.loadAnimation(this, R.anim.mic_anim1);
        animation2 = AnimationUtils.loadAnimation(this, R.anim.mic_anim2);
        txt = (TextView) this.findViewById(R.id.hello); // 'hello' is the ID of your text view
        onlyText = (TextView) this.findViewById(R.id.txvResult);
        txtListening = (TextView) this.findViewById(R.id.txvlistening);
        rlMicImg = (RelativeLayout) this.findViewById(R.id.img_mic_rel);
        bgImg1= findViewById(R.id.bg1);
        bgImg2= findViewById(R.id.bg2);
        bgImg3= findViewById(R.id.bg3);
        bgImg4= findViewById(R.id.bg4);
        bgImg5= findViewById(R.id.bg5);

        bgImg1.setVisibility(View.VISIBLE);
        bgImg2.setVisibility(View.VISIBLE);
        bgImg3.setVisibility(View.VISIBLE);
        bgImg4.setVisibility(View.VISIBLE);
        bgImg5.setVisibility(View.VISIBLE);

        rlMicImg.setOnClickListener(this);

        // Note: we need to request the permissions
        int requestCode = 5; // unique code for the permission request
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{RECORD_AUDIO, INTERNET}, requestCode);
    }



    private void onSpeechButtonClicked() {

        try {

            //txtListening.setVisibility(View.VISIBLE);
            SpeechConfig config = SpeechConfig.fromSubscription(speechSubscriptionKey, serviceRegion);
            assert(config != null);

            SpeechRecognizer reco = new SpeechRecognizer(config);
            assert(reco != null);

            Future<SpeechRecognitionResult> task = reco.recognizeOnceAsync();
            assert(task != null);

            // Note: this will block the UI thread, so eventually, you want to
            //        register for the event (see full samples)
            SpeechRecognitionResult result = task.get();
            assert(result != null);

            if (result.getReason() == ResultReason.RecognizedSpeech) {
                onlyText.setText(result.getText());
                //txt.setText(result.toString());
                strSpeechText = result.getText();
                startWordCheckService();
                txtListening.setVisibility(View.GONE);

/*
                if(result.getText().equalsIgnoreCase(" credit card ") ||result.getText().equalsIgnoreCase(" Credit Card ")||result.getText().equalsIgnoreCase(" Credit card ")||
                        result.getText().equalsIgnoreCase("credit card ") ||result.getText().equalsIgnoreCase("Credit Card ")||result.getText().equalsIgnoreCase("Credit card ")||
                        result.getText().equalsIgnoreCase(" Card ")|| result.getText().equalsIgnoreCase(" card.")||result.getText().equalsIgnoreCase(" Card.")||
                        result.getText().equalsIgnoreCase(" OTP ")|| result.getText().equalsIgnoreCase(" Otp.") ||
                        result.getText().equalsIgnoreCase("Otp ") || result.getText().equalsIgnoreCase("Otp.") ||
                        result.getText().equalsIgnoreCase(" Pin ")|| result.getText().equalsIgnoreCase(" Pin.")||
                        result.getText().equalsIgnoreCase("Pin ")|| result.getText().equalsIgnoreCase("Pin.")||
                        result.getText().equalsIgnoreCase(" PIN ") ||result.getText().equalsIgnoreCase(" CVV ")||
                        result.getText().equalsIgnoreCase(" PIN.") ||result.getText().equalsIgnoreCase(" CVV.")||
                        result.getText().equalsIgnoreCase("PIN ") ||result.getText().equalsIgnoreCase("CVV ")||
                        result.getText().equalsIgnoreCase("PIN.") ||result.getText().equalsIgnoreCase("CVV.")||
                        result.getText().equalsIgnoreCase(" Cvv ")|| result.getText().equalsIgnoreCase(" Cvv.")||
                        result.getText().equalsIgnoreCase("Cvv ")||result.getText().equalsIgnoreCase("cvv.")){
                */
/*if((result.getText().indexOf("credit card"))!=-1 || (result.getText().indexOf("Credit Card"))!=-1 || (result.getText().indexOf("Credit card"))!=-1 ||
                        (result.getText().indexOf(" card "))!=-1||(result.getText().indexOf("Card"))!=-1||
                        (result.getText().indexOf("Otp"))!=-1 ||(result.getText().indexOf("OTP"))!=-1||
                        (result.getText().indexOf("Pin"))!=-1||(result.getText().indexOf("pin"))!=-1){*//*


                    mPlayer2= MediaPlayer.create(this, R.raw.warning_sound);
                    mPlayer2.start();

                    onlyText.setTextColor(Color.parseColor("#FF0000"));
                    Animation anim = new AlphaAnimation(0.0f, 1.0f);
                    anim.setDuration(50); //You can manage the blinking time with this parameter
                    anim.setStartOffset(20);
                    anim.setRepeatMode(Animation.REVERSE);
                    anim.setRepeatCount(Animation.INFINITE);
                    onlyText.startAnimation(anim);

                    new CountDownTimer(5000, 5000) {

                        public void onTick(long millisUntilFinished) {
                            // You don't need to use this.
                        }

                        public void onFinish() {
                            // Put the code to stop the GIF here.
                            //r.stop();
                            mPlayer2.stop();
                            onlyText.clearAnimation();
                            onlyText.setTextColor(Color.parseColor("#000000"));
                        }

                    }.start();



                }
*/
                //setGif();

            }
            else {
                //txt.setText("Error recognizing. Did you update the subscription info?" + System.lineSeparator() + result.toString());
                txt.setText("Sorry Cannot Understand");
                onlyText.setText("");
                //setGif();
                txtListening.setVisibility(View.GONE);
            }

            reco.close();

        } catch (Exception ex) {
            Log.e("SpeechSDKDemo", "unexpected " + ex.getMessage());
            assert(false);
        }
    }

    private void stopGif(){

        bgImg2.clearAnimation();
        bgImg3.clearAnimation();
        bgImg4.clearAnimation();

    }


    private void setStaticRotate(){
        bgImg2.startAnimation(animation1);
        bgImg3.startAnimation(animation2);
        bgImg4.startAnimation(animation1);
        //onSpeechButtonClicked();
    }

    @Override
    public void onClick(View view) {
        txt.setText("");
        onlyText.setText("");
        txtListening.setVisibility(View.VISIBLE);
        //setStaticRotate();

        new CountDownTimer(1000, 1000) {

            public void onTick(long millisUntilFinished) {
                // You don't need to use this.
            }

            public void onFinish() {
                // Put the code to stop the GIF here.
                onSpeechButtonClicked();
            }

        }.start();



        //setStaticRotate();
    }
    private void startWordCheckService(){
        //if(Utility.isNetworkConnected(this)){
        //waitRLayout.setVisibility(View.VISIBLE);
        JSONObject rootObj = new JSONObject();

        try {

            rootObj.put("text", strSpeechText);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        if(backgroundHTTPPostAsync==null) {
            new BackgroundHTTPPostAsync(rootObj.toString(), AppConstant.URL_ALERT_WORD,
                    this, AppConstant.RES_CODE_ALERT_WORD).execute();
        }

       /* }else{
            waitRLayout.setVisibility(View.GONE);
            Utility.showToast(this,AppConstant.CONS_NO_INTERNET_CONNECTION);
        }*/

    }


    @Override
    public void onBackgroundFinish(String result, int responseCode) {

        if (responseCode == AppConstant.RES_CODE_ALERT_WORD) {
            if (result != null) {
                JSONObject resultObj = null;

                wordsList = new ArrayList<>();
                try {

                    resultObj = new JSONObject(result);
                    String statusCode = resultObj.getString("StatusCode");

                    JSONArray wordsArray = resultObj.getJSONArray("Alert_words");
                    for(int i=0;i<wordsArray.length();i++){
                        JSONObject itemObj = wordsArray.getJSONObject(i);
                        WordsModel wordsModel=new WordsModel();
                        wordsModel.setWord(itemObj.getString("Word"));
                        wordsList.add(wordsModel);
                    }

                    if(wordsList.size()>0){
                        mPlayer2= MediaPlayer.create(this, R.raw.warning_sound);
                        mPlayer2.start();

                        onlyText.setTextColor(Color.parseColor("#FF0000"));
                        Animation anim = new AlphaAnimation(0.0f, 1.0f);
                        anim.setDuration(50); //You can manage the blinking time with this parameter
                        anim.setStartOffset(20);
                        anim.setRepeatMode(Animation.REVERSE);
                        anim.setRepeatCount(Animation.INFINITE);
                        onlyText.startAnimation(anim);

                        new CountDownTimer(5000, 5000) {

                            public void onTick(long millisUntilFinished) {
                                // You don't need to use this.
                            }

                            public void onFinish() {
                                // Put the code to stop the GIF here.
                                //r.stop();
                                mPlayer2.stop();
                                onlyText.clearAnimation();
                                onlyText.setTextColor(Color.parseColor("#000000"));
                            }

                        }.start();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                backgroundHTTPPostAsync = null;
            }
        }
    }
}
// </code>
