package br.com.ema.activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.litepal.crud.DataSupport;

import java.util.List;

import br.com.ema.R;
import br.com.ema.activities.admin.MenuActivity;
import br.com.ema.activities.student.StudentMenuActivity;
import br.com.ema.entities.Admin;
import br.com.ema.entities.Student;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void login(View view){
        EditText usernameET = (EditText)findViewById(R.id.username);
        String username = usernameET.getText().toString();
        EditText passwordET = (EditText)findViewById(R.id.passwordField);
        String password = passwordET.getText().toString();

        List<Admin> admins = DataSupport.where("name = ?",username).where("password = ?", password).find(Admin.class);
        if(admins.size() > 0){
            Intent intent = new Intent(this,MenuActivity.class);
            startActivity(intent);
        }else{
           List<Student> students = DataSupport.where("nickname = ?",username).where("password = ?", password).find(Student.class, true);
            if(students.size() > 0 &&  students.get(0) != null){
                //TODO send student to start content
                Intent intent = new Intent(this,StudentMenuActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("student", students.get(0));
                intent.putExtras(bundle);
                startActivity(intent);
            }else{
                TextView info = (TextView) findViewById(R.id.information);
                info.setTextColor(Color.RED);
                info.setText(R.string.checkDataTyped);
                info.setBackgroundColor(Color.TRANSPARENT);
            }
        }
    }
}
