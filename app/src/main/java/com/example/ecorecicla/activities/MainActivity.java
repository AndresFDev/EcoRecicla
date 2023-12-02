package com.example.ecorecicla.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.ColorUtils;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;

import com.bumptech.glide.Glide;
import com.example.ecorecicla.EcoRecicla;
import com.example.ecorecicla.R;
import com.example.ecorecicla.session.SessionManager;
import com.example.ecorecicla.models.UserData;
import com.example.ecorecicla.fragments.CategoriesFragment;
import com.example.ecorecicla.fragments.ProfileFragment;
import com.example.ecorecicla.fragments.StatsFragment;
import com.example.ecorecicla.fragments.TipsFragment;
import com.example.ecorecicla.models.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationBarView;

import java.lang.reflect.Field;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView menuView;
    private UserData userData;
    private ImageView ivProfileIcon;
    private MaterialCardView cvIconImage;
    private ImageButton imageBtnMore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        SessionManager sessionManager = new SessionManager(this);
        userData = new UserData(this, sessionManager);

        menuView = findViewById(R.id.menuView);
        ivProfileIcon = findViewById(R.id.ivProfileIcon);
        cvIconImage = findViewById(R.id.cvIconImage);
        imageBtnMore = findViewById(R.id.imageBtnMore);

        customMenu();
        menuView.setOnItemSelectedListener(navItemSelectedListener);

        EcoRecicla ecoRecicla = (EcoRecicla) getApplication();

        ecoRecicla.setMenuView(menuView);
        setupMenuView();
        loadUserData();
        loadDefaultFragment(savedInstanceState);

        if (getIntent() != null && getIntent().hasExtra("FRAGMENT_TO_SHOW")) {
            String fragmentTag = getIntent().getStringExtra("FRAGMENT_TO_SHOW");

            if ("FragmentProfile".equals(fragmentTag)) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.nav_fragment_activity_main, new ProfileFragment())
                        .commit();

            }

            menuView.setSelectedItemId(R.id.profile);
        }


        String fragmentToLoad = getIntent().getStringExtra("fragmentToLoad");
        if ("TipsFragment" .equals(fragmentToLoad)) {
            loadTipsFragment();
        } else {
            // Cargar otro fragmento o realizar otras acciones según sea necesario
        }
    }

    private void customMenu() {
        if (isDarkModeEnabled()) {
            imageBtnMore.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorDarkOnPrimary)));
        } else {
            imageBtnMore.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorPrimary)));
        }
        imageBtnMore.setOnClickListener(v -> showPopupMenu(v));
    }

    private void showPopupMenu(View v) {
        PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.getMenuInflater().inflate(R.menu.config_menu, popupMenu.getMenu());

        try {
            Field fieldMPopup = PopupMenu.class.getDeclaredField("mPopup");
            fieldMPopup.setAccessible(true);
            Object mPopup = fieldMPopup.get(popupMenu);
            mPopup.getClass()
                    .getDeclaredMethod("setForceShowIcon", boolean.class)
                    .invoke(mPopup, true);
        } catch (Exception e) {
            Log.e("Main", "Error mostrando los iconos del menú.", e);
        }

        for (int i = 0; i < popupMenu.getMenu().size(); i++) {
            MenuItem item = popupMenu.getMenu().getItem(i);
            MenuItem item0 = popupMenu.getMenu().getItem(0);
            Drawable icon = item.getIcon();

            if (icon != null) {
                icon.setColorFilter(ContextCompat.getColor(this, getIconColor()), PorterDuff.Mode.SRC_ATOP);

            }

            if (isDarkModeEnabled()) {
                item0.setIcon(R.drawable.ic_night);
            }
        }

        popupMenu.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.themeItem) {
                showThemeDialog();
                return true;
            } else if (itemId == R.id.signOutItem) {
                SessionManager sessionManager = new SessionManager(this);
                sessionManager.clearSession();
                startActivity(new Intent(this, LoginActivity.class));
                return true;
            }
            return false;
        });

        popupMenu.show();
    }

    private int getIconColor() {
        return isDarkModeEnabled() ? R.color.colorDarkSecondary : R.color.colorPrimary;
    }

    private void showThemeDialog() {
        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(this, R.style.ThemeOverlay_App_MaterialAlertDialog);
        dialogBuilder.setTitle("Selecciona un tema")
                .setView(R.layout.toggle_button)
                .setPositiveButton(getResources().getString(R.string.close), (dialog, which) -> {

                });
        AlertDialog dialog = dialogBuilder.create();
        dialog.show();

        MaterialButtonToggleGroup toggleButton = dialog.findViewById(R.id.toggleButton);
        LinearLayout llProgressBar = dialog.findViewById(R.id.llProgressBar);

        if (llProgressBar != null) {

            if (toggleButton != null) {
                int themeMode = ((EcoRecicla) getApplication()).getThemeMode();
                toggleButton.check(getThemeButtonId(themeMode));

                toggleButton.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
                    if (isChecked) {
                        llProgressBar.setVisibility(View.VISIBLE);
                        int newThemeMode = getThemeModeFromButtonId(checkedId);
                        ((EcoRecicla) getApplication()).setAppTheme(MainActivity.this, newThemeMode);
                    } else {
                        llProgressBar.setVisibility(View.GONE);
                    }
                });
            }
        }
    }

    private int getThemeButtonId(int themeMode) {
        switch (themeMode) {
            case AppCompatDelegate.MODE_NIGHT_NO:
                return R.id.btnLight;
            case AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM:
                return R.id.btnSystem;
            case AppCompatDelegate.MODE_NIGHT_YES:
                return R.id.btnDark;
            default:
                return View.NO_ID;
        }
    }

    private int getThemeModeFromButtonId(int buttonId) {
        if (buttonId == R.id.btnLight) {
            return AppCompatDelegate.MODE_NIGHT_NO;
        } else if (buttonId == R.id.btnSystem) {
            return AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;
        } else if (buttonId == R.id.btnDark) {
            return AppCompatDelegate.MODE_NIGHT_YES;
        }
        return AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;
    }

    private void setupMenuView() {
        int activeIndicatorColor = getColor(isDarkModeEnabled() ? R.color.colorDarkSecondary : R.color.colorSecondary);
        int alpha = 142;
        int transparentColor = ColorUtils.setAlphaComponent(activeIndicatorColor, alpha);

        menuView.setItemActiveIndicatorColor(ColorStateList.valueOf(transparentColor));
        menuView.setItemIconTintList(ColorStateList.valueOf(getColor(R.color.white)));
        menuView.setItemTextColor(ColorStateList.valueOf(getColor(R.color.white)));
    }

    private boolean isDarkModeEnabled() {
        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        return currentNightMode == Configuration.UI_MODE_NIGHT_YES;
    }

    private void loadUserData() {
        User user = userData.getCurrentUser();

        if (user != null) {
            String userImage = user.getUserImage();
            loadUserImage(userImage, ivProfileIcon);
        } else {
            Log.e("UserData", "Failed to load user data from JSON");
        }
    }

    private void loadUserImage(String imageUrl, ImageView imageView) {
        if (!imageUrl.isEmpty()) {
            Glide.with(this)
                    .load(imageUrl)
                    .placeholder(R.drawable.ic_person)
                    .error(R.drawable.ic_close)
                    .into(imageView);
        } else {
            imageView.setImageResource(R.drawable.ic_person);
        }
    }

    private void loadDefaultFragment(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.nav_fragment_activity_main, new CategoriesFragment())
                    .commit();
        }
    }

    private void loadTipsFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.nav_fragment_activity_main, new TipsFragment())
                .commit();

        menuView.setSelectedItemId(R.id.tips);
    }

    private final NavigationBarView.OnItemSelectedListener navItemSelectedListener =
            new NavigationBarView.OnItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    int itemId = item.getItemId();
                    if (itemId == menuView.getSelectedItemId()) {
                        return false;
                    }

                    Fragment selectedFragment = null;
                    boolean showIndicator = true;

                    int activeIndicatorColor = getColor(isDarkModeEnabled() ? R.color.colorDarkSecondary : R.color.colorSecondary);
                    int alpha = 255;
                    int transparentColor = ColorUtils.setAlphaComponent(activeIndicatorColor, alpha);

                    if (itemId == R.id.categories || itemId == R.id.stats || itemId == R.id.tips) {
                        cvIconImage.setStrokeColor(Color.TRANSPARENT);
                        menuView.setItemActiveIndicatorColor(ColorStateList.valueOf(transparentColor));
                    } else if (itemId == R.id.profile) {
                        cvIconImage.setStrokeColor(ColorStateList.valueOf(transparentColor));
                        showIndicator = false;
                    }

                    if (showIndicator) {
                        menuView.setItemActiveIndicatorEnabled(true);
                    } else {
                        menuView.setItemActiveIndicatorEnabled(false);
                    }

                    if (itemId == R.id.categories) {
                        selectedFragment = new CategoriesFragment();
                    } else if (itemId == R.id.stats) {
                        selectedFragment = new StatsFragment();
                    } else if (itemId == R.id.tips) {
                        selectedFragment = new TipsFragment();
                        menuView.removeBadge(R.id.tips);
                    } else if (itemId == R.id.profile) {
                        selectedFragment = new ProfileFragment();
                        User user = userData.getCurrentUser();

                        Bundle args = new Bundle();
                        args.putInt("idUser", user.getId());
                        args.putString("userName", user.getUserName());
                        args.putString("userImage", user.getUserImage());
                        selectedFragment.setArguments(args);

                    }

                    if (selectedFragment != null) {
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.nav_fragment_activity_main, selectedFragment)
                                .commit();
                        return true;
                    }

                    return false;
                }
            };
}