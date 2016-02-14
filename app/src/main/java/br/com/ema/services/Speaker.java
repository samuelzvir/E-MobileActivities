/*
 * Copyright 2015 Samuel Yuri Zvir
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package br.com.ema.services;

import android.content.Context;
import android.media.AudioManager;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;

import org.litepal.crud.DataSupport;

import java.util.HashMap;
import java.util.Locale;

import br.com.ema.entities.AppConfiguration;

public class Speaker implements OnInitListener {

    private TextToSpeech tts;

    private boolean ready = false;

    private boolean allowed = false;

    public Speaker(Context context){
        tts = new TextToSpeech(context, this);
    }

    public boolean isAllowed(){
        return allowed;
    }

    public void allow(boolean allowed){
        this.allowed = allowed;
    }

    @Override
    public void onInit(int status) {
        if(status == TextToSpeech.SUCCESS){
            String language = getLanguage();
            if(language.equalsIgnoreCase("pt-br")){
                Locale locale = new Locale("BR");
                tts.setLanguage(locale);
            }
            else if(language.equalsIgnoreCase("en-us")){
                tts.setLanguage(Locale.US);
            }
            ready = true;
        }else{
            ready = false;
        }
    }

    @SuppressWarnings("deprecation")
    public void speak(String text){

        // Speak only if the TTS is ready
        // and the user has allowed speech

        if(ready && allowed) {
            HashMap<String, String> hash = new HashMap<String,String>();
            hash.put(TextToSpeech.Engine.KEY_PARAM_STREAM,
                    String.valueOf(AudioManager.STREAM_NOTIFICATION));
            tts.speak(text, TextToSpeech.QUEUE_ADD, hash);
        }
    }

    @SuppressWarnings("deprecation")
    public void pause(int duration){
        tts.playSilence(duration, TextToSpeech.QUEUE_ADD, null);
    }
    // Free up resources
    public void destroy(){
        tts.shutdown();
    }

    private String getLanguage(){
        AppConfiguration appConfiguration = DataSupport.findFirst(AppConfiguration.class);
        return appConfiguration.getLanguage();
    }

}
