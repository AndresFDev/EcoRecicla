package com.example.ecorecicla.helpers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Build;

public class AlarmHelper {
    public static final long INTERVAL_DAY = 24 * 60 * 60 * 1000; // 24 horas en milisegundos

    public static void scheduleRepeatingExactAlarm(Context context, long triggerAtMillis, long intervalMillis, PendingIntent pendingIntent) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if (alarmManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
            } else {
                alarmManager.set(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
            }

            if (intervalMillis > 0) {
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, triggerAtMillis + intervalMillis, intervalMillis, pendingIntent);
            }
        }
    }
}