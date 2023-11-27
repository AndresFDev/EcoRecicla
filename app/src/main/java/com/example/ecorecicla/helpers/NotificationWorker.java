package com.example.ecorecicla.helpers;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.ecorecicla.EcoRecicla;

public class NotificationWorker extends Worker {

    public NotificationWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        // Lógica de notificación semanal
        EcoRecicla.showNotification(getApplicationContext());

        // Lógica de notificación diaria (si es necesario)
        if (shouldShowDailyNotification()) {
            EcoRecicla.showNotification(getApplicationContext());
            EcoRecicla.getInstance().setLastDailyNotificationTime(System.currentTimeMillis());
        }

        // Actualiza la insignia del menú
        EcoRecicla.getInstance().setupBadge();

        return Result.success();
    }

    private boolean shouldShowDailyNotification() {
        // Lógica para determinar si se debe mostrar la notificación diaria
        // Puedes ajustar esta lógica según tus necesidades
        return true;
    }
}

