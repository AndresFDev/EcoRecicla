package com.example.ecorecicla;

import android.app.Activity;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.ecorecicla.activities.MainActivity;
import com.example.ecorecicla.fragments.TipsFragment;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Calendar;
import java.util.Objects;
import java.util.TimeZone;

public class EcoApp extends Application {
    private static final String CHANNEL_ID = "EcoReciclaChannel";
    private static final int NOTIFICATION_ID = 123;
    private BottomNavigationView menuView;

    @Override
    public void onCreate() {
        super.onCreate();

        int themeMode = getThemeMode();
        AppCompatDelegate.setDefaultNightMode(themeMode);

        scheduleWeeklyNotification(menuView);
    }

    public BottomNavigationView getMenuView() {
        return menuView;
    }

    public void setMenuView(BottomNavigationView menuView) {
        this.menuView = menuView;
        setupBadge();
    }

    public int getThemeMode() {
        SharedPreferences sharedPreferences = getSharedPreferences("app_preferences", MODE_PRIVATE);
        return sharedPreferences.getInt("theme_mode", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
    }

    public void setAppTheme(Activity activity, int themeMode) {
        AppCompatDelegate.setDefaultNightMode(themeMode);

        SharedPreferences sharedPreferences = getSharedPreferences("app_preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("theme_mode", themeMode);
        editor.apply();

        activity.recreate();

    }

    private void scheduleWeeklyNotification(BottomNavigationView menuView) {
        Intent intent = new Intent(this, NotificationReceiver.class);
        PendingIntent pendingIntent;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            pendingIntent = PendingIntent.getBroadcast(
                    this,
                    0,
                    intent,
                    PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_MUTABLE
            );
        } else {
            // Versiones anteriores a Android 12
            pendingIntent = PendingIntent.getBroadcast(
                    this,
                    0,
                    intent,
                    PendingIntent.FLAG_CANCEL_CURRENT
            );
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "\"¡EcoTips Asombrosos te están esperando! \uD83C\uDF31";
            String description = "Descubre los secretos ecológicos más sorprendentes. ¡No te pierdas los nuevos EcoTips que acaban de llegar! \uD83D\uDE80✨";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            Objects.requireNonNull(notificationManager).createNotificationChannel(channel);
        }

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT-5:00"));
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY); // Establece el día de la semana como lunes
        calendar.set(Calendar.HOUR_OF_DAY, 9); // Ajusta la hora según tus preferencias
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        long lastNotificationTime = getLastNotificationTime();
        Calendar lastNotificationCalendar = Calendar.getInstance();
        lastNotificationCalendar.setTimeInMillis(lastNotificationTime);

        if (isNextWeek(lastNotificationCalendar, calendar)) {
            // Si ha pasado una semana, programa la nueva notificación

            // ... (resto de tu código para programar la notificación)

            // Actualiza la última fecha de visualización de la notificación
            setLastNotificationTime(System.currentTimeMillis());
        } else {
            long lastDailyNotificationTime = getLastDailyNotificationTime();
            Calendar lastDailyNotificationCalendar = Calendar.getInstance();
            lastDailyNotificationCalendar.setTimeInMillis(lastDailyNotificationTime);

            if (isNextDay(lastDailyNotificationCalendar, Calendar.getInstance())) {
                // Si ha pasado una semana, programa la nueva notificación

                // ... (resto de tu código para programar la notificación)

                // Actualiza la última fecha de visualización de la notificación
                setLastDailyNotificationTime(System.currentTimeMillis());
            }
        }
    }

    private long getLastDailyNotificationTime() {
        SharedPreferences sharedPreferences = getSharedPreferences("app_preferences", MODE_PRIVATE);
        return sharedPreferences.getLong("last_daily_notification_time", 0);
    }

    private void setLastDailyNotificationTime(long time) {
        SharedPreferences sharedPreferences = getSharedPreferences("app_preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong("last_daily_notification_time", time);
        editor.apply();
    }

    private boolean isNextDay(Calendar lastDate, Calendar currentDate) {
        return lastDate.get(Calendar.DAY_OF_YEAR) != currentDate.get(Calendar.DAY_OF_YEAR)
                || lastDate.get(Calendar.YEAR) != currentDate.get(Calendar.YEAR);
    }

    private boolean isNextWeek(Calendar lastDate, Calendar currentDate) {
        int daysDifference = currentDate.get(Calendar.DAY_OF_YEAR) - lastDate.get(Calendar.DAY_OF_YEAR);
        int yearDifference = currentDate.get(Calendar.YEAR) - lastDate.get(Calendar.YEAR);
        return yearDifference == 0 && daysDifference >= 7;
    }

    private long getLastNotificationTime() {
        SharedPreferences sharedPreferences = getSharedPreferences("app_preferences", MODE_PRIVATE);
        return sharedPreferences.getLong("last_notification_time", 0);
    }

    private void setLastNotificationTime(long time) {
        SharedPreferences sharedPreferences = getSharedPreferences("app_preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong("last_notification_time", time);
        editor.apply();
    }

    public static void showNotification(Context context) {
        Intent resultIntent = new Intent(context, MainActivity.class);
        resultIntent.putExtra("fragmentToLoad", "TipsFragment"); // Indica que se debe abrir TipsFragment

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntent(resultIntent);

        PendingIntent resultPendingIntent;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            resultPendingIntent = PendingIntent.getActivity(
                    context,
                    0,
                    resultIntent,
                    PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_IMMUTABLE
            );
        } else {
            // Versiones anteriores a Android 12
            resultPendingIntent = PendingIntent.getActivity(
                    context,
                    0,
                    resultIntent,
                    PendingIntent.FLAG_CANCEL_CURRENT
            );
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_app)
                .setContentTitle("¡EcoTips Asombrosos te están esperando! \uD83C\uDF31")
                .setContentText("Descubre los secretos ecológicos más sorprendentes. ¡No te pierdas los nuevos EcoTips que acaban de llegar! \uD83D\uDE80✨")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(resultPendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    private void setupBadge() {
        if (menuView != null) {
            BadgeDrawable badge = menuView.getOrCreateBadge(R.id.tips);
            badge.setBackgroundColor(getResources().getColor(R.color.colorError));
            badge.setHorizontalOffset(-5);
            badge.setVerticalOffset(-5);
            badge.setVisible(true);
        }
    }
}
