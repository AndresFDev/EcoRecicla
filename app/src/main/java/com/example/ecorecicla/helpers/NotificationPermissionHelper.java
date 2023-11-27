package com.example.ecorecicla.helpers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.ColorUtils;

import com.example.ecorecicla.R;

public class NotificationPermissionHelper {

    private final Context context;
    private final ActivityResultLauncher<String> requestPermissionLauncher;
    private AlertDialog alertDialog;

    // Constructor que recibe el contexto y el lanzador de permisos
    public NotificationPermissionHelper(Context context, ActivityResultLauncher<String> requestPermissionLauncher) {
        this.context = context;
        this.requestPermissionLauncher = requestPermissionLauncher;
    }

    // Método para verificar si los permisos de notificación están habilitados
    public void checkNotificationPermission() {
        Log.d("PermissionDebug", "Checking if notifications are enabled...");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && !areNotificationsEnabled(context)) {
            Log.d("PermissionDebug", "Notifications are not enabled, showing dialog...");
            showNotificationDialog();
        } else {
            Log.d("PermissionDebug", "Notifications are enabled.");
        }
    }

    // Método para verificar si las notificaciones están habilitadas
    public boolean areNotificationsEnabled(Context context) {
        Log.d("PermissionDebug", "Checking if notifications are enabled...");
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        return notificationManagerCompat.areNotificationsEnabled();
    }

    // Método para solicitar permisos de notificación
    private void requestNotificationPermission() {
        Intent intent = new Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                .putExtra(Settings.EXTRA_APP_PACKAGE, context.getPackageName());
        context.startActivity(intent);
    }

    // Método para mostrar un diálogo de permisos de notificación
    private void showNotificationDialog() {
        Log.d("PermissionDebug", "Showing notification dialog...");
        View customView = LayoutInflater.from(context).inflate(R.layout.dialog_permission, null);

        ImageView imageView = customView.findViewById(R.id.ivIcon);
        TextView textViewMessage = customView.findViewById(R.id.textViewMessage);
        Button btnAccept = customView.findViewById(R.id.btnAccept);
        Button btnCancel = customView.findViewById(R.id.btnCancel);

        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.DialogTransparent);
        builder.setView(customView);

        // Obtener colores dinámicos para el diseño en Android 12 o superior
        int dynamicAccentColor = 0;
        int dynamicAccentColorDefault = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            dynamicAccentColor = getColorDynamicAccent();
            dynamicAccentColorDefault = getColorDynamicAccentDefault();
        }

        // Establecer colores dinámicos en las vistas
        imageView.setImageTintList(ColorStateList.valueOf(dynamicAccentColor));
        btnAccept.setBackgroundTintList(ColorStateList.valueOf(dynamicAccentColorDefault));
        btnCancel.setBackgroundTintList(ColorStateList.valueOf(dynamicAccentColorDefault));

        // Configurar el clic del botón Aceptar
        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("PermissionDebug", "btnAccept clicked");
                // Solicitar permisos de notificación
                requestNotificationPermission();
                alertDialog.dismiss();
            }
        });

        // Configurar el clic del botón Cancelar
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        // Mostrar el diálogo
        alertDialog = builder.show();
    }

    // Método para obtener el color dinámico del acento predeterminado en Android 12 o superior
    @RequiresApi(api = Build.VERSION_CODES.S)
    private int getColorDynamicAccentDefault() {
        int dynamicAccentColorDefault = ColorUtils.blendARGB(
                ContextCompat.getColor(context, android.R.color.system_accent1_100),
                ContextCompat.getColor(context, android.R.color.system_neutral1_500),
                0.0f // Factor de mezcla (0.0 a 1.0)
        );

        return dynamicAccentColorDefault;
    }

    // Método para obtener el color dinámico del acento en Android 12 o superior
    @RequiresApi(api = Build.VERSION_CODES.S)
    private int getColorDynamicAccent() {
        int dynamicAccentColor = ColorUtils.blendARGB(
                ContextCompat.getColor(context, android.R.color.system_accent1_300),
                ContextCompat.getColor(context, android.R.color.system_neutral1_600),
                0.1f // Factor de mezcla (0.0 a 1.0)
        );

        return dynamicAccentColor;
    }
}