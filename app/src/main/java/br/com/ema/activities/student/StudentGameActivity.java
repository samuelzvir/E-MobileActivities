package br.com.ema.activities.student;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import br.com.ema.R;
import br.com.ema.dialogs.EndGameDialog;
import br.com.ema.dialogs.FinishGameDialog;
import br.com.ema.dialogs.NoWordsRegisteredDialog;
import br.com.ema.entities.AppConfiguration;
import br.com.ema.entities.Challenge;
import br.com.ema.entities.PlayStats;
import br.com.ema.entities.Student;
import br.com.ema.services.Speaker;

import io.realm.Realm;
import me.grantland.widget.AutofitTextView;

public class StudentGameActivity extends Activity implements View.OnClickListener, View.OnTouchListener {

        private static final String TAG = "StudentGameActivity";
        private Vibrator vibrator;
        private final int CHECK_CODE = 0x1;
        private String word = new String();
        private List<Challenge> wordList;
        private Random generator = new Random();
        private String answerString = new String();
        private EditText answerText;
        private TextView info;
        private GridLayout scrambledLayout;
        private MediaPlayer correctSound;
        private MediaPlayer wrongSound;
        private Button nextButton;
        private Button clearButton;

        private static Speaker speaker;
        private ToggleButton toggle;
        private CompoundButton.OnCheckedChangeListener toggleListener;
        private Student student;
        private int counter = 0;
        private static int points = 0;
        private PlayStats activityStats;
        private boolean help;
        private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
            realm = Realm.getDefaultInstance();
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_student_game);
            vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            Intent intent = getIntent();
            String studentId = intent.getStringExtra("studentId");
            student = realm.where(Student.class).equalTo("id", studentId).findFirst();
            Log.i(TAG,"Student "+ student.getNickname()+", ID = "+studentId );
            Log.i(TAG,"With "+student.getChallenges().size()+" challenges.");
            nextButton = (Button) findViewById(R.id.nextButton);
            nextButton.setOnClickListener(this);
            clearButton = (Button) findViewById(R.id.clear);
            clearButton.setOnClickListener(this);
            answerText = (EditText) findViewById(R.id.answer);
            correctSound = MediaPlayer.create(getApplicationContext(), R.raw.correct);
            wrongSound = MediaPlayer.create(getApplicationContext(), R.raw.wrongsound);
            AppConfiguration config = realm.where(AppConfiguration.class).findFirst();
            this.help = config.getShowWord();
            this.wordList = student.getChallenges();
            if(wordList.size() > 0){
                startWord();
                Log.d(TAG, "creating activity stats...");
                this.activityStats = new PlayStats();
                this.activityStats.setStart(new Date());
                this.activityStats.setTotalPoints(0);
                Log.d(TAG, "created.");
            }else{
                //alert informing that the student does not have any words registered for him.
                NoWordsRegisteredDialog wordsRegisteredDialog = new NoWordsRegisteredDialog();
                FragmentManager fm = getFragmentManager();
                wordsRegisteredDialog.show(fm,"no words");
            }
        }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        if(speaker != null) {
            speaker.destroy();
        }
        realm.close();
    }

    @Override
    protected void onPause(){
        super.onPause();
        if(speaker != null){
            speaker.destroy();
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
    }

    /**
     * Method to setup the screen to type the word
     */
    public void startWord(){
        clearButton.setEnabled(Boolean.FALSE);
        Challenge challenge = getNewWord();
        if(challenge == null){
            //End of the game.
            realm = Realm.getDefaultInstance();
            EndGameDialog endGameDialog = new EndGameDialog();
            FragmentManager fm = getFragmentManager();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    activityStats.setEnd(new Date());
                    realm.insertOrUpdate(activityStats);
                }
            });
            endGameDialog.show(fm,"end");
        }else {
            word = challenge.getText();
            if(help){
                AutofitTextView helpText = (AutofitTextView) findViewById(R.id.helpText);
                helpText.setTextSize(75);
                helpText.setText(word.toUpperCase());
            }
            String scrambledWord = scramble(word);
            scrambledLayout = (GridLayout) findViewById(R.id.scrambled);
            setImageView(challenge.getImage(), word);

            info = (TextView) findViewById(R.id.information);
            Set<Character> letters = new HashSet<>();

            for (int i = 0; i < scrambledWord.length(); i++) {
                if(!letters.contains(scrambledWord.charAt(i))){
                    letters.add(scrambledWord.charAt(i));
                    //TODO add button
                    Button  button = new Button(this);
                    button.setId(i);
                    button.setText(Character.toString(scrambledWord.charAt(i)).toUpperCase());
                    button.setTextSize(55);
                    button.setOnTouchListener(this);
                    button.setOnClickListener(this);
                    //button.setBackgroundColor(Color.RED);
                    scrambledLayout.addView(button);
                }
            }
            toggle = (ToggleButton) findViewById(R.id.speechToggle);

            toggleListener = new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton view, boolean isChecked) {
                    if (isChecked) {
                        speaker.allow(true);
                        speaker.speak(word);
                    } else {
                        speaker.speak(word);
                        speaker.allow(false);
                    }
                }
            };
            toggle.setOnCheckedChangeListener(toggleListener);
            checkTTS();
        }
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
            //TODO
            startWord();
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
                        activityStats.addPoint(1);
                        points++;
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
                                startWord();
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
                        vibrator.vibrate(1500);

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
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


    public Challenge getNewWord(){
        if(wordList.size() > counter){
            //int randomWord = generator.nextInt(wordList.size());
            Challenge temp = wordList.get(counter);
            counter++;
            return temp;
        }
        else{
            return null;
        }

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
                //TODO check bug
                if(speaker == null){
                    speaker = new Speaker(this);
                }else{
                    speaker.destroy();
                    //TODO check bound error
                    speaker = new Speaker(this);
                }
            }else {
                Intent install = new Intent();
                install.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(install);
            }
        }
    }

    /**
     * Sets the image path and the content description
     * @param contentDescription
     */
    private void setImageView(byte[] image, String contentDescription) {
        if (image != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
            ImageView imageView = (ImageView) findViewById(R.id.scrambbleImage);
            String prefix = getString(R.string.imageOf);
            imageView.setContentDescription(prefix+contentDescription);
            imageView.setImageBitmap(bitmap);
        }
    }

    public void onFinish(View view){
        realm = Realm.getDefaultInstance();
        FinishGameDialog dialog = new FinishGameDialog();
        FragmentManager fm = getFragmentManager();

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                activityStats.setEnd(new Date());
                realm.insertOrUpdate(activityStats);
            }
        });
        dialog.show(fm, "finish the game");
    }

    public static int getPoints() {
        return points;
    }

    public static void setPoints(int points) {
        StudentGameActivity.points = points;
    }

}