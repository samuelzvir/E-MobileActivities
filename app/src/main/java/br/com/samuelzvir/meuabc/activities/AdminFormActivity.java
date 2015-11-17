package br.com.samuelzvir.meuabc.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.raizlabs.android.dbflow.sql.language.Select;

import br.com.samuelzvir.meuabc.R;
import br.com.samuelzvir.meuabc.entities.Admin;
import br.com.samuelzvir.meuabc.util.SecurityUtils;

public class AdminFormActivity extends AppCompatActivity {
    private static final String TAG = "SecurityUtils";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_form);
    }

    public void save(View view){
        Admin  admin = new Admin();
        EditText usernameET = (EditText)findViewById(R.id.username);
        String username = usernameET.getText().toString();
        admin.setName(username);
        EditText passwordET = (EditText)findViewById(R.id.passwordField);
        String password = passwordET.getText().toString();

        admin.setPassword(password);
        admin.insert();
        if(new Select().from(Admin.class).count() > 0){
            Intent intent =  new Intent(this,MenuActivity.class);
            startActivity(intent);
        }else{
            TextView info = (TextView) findViewById(R.id.info);
            info.setTextColor(Color.RED);
            info.setText(R.string.checkDataTyped);
            info.setBackgroundColor(Color.TRANSPARENT);
        }
    }

}
