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
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import br.com.ema.activities.operations.TakePhotoActivity;
import br.com.ema.dialogs.WordSavedDialog;
import br.com.ema.entities.SimpleChallenge;
import br.com.ema.R;

public class CreateTextActivity extends Activity {

    private static final String TAG = "CreateTextActivity";
    private ImageView imageView;
    private String text;

    /**
     * onCreate method to set all necessary information to this activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_challenge);
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
        }
        this.text = intent.getStringExtra("text");
        EditText t = (EditText) findViewById(R.id.word);
        t.setText(this.text);
        if(this.text != null){
            List<SimpleChallenge> sc = DataSupport.where("word = ?", text).find(SimpleChallenge.class);
                if(sc.size() > 0){
                    Log.d(TAG, "setting image to the view");
                    SimpleChallenge temp = sc.get(0);
                    if(temp.getImagePath() != null){
                        setImageView(temp.getImagePath());
                    }
                }
            }
    }



    /**
     * Redirects to the TakePhotoActivity activity.
     */
    public void onToCamera(View view){
        Intent intent = new Intent(this, TakePhotoActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Method to create or update a word.
     */
    public void saveWord(View view){
        EditText messageView = (EditText) findViewById(R.id.word);
        String word = messageView.getText().toString();
        //TODO validate save button.
        if(this.text != null){
           List<SimpleChallenge> sc = DataSupport.where("word = ?", text).find(SimpleChallenge.class);
            if(sc != null && sc.size() > 0){
                SimpleChallenge c = sc.get(0);
                Log.d(TAG, "updating word " + c.getWord() +" to "+word);
                c.setWord(word);
                if(findViewById(R.id.image) != null){
                    String filename = createImageName();
                    String p = MediaStore.Images.Media.insertImage(getContentResolver(),
                            ((BitmapDrawable)((ImageView)findViewById(R.id.image)).getDrawable()).getBitmap(),
                            filename, null);
                    String filepath = getRealPathFromURI(Uri.parse(p));
                    c.setImagePath(filepath);
                }
                c.save();
                if(c.isSaved()) {
                    Log.d(TAG, "created.");
                }
                WordSavedDialog dialog = new WordSavedDialog();
                dialog.setCreateTextActivity(this);
                FragmentManager fm = getFragmentManager();
                dialog.show(fm, "finish.");
            }
        }else{
            Log.d(TAG, "adding new word " + word);
            SimpleChallenge simpleChallenge = new SimpleChallenge();
            simpleChallenge.setWord(word);
            if(findViewById(R.id.image) != null){
                String filename = createImageName();
                String p = MediaStore.Images.Media.insertImage(getContentResolver(),
                        ((BitmapDrawable)((ImageView)findViewById(R.id.image)).getDrawable()).getBitmap(),
                        filename, null);
                String filepath = getRealPathFromURI(Uri.parse(p));
                simpleChallenge.setImagePath(filepath);
            }
            simpleChallenge.save();
            if(simpleChallenge.isSaved()) {
                Log.d(TAG, "created.");
            }
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
     * Creates a file name based in the current time using the pattern yyyyMMdd_HHmmss.
     */
    private String createImageName(){
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return "IMG_" + timeStamp + ".jpg";
    }

    /**
     * Get the path to the given uri.
     */
    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    /**
     * Adds the image to the view.
     */
    private void setImageView(String path) {
        Log.i(TAG, "Adding image " + path);
        File imgFile = new File(path);
        if (imgFile.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            ImageView imageView = (ImageView) findViewById(R.id.image);
            imageView.setImageBitmap(bitmap);
        }
    }

}