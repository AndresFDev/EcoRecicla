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

import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import com.example.ecorecicla.activities.MainActivity;
import com.example.ecorecicla.helpers.NotificationWorker;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Calendar;
import java.util.Objects;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class EcoRecicla extends Application {
    private static EcoRecicla instance;
    private static final String CHANNEL_ID = "EcoReciclaChannel";
    private static final int NOTIFICATION_ID = 123;
    private BottomNavigationView menuView;

    @Override
    public void onCreate() {
        super.onCreate();

        // Obtiene el modo de tema guardado y lo aplica
        int themeMode = getThemeMode();
        AppCompatDelegate.setDefaultNightMode(themeMode);

        // Programa la notificación semanal
        scheduleWeeklyNotificationWithWorkManager();
    }

    public static EcoRecicla getInstance() {
        return instance;
    }

    private void scheduleWeeklyNotificationWithWorkManager() {
        // Crea una tarea periódica con WorkManager
        PeriodicWorkRequest notificationWork = new PeriodicWorkRequest.Builder(
                NotificationWorker.class,
                7, TimeUnit.DAYS) // Programa la tarea cada 7 días
                .setInitialDelay(calculateDelayToNextMonday(), TimeUnit.MILLISECONDS)
                .build();

        WorkManager.getInstance(this).enqueue(notificationWork);
    }

    private long calculateDelayToNextMonday() {
        // Obtiene el calendario actual
        Calendar currentDate = Calendar.getInstance();

        // Configura el calendario para el próximo lunes a las 9:00 AM
        Calendar nextMonday = Calendar.getInstance();
        nextMonday.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        nextMonday.set(Calendar.HOUR_OF_DAY, 9);
        nextMonday.set(Calendar.MINUTE, 0);
        nextMonday.set(Calendar.SECOND, 0);

        // Si el próximo lunes ya pasó esta semana, avanza al próximo lunes
        if (currentDate.after(nextMonday)) {
            nextMonday.add(Calendar.WEEK_OF_YEAR, 1);
        }

        // Calcula la diferencia en milisegundos entre el momento actual y el próximo lunes
        return nextMonday.getTimeInMillis() - currentDate.getTimeInMillis();
    }


    // Getter para la vista del menú
    public BottomNavigationView getMenuView() {
        return menuView;
    }

    // Setter para la vista del menú y configuración de la insignia
    public void setMenuView(BottomNavigationView menuView) {
        this.menuView = menuView;
        setupBadge();
    }

    // Obtiene el modo de tema guardado
    public int getThemeMode() {
        SharedPreferences sharedPreferences = getSharedPreferences("app_preferences", MODE_PRIVATE);
        return sharedPreferences.getInt("theme_mode", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
    }

    // Configura el tema de la aplicación y recrea la actividad actual
    public void setAppTheme(Activity activity, int themeMode) {
        AppCompatDelegate.setDefaultNightMode(themeMode);

        SharedPreferences sharedPreferences = getSharedPreferences("app_preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("theme_mode", themeMode);
        editor.apply();

        activity.recreate();
    }

    // Guarda el tiempo de la última notificación diaria
    public void setLastDailyNotificationTime(long time) {
        SharedPreferences sharedPreferences = getSharedPreferences("app_preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong("last_daily_notification_time", time);
        editor.apply();
    }

    // Comprueba si ha pasado un día desde la última notificación
    private boolean isNextDay(Calendar lastDate, Calendar currentDate) {
        return lastDate.get(Calendar.DAY_OF_YEAR) != currentDate.get(Calendar.DAY_OF_YEAR)
                || lastDate.get(Calendar.YEAR) != currentDate.get(Calendar.YEAR);
    }

    // Muestra una notificación
    public static void showNotification(Context context) {
        // Crea una intención para abrir la aplicación en una pantalla específica
        Intent resultIntent = new Intent(context, MainActivity.class);
        resultIntent.putExtra("fragmentToLoad", "TipsFragment");

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntent(resultIntent);

        PendingIntent resultPendingIntent;

        // Verifica la versión de Android y configura la intención
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            resultPendingIntent = PendingIntent.getActivity(
                    context,
                    0,
                    resultIntent,
                    PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_IMMUTABLE
            );
        } else {
            resultPendingIntent = PendingIntent.getActivity(
                    context,
                    0,
                    resultIntent,
                    PendingIntent.FLAG_CANCEL_CURRENT
            );
        }

        // Crea y muestra la notificación
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

    // Configura la insignia del menú
    public void setupBadge() {
        if (menuView != null) {
            BadgeDrawable badge = menuView.getOrCreateBadge(R.id.tips);
            badge.setBackgroundColor(getResources().getColor(R.color.colorError));
            badge.setHorizontalOffset(-5);
            badge.setVerticalOffset(-5);

            // Verifica si hay una notificación diaria pendiente
            boolean hasDailyNotification = isNextDay(Calendar.getInstance(), Calendar.getInstance());
            badge.setVisible(hasDailyNotification);
        }
    }
}