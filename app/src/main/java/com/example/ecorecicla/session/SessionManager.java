package com.example.ecorecicla.session;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    // Constantes para las claves de SharedPreferences
    private static final String PREF_NAME = "MyAppPrefs";
    private static final String KEY_REMEMBER_SESSION = "rememberSession";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_CURRENT_USER_ID = "currentUserId";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;

    // Constructor de la clase
    public SessionManager(Context context) {
        this.context = context;
        // Inicializa SharedPreferences y su editor
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    // Método para establecer el estado de "recordar sesión"
    public void setRememberSession(boolean rememberSession) {
        editor.putBoolean(KEY_REMEMBER_SESSION, rememberSession);
        editor.apply();
    }

    // Método para obtener el estado de "recordar sesión"
    public boolean getRememberSession() {
        // Devuelve el valor asociado con KEY_REMEMBER_SESSION, si no existe devuelve false
        return sharedPreferences.getBoolean(KEY_REMEMBER_SESSION, false);
    }

    // Método para establecer el estado de inicio de sesión
    public void setLoggedIn(boolean isLoggedIn) {
        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn);
        editor.apply();
    }

    // Método para verificar si el usuario ha iniciado sesión
    public boolean isLoggedIn() {
        // Devuelve el valor asociado con KEY_IS_LOGGED_IN, si no existe devuelve false
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public void setCurrentUserId(int userId) {
        editor.putInt(KEY_CURRENT_USER_ID, userId);
        editor.apply();
    }

    public int getCurrentUserId() {
        return sharedPreferences.getInt(KEY_CURRENT_USER_ID, -1);
    }

    // Método para limpiar toda la información de la sesión
    public void clearSession() {
        // Elimina todas las preferencias almacenadas en SharedPreferences
        editor.clear();
        editor.apply();
    }
}