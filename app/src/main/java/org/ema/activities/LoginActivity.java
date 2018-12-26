package org.ema.activities;

import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import org.ema.activities.admin.MenuActivity;
import org.ema.activities.admin.NewAdminPassword;
import org.ema.activities.student.StudentMenuActivity;
import org.ema.dialogs.ResetPasswordDialog;
import org.ema.entities.Admin;
import org.ema.entities.Student;
import org.ema.util.HashCodes;

import io.realm.Realm;
import io.realm.RealmQuery;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private Realm realm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(org.ema.R.layout.activity_login);
        this.realm = Realm.getDefaultInstance();
    }

    public void login(View view){
        EditText usernameET = (EditText)findViewById(org.ema.R.id.newName);
        String username = usernameET.getText().toString();
        EditText passwordET = (EditText)findViewById(org.ema.R.id.passwordTxt);
        String password = HashCodes.get_SHA_512_SecurePassword(passwordET.getText().toString(),"eMobileActivities");

        RealmQuery<Admin> query = realm.where(Admin.class);
        query.equalTo("name",username);
        query.equalTo("password",password);
        List<Admin> admins = query.findAll();
        if(admins.size() > 0){
            //reset password?
            Intent intent;
            if(admins.get(0).getChangePassword()){
                intent = new Intent(this,NewAdminPassword.class);
            }else{
                intent = new Intent(this,MenuActivity.class);
            }
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
                TextView info = (TextView) findViewById(org.ema.R.id.information);
                info.setTextColor(Color.RED);
                info.setText(org.ema.R.string.checkDataTyped);
                info.setBackgroundColor(Color.TRANSPARENT);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    public void resetPassword(View view){
        FragmentManager fm = getFragmentManager();
        ResetPasswordDialog dialog = new ResetPasswordDialog();
        dialog.show(fm,"");
    }
}
