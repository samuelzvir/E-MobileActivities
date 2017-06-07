/*
 * Copyright 2017 Samuel Yuri Zvir
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
import android.app.FragmentManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.List;

import br.com.ema.activities.operations.TakePhotoActivity;
import br.com.ema.dialogs.WordSavedDialog;
import br.com.ema.entities.SimpleChallenge;
import br.com.ema.R;
import br.com.ema.util.ContentUtils;
import io.realm.Realm;

public class CreateTextActivity extends Activity {

    private static final String TAG = "CreateTextActivity";
    private ImageView imageView;
    private String text;
    private Realm realm;

    /**
     * onCreate method to set all necessary information to this activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = Realm.getDefaultInstance();
        setContentView(R.layout.activity_create_challenge);
        boolean edition = false;
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        Bitmap photo = null;
        if(extras != null){
            photo = (Bitmap) extras.get("photo");
        }

        if(!hasCamera()) {
            findViewById(R.id.takePicture).setEnabled(false);
        }
        if(photo != null){
            imageView = (ImageView) findViewById(R.id.image);
            imageView.setImageBitmap(photo);
            edition = true;
        }
        this.text = intent.getStringExtra("text");
        EditText t = (EditText) findViewById(R.id.word);
        t.setText(this.text);
        if(this.text != null){
            List<SimpleChallenge> sc = realm.where(SimpleChallenge.class).equalTo("word",text).findAll();
            if(sc.size() > 0){
                    Log.d(TAG, "setting image to the view based on the saved image.");
                    SimpleChallenge temp = sc.get(0);
                    if(temp.getImage() != null && !edition){
                        setImageView(temp.getImage());
                    }
                }
            }
    }



    /**
     * Redirects to the TakePhotoActivity activity.
     */
    public void onToCamera(View view){
        Intent intent = new Intent(this, TakePhotoActivity.class);
        intent.putExtra("text", this.text);
        startActivity(intent);
        finish();
    }

    /**
     * Method to create or update a word.
     */
    public void saveWord(View view){
        EditText messageView = (EditText) findViewById(R.id.word);
        final String word = messageView.getText().toString();
        //TODO validate save button.
        if(this.text != null){
            List<SimpleChallenge> sc = realm.where(SimpleChallenge.class).equalTo("word",text).findAll();
            if(sc != null && sc.size() > 0){
                final SimpleChallenge c = sc.get(0);
                Log.d(TAG, "updating word " + c.getWord() +" to "+word);
                if(findViewById(R.id.image) != null){
                    final byte[] content = ContentUtils.convertToByteArray(((BitmapDrawable)((ImageView)findViewById(R.id.image)).getDrawable()).getBitmap());
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            c.setImage(content);
                            c.setWord(word);
                            realm.insertOrUpdate(c);
                        }
                    });
                }
                WordSavedDialog dialog = new WordSavedDialog();
                dialog.setCreateTextActivity(this);
                FragmentManager fm = getFragmentManager();
                dialog.show(fm, "finish.");
            }
        }else{
            Log.d(TAG, "adding new word " + word);
            final SimpleChallenge simpleChallenge = new SimpleChallenge();
            simpleChallenge.setWord(word);
            if(findViewById(R.id.image) != null){
                byte[] content = ContentUtils.convertToByteArray(((BitmapDrawable)((ImageView)findViewById(R.id.image)).getDrawable()).getBitmap());
                simpleChallenge.setImage(content);
            }
            realm.executeTransaction(
                    new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            realm.insertOrUpdate(simpleChallenge);
                        }
                    }
            );
            WordSavedDialog dialog = new WordSavedDialog();
            dialog.setCreateTextActivity(this);
            FragmentManager fm = getFragmentManager();
            dialog.show(fm, "finish.");
        }

    }

    /**
     * Redirects to the MenuActivity activity.
     */
    public void toMenu(View view){
        Intent intent = new Intent(this,MenuActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    /**
     * Redirects to the WordsManagementActivity activity.
     */
    public void toWordsList(View view){
        Intent intent = new Intent(this,WordsManagementActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    /**
     * Method to check if the device has any camera.
     */
    public boolean hasCamera(){
        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);
    }

    /**
     * Adds the image to the view.
     */
    private void setImageView(byte[] image) {
        Log.i(TAG, "Adding image ... ");
        Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
        ImageView imageView = (ImageView) findViewById(R.id.image);
        imageView.setImageBitmap(bitmap);
    }

    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

}