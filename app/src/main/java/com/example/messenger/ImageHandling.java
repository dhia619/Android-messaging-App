package com.example.messenger;

import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ImageHandling {

    // Helper method to convert input stream to byte array
    static byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];
        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }
    static public byte[] getImageBytesFromBase64(String base64Image) {
        // Decode the Base64 string into a byte array
        byte[] imageData = Base64.decode(base64Image, Base64.DEFAULT);
        return imageData;
    }



}
