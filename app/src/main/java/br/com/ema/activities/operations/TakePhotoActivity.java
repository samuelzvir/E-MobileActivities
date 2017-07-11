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
import android.graphics.Matrix;
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
    private int rotation = 0;
    private boolean newPicture = false;

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
            newPicture = true;
            float rot = mImageView.getRotation();
            if(rot != 90){
                mImageView.setRotation(90);
                rotation = 90;
            }
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
        if(mImageView != null && newPicture) {
            intent.putExtra("photo", ((BitmapDrawable)mImageView.getDrawable()).getBitmap());
            intent.putExtra("imageRotation", rotation);
        }
        intent.putExtra("text", this.text);
        startActivity(intent);
        finish();
    }

    /**
     * Method to rotate bitmaps
     * @param source
     * @param angle
     * @return
     */
    public static Bitmap rotateBitmap(Bitmap source, float angle)
    {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    public void rotate(View view){
        if(mImageView != null){
            if(rotation == 0){
                mImageView.setRotation(90);
                rotation = 90;
            }else if(rotation == 90){
                mImageView.setRotation(180);
                rotation = 180;
            }else if(rotation == 180 ){
                mImageView.setRotation(270);
                rotation = 270;
            }else {
                mImageView.setRotation(0);
                rotation = 0;
            }
        }
    }

    protected void onDestroy() {
        super.onDestroy();
    }
}