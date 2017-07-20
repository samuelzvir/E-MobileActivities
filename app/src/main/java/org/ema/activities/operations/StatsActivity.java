package org.ema.activities.operations;



import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import org.ema.dialogs.ChooseUser;
import org.ema.entities.PlayStats;
import org.ema.entities.Student;

import io.realm.Realm;

public class StatsActivity extends AppCompatActivity {

    private static final String TAG = "StatsActivity";
    private Student student;
    private int points = 0 ;
    private int playedActivities = 0 ;
    private int highestPointing = 0 ;
    private int lowestPointing = 0 ;
    private TextView pointsTextView;
    private TextView playedActivitiesTextView;
    private TextView highestPointingTextView;
    private TextView lowestPointingTextView;
    private TextView name;
    private Realm realm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = Realm.getDefaultInstance();
        setContentView(org.ema.R.layout.activity_stats);
        Intent intent = getIntent();
        String studentId = intent.getStringExtra("studentId");
        this.student = realm.where(Student.class).equalTo("id", studentId).findFirst();
        loadBasicStats(student);
    }

    private void loadBasicStats(Student student){
        if(student == null){
            ChooseUser chooseUser = new ChooseUser();
            FragmentManager fm = getFragmentManager();
            chooseUser.show(fm,"Choose an user");
        }else{
            String id = student.getId();
            List<PlayStats> statsActivities = realm.where(PlayStats.class).equalTo("userId",id).findAll();
            long count = realm.where(PlayStats.class).count();
            Log.i(TAG,"Count: "+count);
            for (PlayStats stats : statsActivities){
                points += stats.getTotalPoints();
                playedActivities++;
                if(stats.getTotalPoints() > highestPointing){
                    highestPointing = (int) stats.getTotalPoints();
                }
                if(lowestPointing == 0){
                    lowestPointing = (int) stats.getTotalPoints();
                }else if(stats.getTotalPoints() < lowestPointing){
                    lowestPointing = (int) stats.getTotalPoints();
                }

            } // for end.

            name = (TextView) findViewById(org.ema.R.id.nameForStats);
            name.setText(student.getNickname());
            pointsTextView = (TextView) findViewById(org.ema.R.id.pointsTextValue);
            pointsTextView.setText(String.valueOf(points));
            playedActivitiesTextView = (TextView) findViewById(org.ema.R.id.playedActivitiesTextValue);
            playedActivitiesTextView.setText(String.valueOf(playedActivities));
            highestPointingTextView = (TextView) findViewById(org.ema.R.id.highestPointingTextValue);
            highestPointingTextView.setText(String.valueOf(highestPointing));
            lowestPointingTextView = (TextView) findViewById(org.ema.R.id.lowestPointingTextValue);
            lowestPointingTextView.setText(String.valueOf(lowestPointing));
        }
    }

    public void goBackToDataAnalysisActivity(View view){
        Intent intent = new Intent(this, DataAnalysisActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}