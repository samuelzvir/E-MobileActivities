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

package br.com.ema.activities.operations;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import br.com.ema.activities.admin.CreateTextActivity;
import br.com.ema.R;

public class TakePhotoActivity extends AppCompatActivity {

    private static final String TAG = "TakePhotoActivity";
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private ImageView mImageView;
    private String text;

    /**
     * onCreate method to set all necessary information to this activity.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_photo);
        Intent intent = getIntent();
        this.text = intent.getStringExtra("text");
        mImageView = (ImageView) findViewById(R.id.photo);
    }

    /**
     * Receives the camera result
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            Log.d(TAG,"setting the image...");
            Bundle  extras = data.getExtras();
            Bitmap photo = (Bitmap) extras.get("data");
            mImageView.setImageBitmap(photo);
            Log.d(TAG,"the image has been set.");
        }
        Intent intent = getIntent();
        this.text = intent.getStringExtra("text");
    }

    /**
     * launches the android camera
     */
    public void launchCamera(View view){
        Log.d(TAG,"launching the camera app...");
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,REQUEST_IMAGE_CAPTURE);
    }

    /**
     * Goes to the screen that creates the challenge creating and saving words and pictures.
     */
    public void toChallenge(View view){
        Intent intent = new Intent(this,CreateTextActivity.class);
        if(mImageView != null) {
            intent.putExtra("photo", ((BitmapDrawable)mImageView.getDrawable()).getBitmap());
        }
        intent.putExtra("text", this.text);
        startActivity(intent);
        finish();
    }

    protected void onDestroy() {
        super.onDestroy();
    }
}