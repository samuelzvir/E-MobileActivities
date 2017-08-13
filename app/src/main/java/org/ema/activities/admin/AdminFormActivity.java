package org.ema.activities.admin;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

import org.ema.R;
import org.ema.entities.Admin;

import io.realm.Realm;

public class AdminFormActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "AdminFormActivity";
    private Realm realm;
    private int counter = 0;
    private ShowcaseView showcaseView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(org.ema.R.layout.activity_admin_form);
        realm = Realm.getDefaultInstance();
        showcaseView = new ShowcaseView.Builder(this)
                .withNewStyleShowcase()
                .setTarget(new ViewTarget(R.id.imageView, this))
                .setContentTitle(R.string.tutorial_admin_form1)
                .setContentText(R.string.tutorial_admin_form2)
                .setOnClickListener(this)
                .build();
        showcaseView.setDetailTextAlignment(Layout.Alignment.ALIGN_CENTER);
        showcaseView.setBackgroundColor(ContextCompat.getColor(this, R.color.blue));
        showcaseView.setTitleTextAlignment(Layout.Alignment.ALIGN_CENTER);
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

    @Override
    public void onClick(View v) {
        switch (counter) {
            case 0:
                showcaseView.setBackgroundColor(Color.TRANSPARENT);
                showcaseView.setShowcase(new ViewTarget(findViewById(R.id.username)), true);
                showcaseView.setContentTitle("");
                showcaseView.setContentText(getString(R.string.tutorial_admin_form3));
                break;
            case 1:
                showcaseView.setShowcase(new ViewTarget(findViewById(R.id.passwordField)), true);
                showcaseView.setContentText(getString(R.string.tutorial_admin_form4));
                showcaseView.setContentTitle("");
                break;
            case 2:
                showcaseView.setShowcase(new ViewTarget(findViewById(R.id.save)), true);
                showcaseView.setContentText(getString(R.string.tutorial_admin_form5));
                showcaseView.setContentTitle("");
                break;
            case 3:
                showcaseView.hide();
                break;
        }
        counter++;
    }

}
