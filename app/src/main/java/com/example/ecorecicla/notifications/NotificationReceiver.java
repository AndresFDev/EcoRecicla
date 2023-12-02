package com.example.ecorecicla.notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "¡Nuevos tips disponibles!", Toast.LENGTH_SHORT).show();
    }
}