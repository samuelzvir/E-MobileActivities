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

package br.com.samuelzvir.meuabc.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.raizlabs.android.dbflow.sql.language.Select;

import java.io.File;
import java.util.List;
import java.util.Random;

import br.com.samuelzvir.meuabc.R;
import br.com.samuelzvir.meuabc.entities.SimpleChallenge;
import br.com.samuelzvir.meuabc.services.Speaker;

public class ScrabbleActivity extends Activity implements View.OnClickListener, View.OnTouchListener {

    private static final String TAG = "ScrabbleActivity";
    private final int CHECK_CODE = 0x1;
    private String word = new String();
    private List<SimpleChallenge> wordList;
    private Random generator = new Random();
    private String answerString = new String();
    private EditText answerText;
    private TextView info;
    private LinearLayout scrambledLayout;
    private MediaPlayer correctSound;
    private MediaPlayer wrongSound;
    private Button nextButton;
    private Button clearButton;

    private Speaker speaker;
    private ToggleButton toggle;
    private CompoundButton.OnCheckedChangeListener toggleListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrabble);

        nextButton = (Button) findViewById(R.id.nextButton);
        nextButton.setOnClickListener(this);
        clearButton = (Button) findViewById(R.id.clear);
        clearButton.setOnClickListener(this);
        answerText = (EditText) findViewById(R.id.answer);
        correctSound = MediaPlayer.create(getApplicationContext(), R.raw.correct);
        wrongSound = MediaPlayer.create(getApplicationContext(), R.raw.wrongsound);
        this.wordList = listSimpleChallenges();
        if(wordList.size() > 0){
            initialize();
        }
    }


    public void initialize(){
        clearButton.setEnabled(Boolean.FALSE);
        SimpleChallenge simpleChallenge = getNewWordByRandon();
        word = simpleChallenge.getWord();
        String scrambledWord = scramble(word);
        scrambledLayout = (LinearLayout) findViewById(R.id.scrambled);
        setImageView(simpleChallenge.getImagePath());
        info = (TextView) findViewById(R.id.information);

        for(int i = 0; i < scrambledWord.length(); i++) {
            TextView letter = new TextView(this);
            letter.setText("");
            letter.setText(Character.toString(scrambledWord.charAt(i)));
            letter.setTextSize(75);
            letter.setPadding(7, 7, 7, 7);
            letter.setOnClickListener(this);
            letter.setId(i);
            letter.setOnTouchListener(this);
            scrambledLayout.addView(letter);
        }
        toggle = (ToggleButton)findViewById(R.id.speechToggle);

        toggleListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton view, boolean isChecked) {
                if(isChecked){
                    speaker.allow(true);
                    //speaker.speak(getString(R.string.start_speaking));
                    speaker.speak(word);
                }else{
                    speaker.speak(getString(R.string.stop_speaking));
                    speaker.allow(false);
                }
            }
        };
        toggle.setOnCheckedChangeListener(toggleListener);
        checkTTS();
    }


    public void onClick(View v){
        TextView clicked = (TextView) findViewById(v.getId());

        if(answerText.getText().toString().length() == 0){
            clearButton.setEnabled(Boolean.FALSE);
        }

        if(clearButton.getId() == v.getId()){
            if(answerText.getText().toString().length() == 1){
                clearButton.setEnabled(Boolean.FALSE);
            }
            answerText.setText(answerText.getText().toString().substring(0, answerText.getText().toString().length()-1));
            answerString = answerString.substring(0, answerString.length()-1);
        }

        else if(nextButton.getId() == v.getId()) {
            clearButton.setEnabled(Boolean.FALSE);
            scrambledLayout.removeAllViews();
            answerText.setText("");
            answerString = "";
            answerText.setTextColor(Color.rgb(0, 0, 0));
            info.setTextColor(Color.DKGRAY);
            info.setText(R.string.build_the_words);
            info.setBackgroundColor(Color.TRANSPARENT);
            initialize();
        }
        else {
            clearButton.setEnabled(Boolean.TRUE);
            answerText.setText(answerText.getText().toString() + clicked.getText());
            answerString += clicked.getText();

            try {
                if(answerString.length() == word.length()){
                    if(answerString.equalsIgnoreCase(word)){
                        clearButton.setEnabled(Boolean.FALSE);
                        info.setBackgroundColor(Color.rgb(33, 196, 18));
                        info.setTextColor(Color.WHITE);
                        info.setText(R.string.correct);
                        answerText.setTextColor(Color.rgb(33, 196, 18));
                        correctSound.start();

                        new Handler().postDelayed(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                scrambledLayout.removeAllViews();
                                answerText.setText("");
                                answerString = "";
                                answerText.setTextColor(Color.rgb(0, 0, 0));
                                info.setTextColor(Color.DKGRAY);
                                info.setText(R.string.build_the_words);
                                info.setBackgroundColor(Color.TRANSPARENT);
                                initialize();
                            }
                        }, 1200);
                    }
                    else {
                        clearButton.setEnabled(Boolean.FALSE);
                        info.setBackgroundColor(Color.rgb(255, 0, 0));
                        info.setTextColor(Color.WHITE);
                        info.setText(R.string.wrong);
                        answerText.setTextColor(Color.rgb(255, 0, 0));
                        wrongSound.start();
                        //vibrator.vibrate(400);

                        new Handler().postDelayed(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                answerString = "";
                                answerText.setText("");
                                answerText.setTextColor(Color.rgb(0, 0, 0));
                                info.setTextColor(Color.DKGRAY);
                                info.setText(R.string.try_again);
                                info.setBackgroundColor(Color.TRANSPARENT);
                            }
                        }, 1200);
                    }
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scrabble, menu);
        return true;
    }


    public SimpleChallenge getNewWordByRandon(){
        int randomWord = generator.nextInt(wordList.size());
        SimpleChallenge temp = wordList.get(randomWord);
        return temp;
    }

    /**
     * Scramble the String
     */
    public String scramble(String wordToScramble){
        String scrambled = "";
        int randomNumber;

        boolean letter[] = new boolean[wordToScramble.length()];
        if(letter.length == 1){
            return wordToScramble;
        }
        do {
            randomNumber = generator.nextInt(wordToScramble.length());
            if(letter[randomNumber] == false){
                scrambled += wordToScramble.charAt(randomNumber);
                letter[randomNumber] = true;
            }
        } while(scrambled.length() < wordToScramble.length());

        if(scrambled.equals(wordToScramble))
            scramble(word);

        return scrambled;
    }

    @Override
    public boolean onTouch(View v, MotionEvent motion) {
        TextView touched = (TextView) findViewById(v.getId());

        if(motion.getAction() == MotionEvent.ACTION_DOWN){
            touched.setTextColor(Color.rgb(0, 189, 252));
        }
        else if(motion.getAction() == MotionEvent.ACTION_UP){
            touched.setTextColor(Color.rgb(0, 0, 0));
        }
        return false;
    }


    private void checkTTS(){
        Intent check = new Intent();
        check.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(check, CHECK_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == CHECK_CODE){
            if(resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS){
                speaker = new Speaker(this);
            }else {
                Intent install = new Intent();
                install.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(install);
            }
        }
    }

    private List<SimpleChallenge> listSimpleChallenges(){
        return new Select().from(SimpleChallenge.class).queryList();
    }

    private void setImageView(String path) {
        Log.d(TAG, "Adding image " + path);
        if(path != null && !path.isEmpty()){
            File imgFile = new File(path);
            if (imgFile.exists()) {
                Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                ImageView imageView = (ImageView) findViewById(R.id.scrambbleImage);
                imageView.setImageBitmap(bitmap);
            }
        }
    }

}
