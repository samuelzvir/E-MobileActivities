/*
 * Copyright 2015 Samuel Yuri Zvir
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ema.activities.admin;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;

import java.util.Locale;

import org.ema.entities.AppConfiguration;

import io.realm.Realm;

public class SettingsActivity extends AppCompatActivity {

    private AppConfiguration configuration;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = Realm.getDefaultInstance();
        setContentView(org.ema.R.layout.activity_settings);
        configuration  = getConfiguration();
        setRadioButtons();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(org.ema.R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == org.ema.R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onLanguageClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case org.ema.R.id.portugueseRadioButton:
                if (checked){
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            configuration.setLanguage("pt-br");
                            realm.insertOrUpdate(configuration);
                        }
                    });
                    changeLocale("pt-br");
                }
                    break;
            case org.ema.R.id.englishRadioButton:
                if (checked){
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            configuration.setLanguage("en-us");
                            realm.insertOrUpdate(configuration);
                        }
                    });
                    changeLocale("en-us");
                }
                    break;
        }
    }

    public void onLevelClicked(View view) {
        // Is the button now checked?
//        boolean checked = ((RadioButton) view).isChecked();//TODO
    }

    public void onSoundClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case org.ema.R.id.soundOn:
                if (checked){
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            configuration.setSound(true);
                            realm.insertOrUpdate(configuration);
                        }
                    });
                }
                break;
            case org.ema.R.id.soundOff:
                if (checked){
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            configuration.setSound(false);
                            realm.insertOrUpdate(configuration);
                        }
                    });
                }
                break;
        }
    }

    private void setRadioButtons() {
        String language = configuration.getLanguage();
        if (language.equalsIgnoreCase("pt-br")) {
            ((RadioButton) findViewById(org.ema.R.id.portugueseRadioButton)).setChecked(true);
            ((RadioButton) findViewById(org.ema.R.id.englishRadioButton)).setChecked(false);
        } else if (language.equalsIgnoreCase("en-us")) {
            ((RadioButton) findViewById(org.ema.R.id.englishRadioButton)).setChecked(true);
            ((RadioButton) findViewById(org.ema.R.id.portugueseRadioButton)).setChecked(false);
        }
        Boolean sound = configuration.getSound();
        if(sound){
            ((RadioButton) findViewById(org.ema.R.id.soundOn)).setChecked(true);
            ((RadioButton) findViewById(org.ema.R.id.soundOff)).setChecked(false);
        } else  {
            ((RadioButton) findViewById(org.ema.R.id.soundOn)).setChecked(false);
            ((RadioButton) findViewById(org.ema.R.id.soundOff)).setChecked(true);
        }
    }


    private AppConfiguration getConfiguration(){
        AppConfiguration config = realm.where(AppConfiguration.class).findFirst();
        if( config == null ){
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    AppConfiguration config = realm.createObject(AppConfiguration.class, 1);
                    config.setLanguage("pt-br");
                    changeLocale("pt-br");
                }
            });
        }
        config = realm.where(AppConfiguration.class).findFirst();
        return config;
    }

    private void changeLocale(String language_code){
        Resources res = getApplicationContext().getResources();
        // Change locale settings in the app.
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        conf.locale = new Locale(language_code.toLowerCase());
        res.updateConfiguration(conf, dm);
    }

    public void save(View view){
        Intent intent = new Intent(this,MenuActivity.class);
        startActivity(intent);
    }

    public void goBack(View view){
        Intent intent = new Intent(this, MenuActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

}
