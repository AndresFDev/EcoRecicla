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

    // Método para comprimir y convertir una imagen a formato WebP
    public static String compressAndConvertToWebP(Context context, Uri imageUri) {
        try {
            // Obtener el bitmap original a partir de la URI de la imagen
            Bitmap originalBitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), imageUri);

            // Crear un flujo de salida para almacenar la imagen comprimida
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            // Comprimir el bitmap original a formato WebP con calidad del 50%
            originalBitmap.compress(Bitmap.CompressFormat.WEBP, 50, outputStream);

            // Obtener los bytes de la imagen comprimida
            byte[] compressedImageBytes = outputStream.toByteArray();

            // Obtener la fecha y hora actual para formatear el nombre del archivo
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
            String formattedDateTime = dateFormat.format(calendar.getTime());

            // Crear una ruta de archivo de salida en el directorio de archivos de la aplicación
            String outputFilePath = context.getFilesDir() + File.separator + "compressed_image_" + formattedDateTime + ".webp";

            // Escribir los bytes comprimidos en el archivo de salida
            try (FileOutputStream fileOutputStream = new FileOutputStream(outputFilePath)) {
                fileOutputStream.write(compressedImageBytes);
            }

            // Devolver la ruta del archivo comprimido
            return outputFilePath;
        } catch (IOException e) {
            // Manejar posibles excepciones de E/S
            e.printStackTrace();
            // Devolver una cadena vacía en caso de error
            return "";
        }
    }
}