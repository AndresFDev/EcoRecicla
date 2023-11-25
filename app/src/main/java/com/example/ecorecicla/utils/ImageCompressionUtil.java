package com.example.ecorecicla.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ImageCompressionUtil {

    public static String compressAndConvertToWebP(Context context, Uri imageUri) {
        try {
            Bitmap originalBitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), imageUri);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            originalBitmap.compress(Bitmap.CompressFormat.WEBP, 50, outputStream);
            byte[] compressedImageBytes = outputStream.toByteArray();

            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
            String formattedDateTime = dateFormat.format(calendar.getTime());

            String outputFilePath = context.getFilesDir() + File.separator + "compressed_image_" + formattedDateTime + ".webp";

            try (FileOutputStream fileOutputStream = new FileOutputStream(outputFilePath)) {
                fileOutputStream.write(compressedImageBytes);
            }

            return outputFilePath;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
}
