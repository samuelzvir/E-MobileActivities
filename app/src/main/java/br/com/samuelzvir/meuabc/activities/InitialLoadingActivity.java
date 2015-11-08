package br.com.samuelzvir.meuabc.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.Select;

import java.util.Locale;

import br.com.samuelzvir.meuabc.R;
import br.com.samuelzvir.meuabc.entities.Admin;
import br.com.samuelzvir.meuabc.entities.AppConfiguration;

public class InitialLoadingActivity extends Activity {

    private static final String TAG = "InitialLoadingActivity";
    private AppConfiguration configuration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial_loading);
        startLogic();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setContentView(R.layout.activity_initial_loading);
    }

    private String checkStatus(){
        long admins = new Select().from(Admin.class).queryList().size();
        Log.i(TAG, "admins = " + admins );
        if(admins == 0){
            return "form";
        }
            return  "login";
    }

    private AppConfiguration getConfiguration(){
        AppConfiguration config = new Select().from(AppConfiguration.class).querySingle();
        if( config == null ){
            config = new AppConfiguration();
            config.setLanguage("pt-br");
            config.setLevel("easy");
            config.insert();
        }
        return config;
    }

    private void setSavedLocale(String language_code){
        Log.d(TAG, "loading locale properties...");
        Resources res = getApplicationContext().getResources();
        // Change locale settings in the app.
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        conf.locale = new Locale(language_code.toLowerCase());
        res.updateConfiguration(conf, dm);
        Log.d(TAG, "loaded.");
    }

    private void startLogic(){
        FlowManager.init(this);
        configuration = getConfiguration();
        setSavedLocale(configuration.getLanguage());
        String action = checkStatus();
        if(action.equals("form")){
            Intent intent = new Intent(this, AdminFormActivity.class);
            startActivity(intent);
        }else{ // login
            Intent intent = new Intent(this,LoginActivity.class);
            startActivity(intent);
        }
    }

}
