package org.ema.activities.admin;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.ema.entities.Admin;

import io.realm.Realm;

public class AdminFormActivity extends AppCompatActivity {
    private static final String TAG = "AdminFormActivity";
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(org.ema.R.layout.activity_admin_form);
        realm = Realm.getDefaultInstance();
    }

    public void save(View view){
        final Admin admin = new Admin();
        EditText usernameET = (EditText)findViewById(org.ema.R.id.username);
        final String username = usernameET.getText().toString();
        EditText passwordET = (EditText)findViewById(org.ema.R.id.passwordField);
        final String password = passwordET.getText().toString();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                admin.setName(username);
                admin.setPassword(password);
                realm.insertOrUpdate(admin);
            }
        });
        if(realm.where(Admin.class).findFirst() != null){
            Intent intent =  new Intent(this,MenuActivity.class);
            startActivity(intent);
        }else{
            TextView info = (TextView) findViewById(org.ema.R.id.info);
            info.setTextColor(Color.RED);
            info.setText(org.ema.R.string.checkDataTyped);
            info.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}
