package com.example.ecorecicla.fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ecorecicla.R;
import com.example.ecorecicla.UserData;
import com.example.ecorecicla.activities.MainActivity;
import com.example.ecorecicla.models.User;
import com.example.ecorecicla.utils.ImageCompressionUtil;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.materialswitch.MaterialSwitch;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Map;

public class EditProfileFragment extends Fragment {
    private ImageView ivImage;
    private Uri selectedImageUri;
    private TextInputLayout tilUserName, tilEmail, tilPassword, tilNewPassword, tilConfPassword;
    private TextInputEditText tiUserName, tiEmail, tiPassword, tiNewPassword, tiConfPassword;
    private MaterialSwitch switchPass;
    private MaterialButton btnBack, btnImage, btnSave;
    private FrameLayout flSave;
    private static final String PERMISSION_READ_MEDIA_IMAGES = "android.permission.READ_MEDIA_IMAGES";
    private static final String PERMISSION_READ_MEDIA_IMAGES_API27 = "android.permission.READ_EXTERNAL_STORAGE";
    private UserData userData;

    private final ActivityResultLauncher<String[]> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), this::handlePermissionResult);

    private final ActivityResultLauncher<Intent> pickImageLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), this::handleImageSelectionResult);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        initializeViews(rootView);
        setupListeners();
        populateUserData();
        return rootView;
    }

    private void initializeViews(View rootView) {
        btnBack = rootView.findViewById(R.id.btnBack);
        ivImage = rootView.findViewById(R.id.ivImage);
        btnImage = rootView.findViewById(R.id.btnImage);
        tilUserName = rootView.findViewById(R.id.tilUserName);
        tiUserName = rootView.findViewById(R.id.tiUserName);
        tilEmail = rootView.findViewById(R.id.tilEmail);
        tiEmail = rootView.findViewById(R.id.tiEmail);
        switchPass = rootView.findViewById(R.id.switchPass);
        tiPassword = rootView.findViewById(R.id.tiPassword);
        tilPassword = rootView.findViewById(R.id.tilPassword);
        tilNewPassword = rootView.findViewById(R.id.tilNewPassword);
        tiNewPassword = rootView.findViewById(R.id.tiNewPassword);
        tiConfPassword = rootView.findViewById(R.id.tiConfPassword);
        tilConfPassword = rootView.findViewById(R.id.tilConfPassword);
        btnSave = rootView.findViewById(R.id.btnSave);
        flSave = rootView.findViewById(R.id.flSave);
        userData = new UserData(getContext());
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> navigateToProfile());
        btnImage.setOnClickListener(v -> openImagePicker());

        switchPass.setOnCheckedChangeListener((buttonView, isChecked) -> {
            tilPassword.setVisibility(isChecked ? View.VISIBLE : View.GONE);
            tilNewPassword.setVisibility(isChecked ? View.VISIBLE : View.GONE);
            tilConfPassword.setVisibility(isChecked ? View.VISIBLE : View.GONE);
        });

        btnSave.setOnClickListener(v -> saveChanges());
    }

    private void populateUserData() {
        User user = userData.getCurrentUser();
        if (user != null) {
            if (!TextUtils.isEmpty(user.getUserImage())) {
                loadUserImage(user.getUserImage(), ivImage);
            }
            tiUserName.setText(user.getUserName());
            tiEmail.setText(user.getEmail());
        }
    }

    private void requestPermissions() {
        String[] permissions = Build.VERSION.SDK_INT >= Build.VERSION_CODES.R
                ? new String[]{PERMISSION_READ_MEDIA_IMAGES}
                : new String[]{PERMISSION_READ_MEDIA_IMAGES_API27};
        requestPermissionLauncher.launch(permissions);
    }

    public void openImagePicker() {
        requestPermissions();
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickImageLauncher.launch(intent);
    }

    private void loadUserImage(String imageUrl, ImageView imageView) {
        Glide.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.ic_person)
                .error(R.drawable.ic_close)
                .into(imageView);
    }

    private void saveChanges() {
        String userImage = selectedImageUri != null
                ? ImageCompressionUtil.compressAndConvertToWebP(getContext(), selectedImageUri)
                : userData.getCurrentUser().getUserImage();

        User currentUser = userData.getCurrentUser();
        String userName = tiUserName.getText().toString();
        String email = tiEmail.getText().toString();
        String currentPassword = currentUser.getPassword();
        User editedUser = new User(userImage, userName, email, currentPassword);

        editedUser.setId(currentUser.getId());
        editedUser.setUserImage(userImage);
        editedUser.setUserName(userName);
        editedUser.setEmail(email);
        editedUser.setPassword(currentPassword);

        boolean validatePassword = switchPass.isChecked();
        String password = tiPassword.getText().toString();
        String newPassword = tiNewPassword.getText().toString();
        String confPassword = tiConfPassword.getText().toString();

        if (!newPassword.isEmpty()) {
            editedUser.setPassword(newPassword);
        }

        if (hasUserChanged(currentUser, editedUser, validatePassword, password, newPassword, confPassword)) {
            handleEditResult(validatePassword, editedUser, userName, email, password, newPassword, confPassword);
        } else {
            Toast.makeText(getContext(), "No se realizaron cambios", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean hasUserChanged(User currentUser, User editedUser, boolean validatePassword, String password, String newPassword, String confPassword) {
        if (validatePassword) {
            return !currentUser.getUserName().equals(editedUser.getUserName()) ||
                    !currentUser.getEmail().equals(editedUser.getEmail()) ||
                    !currentUser.getPassword().equals(password) ||
                    !newPassword.isEmpty() || !confPassword.equals(newPassword);
        } else {
            return !currentUser.getUserImage().equals(editedUser.getUserImage()) ||
                    !currentUser.getUserName().equals(editedUser.getUserName()) ||
                    !currentUser.getEmail().equals(editedUser.getEmail());
        }
    }

    private void handleEditResult(boolean validatePassword, User editedUser, String userName, String email, String currentPassword, String newPassword, String confirmPassword) {
        String errorMessage = "";
        int editResult;

        if (validatePassword) {
            editResult = userData.editUser(editedUser, userName, email, currentPassword, newPassword, confirmPassword, true);
        } else {
            editResult = userData.editUser(editedUser, userName, email, "", "", "", false);
        }

        handleEditResultMessage(editResult);
    }

    private void handleEditResultMessage(int editResult) {
        setupTextWatchers();
        String errorMessage = "";
        switch (editResult) {
            case UserData.EDIT_SUCCESS:
                navigateToMainActivity();
                break;
            case UserData.EDIT_USER_NOT_FOUND:
                Toast.makeText(getContext(), "Usuario no encontrado para editar", Toast.LENGTH_SHORT).show();
                break;
            case UserData.FIELDS_EMPTY:
                errorMessage = "Campos obligatorios no proporcionados";
                showError(tilUserName, errorMessage);
                showError(tilEmail, errorMessage);
                break;
            case UserData.USERNAME_ALREADY_EXISTS:
                errorMessage = "Usuario ya está en uso";
                showError(tilUserName, errorMessage);
                break;
            case UserData.INCORRECT_CURRENT_PASSWORD:
                errorMessage = "Contraseña no coincide con la actual";
                showError(tilPassword, errorMessage);
                break;
            case UserData.EMPTY_PASSWORD_EDIT:
                errorMessage = "Campos obligatorios no proporcionados";
                showError(tilNewPassword, errorMessage);
                showError(tilConfPassword, errorMessage);
                break;
            case UserData.EMPTY_NEW_PASSWORD_EDIT:
                errorMessage = "Campos obligatorios no proporcionados";
                showError(tilNewPassword, errorMessage);
                break;
            case UserData.INVALID_PASSWORD_FORMAT:
                errorMessage = "La contraseña debe ser mayor de 8 caracteres y contener una letra";
                showError(tilNewPassword, errorMessage);
                break;
            case UserData.EMPTY_CONFIRM_PASSWORD_EDIT:
                errorMessage = "Confirma la nueva contraseña";
                showError(tilConfPassword, errorMessage);
                break;
            case UserData.NEW_PASSWORDS_DO_NOT_MATCH:
                errorMessage = "Contraseñas no coinciden";
                showError(tilNewPassword, errorMessage);
                showError(tilConfPassword, errorMessage);
                break;
        }

        if (!errorMessage.isEmpty()) {
            Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
        }
    }

    private void showError(TextInputLayout textInputLayout, String errorMessage) {
        textInputLayout.setError(errorMessage);
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.putExtra("FRAGMENT_TO_SHOW", "FragmentProfile");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                flSave.setVisibility(View.VISIBLE);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(intent);
                        flSave.setVisibility(View.GONE);
                        requireActivity().finish();
                    }
                }, 4000);
            }
        }, 0);
    }

    private void navigateToProfile() {
        ProfileFragment profileFragment = new ProfileFragment();
        FragmentTransaction fragmentTransaction = requireActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.nav_fragment_activity_main, profileFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void handlePermissionResult(Map<String, Boolean> result) {
        for (Map.Entry<String, Boolean> entry : result.entrySet()) {
            String permission = entry.getKey();
            Boolean granted = entry.getValue();
        }
    }

    private void handleImageSelectionResult(ActivityResult result) {
        if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
            Intent data = result.getData();
            handleImageSelection(data);
        }
    }

    private void handleImageSelection(Intent data) {
        if (data == null) {
            return;
        }

        selectedImageUri = data.getData();

        if (selectedImageUri != null) {
            ivImage.setImageURI(selectedImageUri);
        }
    }

    private void setupTextWatchers() {
        addTextWatcher(tilUserName);
        addTextWatcher(tilEmail);
        addTextWatcher(tilPassword);
        addTextWatcher(tilNewPassword);
        addTextWatcher(tilConfPassword);
    }

    private void addTextWatcher(TextInputLayout textInputLayout) {
        textInputLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // No action needed before text changes
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Clear the error when the user starts editing the password
                textInputLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // No action needed after text changes
            }
        });
    }
}