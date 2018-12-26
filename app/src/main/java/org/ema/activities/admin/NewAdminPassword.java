package org.ema.activities.admin;

import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.ema.R;
import org.ema.dialogs.DifferentPasswords;
import org.ema.dialogs.WeakPassword;
import org.ema.entities.Admin;
import org.ema.util.HashCodes;

import io.realm.Realm;

public class NewAdminPassword extends AppCompatActivity {

    private Realm realm;
    private int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_admin_password);
        realm = Realm.getDefaultInstance();
    }

    public void save(View view){
        final Admin admin = realm.where(Admin.class).findFirst();
        EditText newPasswordET = (EditText)findViewById(R.id.newName);
        EditText passwordET = (EditText)findViewById(org.ema.R.id.passwordTxt);
        if(passwordET.getText().toString().length() < 6){
            passwordET.getText().clear();
            newPasswordET.getText().clear();
            android.app.FragmentManager fm = getFragmentManager();
            WeakPassword dialog = new WeakPassword();
            dialog.show(fm,"");
        }else{
            if(!newPasswordET.getText().equals(passwordET.getText())){
                passwordET.getText().clear();
                newPasswordET.getText().clear();
                FragmentManager fm = getFragmentManager();
                DifferentPasswords dialog = new DifferentPasswords();
                dialog.show(fm,"");
            }else {
                final String password = HashCodes.get_SHA_512_SecurePassword(passwordET.getText().toString(),"eMobileActivities");
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        admin.setPassword(password);
                        admin.setChangePassword(false);
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
        }

    }

    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

}
