package com.example.ecorecicla.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ecorecicla.helpers.NotificationPermissionHelper;
import com.example.ecorecicla.R;
import com.example.ecorecicla.UserData;
import com.example.ecorecicla.models.User;
import com.example.ecorecicla.utils.ImageCompressionUtil;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class SignUpActivity extends AppCompatActivity {

    private TextInputLayout tilUserName, tilEmail, tilPassword, tilConfPassword, tilCheckTerm;
    private TextInputEditText tiUserName, tiEmail, tiPassword, tiConfPassword;
    private MaterialCheckBox cbTerm;
    private ImageView ivImage;
    private MaterialButton btnBack, btnSignUp, btnLogin, btnImage;
    private LinearLayoutCompat llBtnSignup;
    private UserData userData;
    private Uri selectedImageUri;
    private NotificationPermissionHelper notificationPermissionHelper;
    private static final int REQUEST_PERMISSION = 123;
    private static final String PERMISSION_READ_MEDIA_IMAGES = "android.permission.READ_MEDIA_IMAGES";
    private static final String PERMISSION_READ_MEDIA_IMAGES_API27 = "android.permission.READ_EXTERNAL_STORAGE";

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    Toast.makeText(this, "Permiso concedido", Toast.LENGTH_SHORT).show();
                    btnImage.setEnabled(true);
                } else {
                    Toast.makeText(this, "Permiso denegado", Toast.LENGTH_SHORT).show();
                }
            });

    private final ActivityResultLauncher<Intent> pickImageLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Intent data = result.getData();
                    handleImageSelection(data);
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        checkMediaPermission();

        initializeViews();
        setupButtons();
        setupPermissions();
    }

    private void checkMediaPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (checkSelfPermission(PERMISSION_READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{PERMISSION_READ_MEDIA_IMAGES}, REQUEST_PERMISSION);
            }
        } else {
            if (checkSelfPermission(PERMISSION_READ_MEDIA_IMAGES_API27) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{PERMISSION_READ_MEDIA_IMAGES_API27}, REQUEST_PERMISSION);
            }
        }
    }

    private void initializeViews() {
        tilUserName = findViewById(R.id.tilUserName);
        tilEmail = findViewById(R.id.tilEmail);
        tilPassword = findViewById(R.id.tilPassword);
        tilConfPassword = findViewById(R.id.tilConfPassword);
        tilCheckTerm = findViewById(R.id.tilCheckTerm);

        llBtnSignup = findViewById(R.id.llBtnSignup);
        llBtnSignup.setElevation(4f);

        btnBack = findViewById(R.id.btnBack);
        btnLogin = findViewById(R.id.btnLogin);
        btnSignUp = findViewById(R.id.btnSignUp);
        btnImage = findViewById(R.id.btnImage);
        tiUserName = findViewById(R.id.tiUserName);
        tiEmail = findViewById(R.id.tiEmail);
        tiPassword = findViewById(R.id.tiPassword);
        tiConfPassword = findViewById(R.id.tiConfPassword);
        cbTerm = findViewById(R.id.cbTerm);

        ivImage = findViewById(R.id.ivImage);
    }

    private void setupButtons() {
        btnBack.setOnClickListener(v -> finish());
        btnImage.setOnClickListener(v -> openImagePicker());
        btnSignUp.setOnClickListener(v -> signUp());
        cbTerm.setOnClickListener(v -> showTermsDialog());
        btnLogin.setOnClickListener(v -> startLoginActivity());
    }

    public void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickImageLauncher.launch(intent);
    }

    private void setupPermissions() {
        notificationPermissionHelper = new NotificationPermissionHelper(this, requestPermissionLauncher);
    }

    private void signUp() {
        userData = new UserData(this);
        String userImage = "";
        String userName = tiUserName.getText().toString().toLowerCase();
        String email = tiEmail.getText().toString().toLowerCase();
        String password = tiPassword.getText().toString();
        String confirmPassword = tiConfPassword.getText().toString();

        if (selectedImageUri != null) {
            userImage = ImageCompressionUtil.compressAndConvertToWebP(this, selectedImageUri);
        }

        User newUser = new User(userImage, userName, email, password);

        boolean check = cbTerm.isChecked();
        int registrationResult = userData.registerUser(newUser, check, confirmPassword);
        handleSignUpResult(registrationResult);
    }

    private void showTermsDialog() {
        if (cbTerm.isChecked()) {
            new MaterialAlertDialogBuilder(this, R.style.ThemeOverlay_App_MaterialAlertDialog)
                    .setTitle(getResources().getString(R.string.termsCond))
                    .setMessage(getResources().getString(R.string.bodyTermyCon))
                    .setPositiveButton(getResources().getString(R.string.close), (dialog, which) -> notificationPermissionHelper.checkNotificationPermission())
                    .show();
        }
    }

    private void startLoginActivity() {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            ivImage.setImageURI(selectedImageUri);
        }
    }

    private void handleImageSelection(Intent data) {
        ivImage = findViewById(R.id.ivImage);

        if (data == null) {
            // Si no hay datos, la selección de imagen no fue exitosa
            return;
        }

        selectedImageUri = data.getData();

        if (selectedImageUri != null) {
            ivImage.setImageURI(selectedImageUri);
        }
    }

    private void handleSignUpResult(int registrationResult) {
        setupTextWatchers();
        String errorMessage = "";
        switch (registrationResult) {
            case UserData.SUCCESS:
                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
            case UserData.USER_ALREADY_EXISTS:
                errorMessage = "Usuario ya está en uso";
                showError(tilUserName, errorMessage);
                break;
            case UserData.EMAIL_ALREADY_EXISTS:
                errorMessage = "Correo electrónico ya está en uso";
                showError(tilEmail, errorMessage);
                break;
            case UserData.PASSWORDS_DO_NOT_MATCH:
                errorMessage = "Las contraseñas no coinciden";
                showError(tilPassword, errorMessage);
                break;
            case UserData.EMPTY_USERNAME:
                errorMessage = "Usuario no proporcionado";
                showError(tilUserName, errorMessage);
                break;
            case UserData.EMPTY_EMAIL:
                errorMessage = "Correo electrónico no proporcionado";
                showError(tilEmail, errorMessage);
                break;
            case UserData.INVALID_EMAIL_FORMAT:
                errorMessage = "Correo electrónico invalido";
                showError(tilEmail, errorMessage);
                break;
            case UserData.EMPTY_PASSWORD:
                errorMessage = "Contraseña no proporcionada";
                showError(tilPassword, errorMessage);
                break;
            case UserData.INVALID_PASSWORD_FORMAT:
                errorMessage = "La contraseña debe ser mayor de 8 caracteres y contener una letra";
                showError(tilPassword, errorMessage);
                break;
            case UserData.EMPTY_CONFIRM_PASSWORD:
                errorMessage = "Confirma tu contraseña";
                showError(tilConfPassword, errorMessage);
                break;
            case UserData.CHECKBOX_NOT_SELECTED:
                errorMessage = "Confirma términos y condiciones";
                showError(tilCheckTerm, errorMessage);
                break;
            case UserData.MISSING_FIELDS:
                errorMessage = "Campos obligatorios no proporcionados";
                showError(tilUserName, errorMessage);
                showError(tilEmail, errorMessage);
                showError(tilPassword, errorMessage);
                showError(tilConfPassword, errorMessage);
                showError(tilCheckTerm, errorMessage);
                break;
        }

        if (!errorMessage.isEmpty()) {
            Toast.makeText(SignUpActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
        }
    }

    private void showError(TextInputLayout textInputLayout, String errorMessage) {
        textInputLayout.setError(errorMessage);
    }

    private void setupTextWatchers() {
        addTextWatcher(tilUserName);
        addTextWatcher(tilEmail);
        addTextWatcher(tilPassword);
        addTextWatcher(tilConfPassword);
        setupCheckboxListener();
    }

    private void addTextWatcher(TextInputLayout textInputLayout) {
        textInputLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // No es necesario realizar ninguna acción antes de que cambie el texto
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Limpiar el error cuando el usuario comienza a editar la contraseña
                textInputLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // No es necesario realizar ninguna acción después de que cambie el texto
            }
        });
    }

    private void setupCheckboxListener() {
        cbTerm.setOnCheckedChangeListener((buttonView, isChecked) -> tilCheckTerm.setError(null));
    }
}