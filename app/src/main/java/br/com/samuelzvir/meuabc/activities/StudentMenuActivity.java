package br.com.samuelzvir.meuabc.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.language.Select;

import br.com.samuelzvir.meuabc.R;
import br.com.samuelzvir.meuabc.entities.Student;
import br.com.samuelzvir.meuabc.entities.Student$Table;

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
