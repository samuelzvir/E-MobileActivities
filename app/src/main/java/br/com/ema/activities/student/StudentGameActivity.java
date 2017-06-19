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
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
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

public class StudentGameActivity extends Activity implements View.OnClickListener, View.OnTouchListener {
        private static final String TAG = "StudentGameActivity";
        private Vibrator vibrator;
        private final int CHECK_CODE = 0x1;
        private String word = "";
        private List<Challenge> wordList;
        private Random generator = new Random();
        private String answerString = "";
        private EditText answerText;
        private LinearLayout scrambledLayout;
        private MediaPlayer correctSound;
        private MediaPlayer wrongSound;
        private Button nextButton;
        private ImageButton clearButton;
        private Student student;
        private int counter = 0;
        private static int points = 0;
        private PlayStats activityStats;
        private boolean help;
        private Realm realm;
        private Speaker speaker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        realm = Realm.getDefaultInstance();
        super.onCreate(savedInstanceState);
        //start speaker
        speaker = new Speaker(getApplicationContext());
        setContentView(R.layout.activity_student_game);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        Intent intent = getIntent();
        String studentId = intent.getStringExtra("studentId");
        student = realm.where(Student.class).equalTo("id", studentId).findFirst();
        Log.i(TAG,"Student "+ student.getNickname()+", ID = "+studentId );
        Log.i(TAG,"With "+student.getChallenges().size()+" challenges.");
        nextButton = (Button) findViewById(R.id.nextButton);
        nextButton.setOnClickListener(this);
        clearButton = (ImageButton) findViewById(R.id.backspaceBtn);
        clearButton.setOnClickListener(this);
        answerText = (EditText) findViewById(R.id.awnser);
        correctSound = MediaPlayer.create(getApplicationContext(), R.raw.correct);
        wrongSound = MediaPlayer.create(getApplicationContext(), R.raw.wrongsound);
        AppConfiguration config = realm.where(AppConfiguration.class).findFirst();
        //set space button
        Button spaceButton = (Button)findViewById(R.id.spaceBtn);
        spaceButton.setOnTouchListener(this);
        spaceButton.setOnClickListener(this);
        //---
        this.help = config.getShowWord();
        this.wordList = student.getChallenges();
        if(wordList.size() > 0){
           startWord();
           Log.d(TAG, "creating activity stats...");
           activityStats = new PlayStats();
           activityStats.setStart(new Date());
           activityStats.setTotalPoints(0);
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
                TextView helpText = (TextView) findViewById(R.id.tip);
                helpText.setText(word.toUpperCase());
            }
            String scrambledWord = scramble(word);
            scrambledLayout = (LinearLayout) findViewById(R.id.keyboardLayout);
            setImageView(challenge.getImage(), word);

            Set<Character> letters = new HashSet<>();
            for (int i = 0; i < scrambledWord.length(); i++) {
                letters.add(scrambledWord.charAt(i));
            }

            if(letters.size() <= 10){
                LinearLayout row = new LinearLayout(this);
                row.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                row.setOrientation(LinearLayout.HORIZONTAL);
                Iterator<Character> characters = letters.iterator();
                while (characters.hasNext()) {
                    Character c = characters.next();
                    String l = Character.toString(c).toUpperCase();
                    if (!l.trim().isEmpty()) {
                        Button button = new Button(this);
                        button.setId(c);
                        button.setLayoutParams(new LinearLayout.LayoutParams(125, ViewGroup.LayoutParams.WRAP_CONTENT));
                        button.setText(Character.toString(c).toUpperCase());
                        button.setTextSize(12);
                        button.setMinHeight(5);
                        button.setOnTouchListener(this);
                        button.setOnClickListener(this);
                        row.addView(button);
                    }
                }
                scrambledLayout.addView(row);
            }else{
                LinearLayout row = new LinearLayout(this);
                row.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                row.setOrientation(LinearLayout.HORIZONTAL);
                Iterator<Character> characters = letters.iterator();
                int counter = 0;
                while (characters.hasNext()) {
                    Character c = characters.next();
                    counter++;
                    String l = Character.toString(c).toUpperCase();
                    if (!l.trim().isEmpty()) {
                        Button button = new Button(this);
                        button.setId(c);
                        button.setLayoutParams(new LinearLayout.LayoutParams(125, ViewGroup.LayoutParams.WRAP_CONTENT));
                        button.setText(Character.toString(c).toUpperCase());
                        button.setTextSize(12);
                        button.setMinHeight(5);
                        button.setOnTouchListener(this);
                        button.setOnClickListener(this);
                        row.addView(button);
                    }
                    if(counter % 10 == 0){
                        scrambledLayout.addView(row);
                        row = new LinearLayout(this);
                        row.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        row.setOrientation(LinearLayout.HORIZONTAL);
                    }
                }
                scrambledLayout.addView(row); // last line
            }
        }
    }


    /**
     * Method called to speak the current word of the activity.
     * @param view
     */
    public void speak(View view){
        speaker.speak(word);
    }


    /**
     * Method to process the actions of the buttons.
     * @param v
     */
    public void onClick(View v){
        if(clearButton.getId() == v.getId()){
            if(answerText.getText().toString().length() == 0){
                clearButton.setEnabled(Boolean.FALSE);
            }else {
                answerText.setText(answerText.getText().toString().substring(0, answerText.getText().toString().length() - 1));
                answerString = answerString.substring(0, answerString.length() - 1);
            }
        }else{
            TextView clicked = (TextView) findViewById(v.getId());
            if(nextButton.getId() == v.getId()) {
                clearButton.setEnabled(Boolean.FALSE);
                scrambledLayout.removeAllViews();
                answerText.setText("");
                answerString = "";
                answerText.setTextColor(Color.rgb(0, 0, 0));
                startWord();
            }
            else {
                clearButton.setEnabled(Boolean.TRUE);
                if(clicked.getText().toString().trim().isEmpty()){
                    answerText.setText(answerText.getText().toString() + " ");
                    answerString += " ";
                }else{
                    answerText.setText(answerText.getText().toString() + clicked.getText());
                    answerString += clicked.getText();
                }

                try {
                    if(answerString.length() == word.length()){
                        if(answerString.equalsIgnoreCase(word)){
                            clearButton.setEnabled(Boolean.FALSE);
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
                                    startWord();
                                }
                            }, 1200);
                        }
                        else {
                            clearButton.setEnabled(Boolean.FALSE);
                            answerText.setTextColor(Color.rgb(255, 0, 0));
                            wrongSound.start();
                            vibrator.vibrate(1500);

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    answerString = "";
                                    answerText.setText("");
                                    answerText.setTextColor(Color.rgb(0, 0, 0));
                                    //TODO add
                                }
                            }, 1200);
                        }
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == CHECK_CODE){
            if(resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS){
                if(speaker == null){
                    speaker = new Speaker(this);
                }else{
                    speaker.destroy();
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