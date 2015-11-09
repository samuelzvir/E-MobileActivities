package br.com.samuelzvir.meuabc.activities;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.language.Select;

import java.util.List;

import br.com.samuelzvir.meuabc.R;
import br.com.samuelzvir.meuabc.entities.Admin;
import br.com.samuelzvir.meuabc.entities.Admin$Table;
import br.com.samuelzvir.meuabc.entities.Student;
import br.com.samuelzvir.meuabc.entities.Student$Table;
import br.com.samuelzvir.meuabc.util.SecurityUtils;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "SecurityUtils";

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

        List<Admin> admins =  new Select().from(Admin.class).where(Condition.column(Admin$Table.NAME).eq(username),
                Condition.column(Admin$Table.PASSWORD).eq(password)).queryList();
        if(admins.size() > 0){
            Intent intent = new Intent(this,MenuActivity.class);
            startActivity(intent);
        }else{
            Student student = new Select().from(Student.class).where(Condition.column(Student$Table.NICKNAME).eq(username),
                    Condition.column(Student$Table.PASSWORD).eq(password)).querySingle();
            if(student != null){
                //TODO send student to start content
                Intent intent = new Intent(this,StudentMenuActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("student", student);
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
