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

package br.com.ema.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

import br.com.ema.entities.SimpleChallenge;
import br.com.ema.R;

public class CreateTextActivity extends Activity {

    private static final String TAG = "CreateTextActivity";
    private ImageView image;
    private String imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_challenge);
        Intent intent = getIntent();
        String imagePath = intent.getStringExtra("path");
        this.imagePath = imagePath;
        if(imagePath != null){
            setImageView(imagePath);
        }
    }

    public void onToCamera(View view){
        Intent intent = new Intent(this, TakePhotoActivity.class);
        startActivity(intent);
    }

    public void saveWord(View view){
        EditText messageView = (EditText) findViewById(R.id.word);
        String word = messageView.getText().toString();
        Log.d(TAG, "adding word " + word);
        SimpleChallenge simpleChallenge = new SimpleChallenge();
        simpleChallenge.setWord(word);
        simpleChallenge.setImagePath(this.imagePath);
        simpleChallenge.save();
        if(simpleChallenge.isSaved()) {
            Log.d(TAG, "created.");
        }
        TextView info = (TextView) findViewById(R.id.status);
        info.setTextColor(Color.GREEN);
        info.setText(R.string.saved);
    }

    private void setImageView(String path) {
        Log.i(TAG, "Adding image " + path);
        File imgFile = new File(path);
        if (imgFile.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            ImageView imageView = (ImageView) findViewById(R.id.imageViewPicture);
            imageView.setImageBitmap(bitmap);
        }
    }

    public void toMenu(View view){
        Intent intent = new Intent(this,MenuActivity.class);
        startActivity(intent);
    }

}