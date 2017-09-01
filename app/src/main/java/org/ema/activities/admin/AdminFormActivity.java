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
import org.ema.dialogs.InvalidEmail;
import org.ema.dialogs.WeakPassword;
import org.ema.entities.Admin;
import org.ema.util.HashCodes;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.realm.Realm;

public class AdminFormActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "AdminFormActivity";
    private Realm realm;
    private int counter = 0;
    private ShowcaseView showcaseView;
    private static final String EMAIL_PATTERN = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";

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
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        final Admin admin = new Admin();
        EditText usernameET = (EditText)findViewById(org.ema.R.id.newPass);
        final String username = usernameET.getText().toString();
        EditText emailET = (EditText)findViewById(R.id.emailField);
        final String email = emailET.getText().toString();
        Matcher matcher = pattern.matcher(email);
        if(!matcher.find()){
            emailET.getText().clear();
            android.app.FragmentManager fm = getFragmentManager();
            InvalidEmail dialog = new InvalidEmail();
            dialog.show(fm,"");
        }else{
            EditText passwordET = (EditText)findViewById(org.ema.R.id.confirmaPass);
            if(passwordET.getText().toString().length() < 6){
                passwordET.getText().clear();
                android.app.FragmentManager fm = getFragmentManager();
                WeakPassword dialog = new WeakPassword();
                dialog.show(fm,"");
            }else{
                final String password = HashCodes.get_SHA_512_SecurePassword(passwordET.getText().toString(),"eMobileActivities");
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        admin.setName(username);
                        admin.setEmail(email);
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

    @Override
    public void onClick(View v) {
        switch (counter) {
            case 0:
                showcaseView.setBackgroundColor(Color.TRANSPARENT);
                showcaseView.setShowcase(new ViewTarget(findViewById(R.id.newPass)), true);
                showcaseView.setContentTitle("");
                showcaseView.setContentText(getString(R.string.tutorial_admin_form3));
                break;
            case 1:
                showcaseView.setShowcase(new ViewTarget(findViewById(R.id.emailField)), true);
                showcaseView.setContentText(getString(R.string.tutorial_admin_form5));
                showcaseView.setContentTitle("");
                break;
            case 2:
                showcaseView.setShowcase(new ViewTarget(findViewById(R.id.confirmaPass)), true);
                showcaseView.setContentText(getString(R.string.tutorial_admin_form4));
                showcaseView.setContentTitle("");
                break;
            case 3:
                showcaseView.setShowcase(new ViewTarget(findViewById(R.id.save)), true);
                showcaseView.setContentText(getString(R.string.tutorial_admin_form6));
                showcaseView.setContentTitle("");
                break;
            case 4:
                showcaseView.hide();
                break;
        }
        counter++;
    }

}
