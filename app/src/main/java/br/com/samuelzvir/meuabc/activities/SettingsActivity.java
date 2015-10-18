package br.com.samuelzvir.meuabc.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;

import com.raizlabs.android.dbflow.sql.language.Select;

import java.util.Locale;

import br.com.samuelzvir.meuabc.R;
import br.com.samuelzvir.meuabc.entities.AppConfiguration;

public class SettingsActivity extends Activity {

    private AppConfiguration configuration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        configuration  = getConfiguration();
        setRadioButtons();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
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

    public void onLanguageClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.portugueseRadioButton:
                if (checked){
                    configuration.setLanguage("pt-br");
                    configuration.update();
                    changeLocale("pt-br");
                }
                    break;
            case R.id.englishRadioButton:
                if (checked){
                    configuration.setLanguage("en-us");
                    configuration.update();
                    changeLocale("en-us");
                }
                    break;
        }
    }

    public void onLevelClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.levelEasyRadioButton:
                if (checked){
                    configuration.setLevel("easy");
                    configuration.update();
                }
                break;
            case R.id.levelIntermediateRadioButton:
                if (checked){
                    configuration.setLevel("intermediate");
                    configuration.update();
                }
                break;
            case R.id.levelHardRadioButton:
                if (checked){
                    configuration.setLevel("hard");
                    configuration.update();
                }
                break;
        }
    }

    private void setRadioButtons() {
        String language = configuration.getLanguage();
        if (language.equalsIgnoreCase("pt-br")) {
            ((RadioButton) findViewById(R.id.portugueseRadioButton)).setChecked(true);
            ((RadioButton) findViewById(R.id.englishRadioButton)).setChecked(false);
        } else if (language.equalsIgnoreCase("en-us")) {
            ((RadioButton) findViewById(R.id.englishRadioButton)).setChecked(true);
            ((RadioButton) findViewById(R.id.portugueseRadioButton)).setChecked(false);
        }
        String level = configuration.getLevel();
        if (level.equalsIgnoreCase("easy")) {
            ((RadioButton) findViewById(R.id.levelEasyRadioButton)).setChecked(true);
            ((RadioButton) findViewById(R.id.levelIntermediateRadioButton)).setChecked(false);
            ((RadioButton) findViewById(R.id.levelHardRadioButton)).setChecked(false);
        } else if (level.equalsIgnoreCase("intermediate")) {
            ((RadioButton) findViewById(R.id.levelEasyRadioButton)).setChecked(false);
            ((RadioButton) findViewById(R.id.levelIntermediateRadioButton)).setChecked(true);
            ((RadioButton) findViewById(R.id.levelHardRadioButton)).setChecked(false);
        } else if (level.equalsIgnoreCase("hard")) {
            ((RadioButton) findViewById(R.id.levelEasyRadioButton)).setChecked(false);
            ((RadioButton) findViewById(R.id.levelIntermediateRadioButton)).setChecked(false);
            ((RadioButton) findViewById(R.id.levelHardRadioButton)).setChecked(true);
        }
    }


    private AppConfiguration getConfiguration(){
        AppConfiguration config = new Select().from(AppConfiguration.class).querySingle();
        if( config == null ){
            config = new AppConfiguration();
            config.setLanguage("pt-br");
            changeLocale("pt-br");
            config.setLevel("easy");
            config.insert();

        }
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

}
