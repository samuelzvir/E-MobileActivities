package br.com.ema.util;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;

import io.realm.Realm;

/**
 * Created by samuel on 05/06/17.
 */

public class ContentUtils {


    public static byte[] convertToByteArray(Bitmap bitmap){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] content = stream.toByteArray();
        return content;
    }

}
