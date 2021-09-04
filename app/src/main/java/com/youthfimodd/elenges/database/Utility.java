package com.youthfimodd.elenges.database;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Utility {
    // convert from bitmap to byte array
    /*public static byte[] getBytes(byte[] bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }*/
    // convert from byte array to bitmap
    public static Bitmap getPhoto(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 100, image.length);
    }
}
