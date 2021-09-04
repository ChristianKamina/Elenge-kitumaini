package com.youthfimodd.elenges.services;

import android.content.Context;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.widget.Toast;

import java.util.Locale;

public class TextSpeach {

    private TextToSpeech textToSpeech;
    Context context;

    public TextSpeach(Context context) {
        this.context = context;
    }
    public void SpeachTexte(final String message)
    {

        textToSpeech=new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status==TextToSpeech.SUCCESS){

                    int result=textToSpeech.setLanguage(Locale.FRENCH);
                    if(result==TextToSpeech.LANG_MISSING_DATA || result==TextToSpeech.LANG_NOT_SUPPORTED){
                        Toast.makeText(context, "cette langue ne pas supporte", Toast.LENGTH_SHORT).show();
                    }
                    else {
//                              textToSpeech.setPitch(0.2f);
//                              textToSpeech.setSpeechRate(1.5f);
                        speak(message);
                    }

                }
            }
        });
    }

    public void stopping(){
        textToSpeech.stop();
    }

    private void speak(String message) {
        if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.LOLLIPOP)
            textToSpeech.speak(message,TextToSpeech.QUEUE_FLUSH,null,null);
        else
            textToSpeech.speak(message,TextToSpeech.QUEUE_FLUSH,null);
    }

}
