package com.example.ecorecicla;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // Lógica para mostrar la notificación aquí (puedes utilizar un Snackbar, Toast o una notificación del sistema)
        Toast.makeText(context, "¡Nuevos tips disponibles!", Toast.LENGTH_SHORT).show();
    }
}