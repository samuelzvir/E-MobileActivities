package br.com.ema.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;


import org.litepal.crud.DataSupport;

import java.util.Locale;

import br.com.ema.R;
import br.com.ema.entities.Admin;
import br.com.ema.entities.AppConfiguration;

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
        startLogic();
    }

    private String checkStatus(){
        long admins = DataSupport.findAll(Admin.class).size();
        Log.i(TAG, "admins = " + admins );
        if(admins == 0){
            return "form";
        }
            return  "login";
    }

    private AppConfiguration getConfiguration(){
        AppConfiguration config = DataSupport.findFirst(AppConfiguration.class);
        if( config == null ){
            config = new AppConfiguration();
            config.setLanguage("pt-br");
            config.setLevel("easy");
            config.save();
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
