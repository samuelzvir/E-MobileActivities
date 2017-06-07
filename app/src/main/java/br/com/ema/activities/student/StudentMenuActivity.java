package br.com.ema.activities.student;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;


import br.com.ema.R;
import br.com.ema.activities.LoginActivity;
import br.com.ema.activities.student.StudentGameActivity;
import br.com.ema.entities.Student;
import io.realm.Realm;

public class StudentMenuActivity extends AppCompatActivity {

    private Student student;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = Realm.getDefaultInstance();
        setContentView(R.layout.activity_student_menu);
        Intent intent = getIntent();
        String id = intent.getStringExtra("studentId");
        student = realm.where(Student.class).equalTo("id",id).findFirst();
    }

    public void toPlay(View view){
        Intent intent = new Intent(this, StudentGameActivity.class);
        intent.putExtra("studentId", student.getId());
        startActivity(intent);
    }

    public void logout(View view){
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}
