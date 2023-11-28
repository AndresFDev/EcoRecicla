package com.example.ecorecicla.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.example.ecorecicla.R;
import com.example.ecorecicla.SessionManager;
import com.example.ecorecicla.UserData;
import com.example.ecorecicla.models.User;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private UserData userData;
    private SessionManager sessionManager;
    private MaterialButton btnRecoveryPass, btnLogin, btnSignUp;
    private TextInputLayout tilEmail, tilPassword;
    private TextInputEditText tietEmail, tietPassword;
    private MaterialTextView tvTitle;
    private MaterialCheckBox cbRememberSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Inicializar instancias
        sessionManager = new SessionManager(this);
        userData = new UserData(this, sessionManager);

        // Verificar si hay una sesión activa y si se debe recordar
        if (sessionManager.isLoggedIn() && sessionManager.getRememberSession()) {
            navigateToMainActivity();
        }

        // Inicializar componentes de la interfaz de usuario
        initializeViews();

        // Configurar escuchadores para los botones
        setupButtonListeners();
    }

    // Inicializar vistas
    private void initializeViews() {
        tvTitle = findViewById(R.id.tvTitle);
        tilEmail = findViewById(R.id.tilEmail);
        tilPassword = findViewById(R.id.tilPassword);
        cbRememberSession = findViewById(R.id.cbRememberSesion);
        btnLogin = findViewById(R.id.btnLogin);
        btnSignUp = findViewById(R.id.btnSignUp);
        btnRecoveryPass = findViewById(R.id.btnRecoveryPass);
        tietEmail = findViewById(R.id.tietEmail);
        tietPassword = findViewById(R.id.tietPassword);
    }

    // Configurar escuchadores para los botones
    private void setupButtonListeners() {
        btnRecoveryPass.setOnClickListener(v -> navigateToRecoveryPass());
        btnSignUp.setOnClickListener(v -> navigateToSignUp());
        btnLogin.setOnClickListener(v -> handleLogin());
    }

    // Manejar el proceso de inicio de sesión
    private void handleLogin() {
        String credential = tietEmail.getText().toString().trim().toLowerCase();
        String password = tietPassword.getText().toString().trim();

        int loginResult = userData.loginUser(credential, password);
        handleLoginResult(loginResult);
    }

    // Navegar a la actividad principal
    private void navigateToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    // Navegar a la actividad de recuperación de contraseña
    private void navigateToRecoveryPass() {
        startActivity(new Intent(this, RecoveryPassActivity.class));
    }

    // Navegar a la actividad de registro
    private void navigateToSignUp() {
        startActivity(new Intent(this, SignUpActivity.class));
    }

    // Manejar el resultado del inicio de sesión
    private void handleLoginResult(int loginResult) {
        setupTextWatchers();
        String errorMessage = "";

        switch (loginResult) {
            case UserData.BOTH_FIELDS_EMPTY:
                errorMessage = "Campos obligatorios vacíos";
                showError(tilEmail, errorMessage);
                showError(tilPassword, errorMessage);
                break;
            case UserData.EMPTY_CREDENTIALS_LOGIN:
                errorMessage = "Nombre de usuario o correo electrónico vacío";
                showError(tilEmail, errorMessage);
                break;
            case UserData.USER_NOT_FOUND:
                errorMessage = "Usuario o correo electrónico no encontrado";
                showError(tilEmail, errorMessage);
                break;
            case UserData.EMPTY_PASSWORD_LOGIN:
                errorMessage = "La contraseña está vacía. Por favor, ingresa tu contraseña";
                showError(tilPassword, errorMessage);
                break;
            case UserData.INCORRECT_PASSWORD:
                errorMessage = "Contraseña incorrecta";
                showError(tilPassword, errorMessage);
                break;
            case UserData.SUCCESS:
                boolean rememberSession = cbRememberSession.isChecked();
                sessionManager.setRememberSession(rememberSession);
                sessionManager.setLoggedIn(rememberSession);
                navigateToMainActivity();
                break;
        }

        // Mostrar mensaje de error si es necesario
        if (!errorMessage.isEmpty()) {
            Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
        }
    }

    // Mostrar un error en el diseño de texto de entrada
    private void showError(TextInputLayout textInputLayout, String errorMessage) {
        textInputLayout.setError(errorMessage);
    }

    // Configurar los oyentes de texto para borrar los errores después de la edición
    private void setupTextWatchers() {
        addTextWatcher(tilEmail);
        addTextWatcher(tilPassword);
    }

    private void addTextWatcher(TextInputLayout textInputLayout) {
        textInputLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                textInputLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }
}