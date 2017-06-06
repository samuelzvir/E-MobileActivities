package br.com.ema.activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import br.com.ema.R;
import br.com.ema.activities.admin.MenuActivity;
import br.com.ema.activities.student.StudentMenuActivity;
import br.com.ema.entities.Admin;
import br.com.ema.entities.Student;
import io.realm.Realm;
import io.realm.RealmQuery;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private Realm realm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.realm = Realm.getDefaultInstance();
    }

    public void login(View view){
        EditText usernameET = (EditText)findViewById(R.id.username);
        String username = usernameET.getText().toString();
        EditText passwordET = (EditText)findViewById(R.id.passwordField);
        String password = passwordET.getText().toString();

        RealmQuery<Admin> query = realm.where(Admin.class);
        query.equalTo("name",username);
        query.equalTo("password",password);
        List<Admin> admins = query.findAll();
        if(admins.size() > 0){
            Intent intent = new Intent(this,MenuActivity.class);
            startActivity(intent);
        }else{
            RealmQuery<Student> queryStudent = realm.where(Student.class);
            queryStudent.equalTo("nickname",username);
            queryStudent.equalTo("password",password);
            List<Student> students  = queryStudent.findAll();
            if(students.size() > 0 &&  students.get(0) != null){
                //TODO send student to start content
                Intent intent = new Intent(this,StudentMenuActivity.class);
                intent.putExtra("studentId", students.get(0).getId());
                startActivity(intent);
            }else{
                TextView info = (TextView) findViewById(R.id.information);
                info.setTextColor(Color.RED);
                info.setText(R.string.checkDataTyped);
                info.setBackgroundColor(Color.TRANSPARENT);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}
