package com.example.ecorecicla;

import android.content.Context;
import android.util.Log;

import com.example.ecorecicla.models.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserData {
    private static final String FILENAME = "userData.json";
    private static final int INVALID_ID = -1;
    private Context context;

    public UserData(Context context) {
        this.context = context;
    }

    // Constantes para códigos de retorno
    public static final int SUCCESS = 0;
    public static final int USER_ALREADY_EXISTS = 1;
    public static final int EMAIL_ALREADY_EXISTS = 2;
    public static final int PASSWORDS_DO_NOT_MATCH = 3;
    public static final int EMPTY_USERNAME = 4;
    public static final int EMPTY_EMAIL = 5;
    public static final int INVALID_EMAIL_FORMAT = 6;
    public static final int EMPTY_PASSWORD = 7;
    public static final int INVALID_PASSWORD_FORMAT = 8;
    public static final int EMPTY_CONFIRM_PASSWORD = 9;
    public static final int MISSING_FIELDS = 10;
    public static final int CHECKBOX_NOT_SELECTED = 11;
    public static final int USER_NOT_FOUND = 12;
    public static final int INCORRECT_PASSWORD = 13;
    public static final int BOTH_FIELDS_EMPTY = 14;
    public static final int EMPTY_CREDENTIALS_LOGIN = 15;
    public static final int EMPTY_PASSWORD_LOGIN = 16;
    public static final int FIELDS_EMPTY = 18;
    public static final int USERNAME_ALREADY_EXISTS = 19;
    public static final int USEREMAIL_ALREADY_EXISTS = 20;
    public static final int EMPTY_PASSWORD_EDIT = 21;
    public static final int EMPTY_NEW_PASSWORD_EDIT = 22;
    public static final int EMPTY_CONFIRM_PASSWORD_EDIT = 23;
    public static final int INCORRECT_CURRENT_PASSWORD = 24;
    public static final int NEW_PASSWORDS_DO_NOT_MATCH = 25;
    public static final int EDIT_SUCCESS = 26;
    public static final int EDIT_USER_NOT_FOUND = 27;

    // Método para registrar un nuevo usuario
    public int registerUser(User user, boolean isCheckboxChecked, String confirmPassword) {
        List<User> userList = loadUserList();

        if (userList == null) {
            userList = new ArrayList<>();
        }

        // Validaciones de campos
        if (areFieldsEmpty(user, confirmPassword)) {
            return MISSING_FIELDS;
        }

        if (user.getUserName().isEmpty()) {
            return EMPTY_USERNAME;
        }

        if (isUserAlreadyExists(userList, user.getUserName())) {
            return USER_ALREADY_EXISTS;
        }

        if (user.getEmail().isEmpty()) {
            return EMPTY_EMAIL;
        }

        if (!isValidEmail(user.getEmail())) {
            return INVALID_EMAIL_FORMAT;
        }

        if (isEmailAlreadyExists(userList, user.getEmail())) {
            return EMAIL_ALREADY_EXISTS;
        }

        if (user.getPassword().isEmpty()) {
            return EMPTY_PASSWORD;
        }

        if (!isValidPassword(user.getPassword())) {
            return INVALID_PASSWORD_FORMAT;
        }

        if (confirmPassword.isEmpty()) {
            return EMPTY_CONFIRM_PASSWORD;
        }

        if (!user.getPassword().equals(confirmPassword)) {
            return PASSWORDS_DO_NOT_MATCH;
        }

        if (!isCheckboxChecked) {
            return CHECKBOX_NOT_SELECTED;
        }

        // Generar un nuevo ID para el usuario
        int newId = generateNewUserId(userList);
        user.setId(newId);

        userList.add(user);
        saveUserList(userList);
        return SUCCESS;
    }

    // Método para cargar la lista de usuarios desde el archivo JSON
    /*public List<User> loadUserList() {
        List<User> userList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(context.getFileStreamPath(FILENAME)))) {
            StringBuilder jsonBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line);
            }
            Type userListType = new TypeToken<List<User>>() {}.getType();
            userList = new Gson().fromJson(jsonBuilder.toString(), userListType);
            Log.d("UserData", "Usuarios cargados correctamente desde userData.json");

        } catch (IOException e) {
            Log.e("UserData", "Error al cargar usuarios: " + e.getMessage());
        }
        return userList;
    } */

    public List<User> loadUserList() {
        List<User> userList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(context.getFileStreamPath(FILENAME)))) {
            Gson gson = new Gson();
            userList = Arrays.asList(gson.fromJson(reader, User[].class));
            Log.d("UserData", "Usuarios cargados correctamente desde userData.json");
        } catch (IOException e) {
            Log.e("UserData", "Error al cargar usuarios: " + e.getMessage());
        }
        return userList;
    }


    // Método para guardar la lista de usuarios en el archivo JSON
    /*private void saveUserList(List<User> userList) {
        try (FileOutputStream outputStream = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
             OutputStreamWriter writer = new OutputStreamWriter(outputStream)) {
            String userListJson = new Gson().toJson(userList);
            writer.write(userListJson);
            Log.d("UserData", "Usuarios guardados correctamente en userData.json");
        } catch (IOException e) {
            Log.e("UserData", "Error al guardar usuarios: " + e.getMessage());
        }
    } */

    private void saveUserList(List<User> userList) {
        try (FileOutputStream outputStream = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
             OutputStreamWriter writer = new OutputStreamWriter(outputStream)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String userListJson = gson.toJson(userList);
            writer.write(userListJson);
            Log.d("UserData", "Usuarios guardados correctamente en userData.json");
        } catch (IOException e) {
            Log.e("UserData", "Error al guardar usuarios: " + e.getMessage());
        }
    }


    // Método para obtener el usuario actualmente logueado
    public User getCurrentUser() {
        List<User> userList = loadUserList();

        if (!userList.isEmpty()) {
            return userList.get(userList.size() - 1);
        } else {
            return null;
        }
    }

    // Método para iniciar sesión
    public int loginUser(String credential, String password) {
        if (areLoginFieldsEmpty(credential, password)) {
            return BOTH_FIELDS_EMPTY;
        }

        if (credential.isEmpty()) {
            return EMPTY_CREDENTIALS_LOGIN;
        }

        if (password.isEmpty()) {
            return EMPTY_PASSWORD_LOGIN;
        }

        List<User> userList = loadUserList();

        for (User user : userList) {
            if (isCredentialMatched(user, credential) && user.getPassword().equals(password)) {
                return SUCCESS;
            }
        }

        return USER_NOT_FOUND;
    }

    // Método para editar un usuario existente
    public int editUser(User editedUser, String username, String email, String password, String newPassword, String confirmPassword, boolean validatePassword) {
        List<User> userList = loadUserList();
        User currentUser = getCurrentUser();

        if (areEditFieldsEmpty(username, email)) {
            return FIELDS_EMPTY;
        }

        if (isUsernameAlreadyExists(userList, editedUser, username)) {
            return USERNAME_ALREADY_EXISTS;
        }

        if (isEmailAlreadyExists(userList, editedUser, email)) {
            return USEREMAIL_ALREADY_EXISTS;
        }

        if (validatePassword) {
            if (!currentUser.getPassword().equals(password)) {
                return INCORRECT_CURRENT_PASSWORD;
            }

            if (areEditPasswordFieldsEmpty(newPassword, confirmPassword)) {
                return EMPTY_PASSWORD_EDIT;
            }

            if (newPassword.isEmpty()) {
                return EMPTY_NEW_PASSWORD_EDIT;
            }

            if (!isValidPassword(newPassword)) {
                return INVALID_PASSWORD_FORMAT;
            }

            if (confirmPassword.isEmpty()) {
                return EMPTY_CONFIRM_PASSWORD_EDIT;
            }

            if (!newPassword.equals(confirmPassword)) {
                return NEW_PASSWORDS_DO_NOT_MATCH;
            }
        }

        if (editUserInList(userList, editedUser)) {
            saveUserList(userList);
            return EDIT_SUCCESS;
        } else {
            return EDIT_USER_NOT_FOUND;
        }
    }

    // Métodos de utilidad

    private boolean areFieldsEmpty(User user, String confirmPassword) {
        return user.getUserName().isEmpty() && user.getEmail().isEmpty() && user.getPassword().isEmpty() && confirmPassword.isEmpty();
    }

    private boolean areLoginFieldsEmpty(String credential, String password) {
        return credential.isEmpty() && password.isEmpty();
    }

    private boolean areEditFieldsEmpty(String username, String email) {
        return username.isEmpty() || email.isEmpty();
    }

    private boolean areEditPasswordFieldsEmpty(String newPassword, String confirmPassword) {
        return newPassword.isEmpty() && confirmPassword.isEmpty();
    }

    private boolean isValidEmail(String email) {
        return email.contains("@");
    }

    private boolean isValidPassword(String password) {
        String passwordRegex = context.getString(R.string.passRegex);
        return password.matches(passwordRegex);
    }

    private boolean isUserAlreadyExists(List<User> userList, String username) {
        for (User existingUser : userList) {
            if (existingUser.getUserName().equalsIgnoreCase(username)) {
                return true;
            }
        }
        return false;
    }

    private boolean isEmailAlreadyExists(List<User> userList, String email) {
        for (User existingUser : userList) {
            if (existingUser.getEmail().equalsIgnoreCase(email)) {
                return true;
            }
        }
        return false;
    }

    private boolean isUsernameAlreadyExists(List<User> userList, User editedUser, String username) {
        User currentUser = getCurrentUser();
        if (!currentUser.getUserName().equals(editedUser.getUserName())) {
            for (User existingUser : userList) {
                if (!existingUser.equals(editedUser) && existingUser.getUserName().equalsIgnoreCase(editedUser.getUserName())) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isEmailAlreadyExists(List<User> userList, User editedUser, String email) {
        User currentUser = getCurrentUser();
        if (!currentUser.getEmail().equals(editedUser.getEmail())) {
            for (User existingUser : userList) {
                if (!existingUser.equals(editedUser) && existingUser.getEmail().equalsIgnoreCase(editedUser.getEmail())) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isCredentialMatched(User user, String credential) {
        return user.getEmail().equalsIgnoreCase(credential) || user.getUserName().equalsIgnoreCase(credential);
    }

    private int generateNewUserId(List<User> userList) {
        return userList.isEmpty() ? 1 : userList.stream().mapToInt(User::getId).max().orElse(INVALID_ID) + 1;
    }

    private boolean editUserInList(List<User> userList, User editedUser) {
        for (int i = 0; i < userList.size(); i++) {
            User user = userList.get(i);
            if (user.getId() == editedUser.getId()) {
                userList.set(i, editedUser);
                return true;
            }
        }
        return false;
    }
}