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

package br.com.ema.activities.admin;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.Locale;

import br.com.ema.R;
import br.com.ema.activities.LoginActivity;
import br.com.ema.activities.operations.DataAnalysisActivity;
import br.com.ema.activities.operations.TakePhotoActivity;
import br.com.ema.entities.AppConfiguration;
import io.realm.Realm;

public class MenuActivity extends Activity {

    private static final String TAG = "MenuActivity";
    private AppConfiguration configuration;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = Realm.getDefaultInstance();
        setContentView(R.layout.activity_menu);
        configuration = getConfiguration();
        setSavedLocale(configuration.getLanguage());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onToWords(View view){
        Intent intent = new Intent(this, WordsManagementActivity.class);
        startActivity(intent);
    }

    public void onToCamera(View view){
        Intent intent = new Intent(this, TakePhotoActivity.class);
        startActivity(intent);
    }

    public void onToProfiles(View view){
        Intent intent = new Intent(this, ProfilesActivity.class);
        startActivity(intent);
    }

    public void onToDataAnalysis(View view){
        Intent intent = new Intent(this, DataAnalysisActivity.class);
        startActivity(intent);
    }

    public void onToSettings(View view){
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public void onToABC(View view){
        Intent intent = new Intent(this, ABCActivity.class);
        startActivity(intent);
    }

    private AppConfiguration getConfiguration(){
        AppConfiguration config = realm.where(AppConfiguration.class).findFirst();
        if( config == null ){
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    AppConfiguration config = realm.createObject(AppConfiguration.class, 1);
                    config.setLanguage("pt-br");
                    config.setShowWord(true);
                }
            });
        }
        config = realm.where(AppConfiguration.class).findFirst();
        return config;
    }

    private void setSavedLocale(String language_code){
        Log.d(TAG,"loading locale properties...");
        Resources res = getApplicationContext().getResources();
        // Change locale settings in the app.
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        conf.locale = new Locale(language_code.toLowerCase());
        res.updateConfiguration(conf, dm);
        Log.d(TAG, "loaded.");
    }

    public void logout(View view){
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}
