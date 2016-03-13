package br.com.ema.activities.student;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;


import br.com.ema.R;
import br.com.ema.activities.LoginActivity;
import br.com.ema.activities.student.StudentGameActivity;
import br.com.ema.entities.Student;

public class StudentMenuActivity extends AppCompatActivity {

    private Student student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_menu);
        Intent intent = getIntent();
        Bundle bundle=intent.getExtras();
        student = (Student) bundle.getSerializable("student");
    }

    public void toPlay(View view){
        Intent intent = new Intent(this, StudentGameActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("student", student);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void logout(View view){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
