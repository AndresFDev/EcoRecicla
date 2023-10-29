package com.example.ecorecicla;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.button.MaterialButton;

public class CategoriesActivity extends AppCompatActivity {
    private MaterialButton btnPlastic, btnPaper, btnElectronics, btnGlass, btnCard,
            btnSteel, btnTexiles, btnBatteries, btnArtHome, btnCategories, btnStats, btnTips;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        navButtons();
        managerButtons();

    }

    public void managerButtons() {
        btnPlastic = findViewById(R.id.btnPlastic);
        btnPaper = findViewById(R.id.btnPaper);
        btnElectronics = findViewById(R.id.btnElectronics);
        btnGlass = findViewById(R.id.btnGlass);
        btnCard = findViewById(R.id.btnCard);
        btnSteel = findViewById(R.id.btnSteel);
        btnTexiles = findViewById(R.id.btnTexiles);
        btnBatteries = findViewById(R.id.btnBatteries);
        btnArtHome = findViewById(R.id.btnArtHome);

        btnPlastic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), EntryCategoryActivity.class);
                intent.putExtra("title", getString(R.string.plastic));
                startActivity(intent);
            }
        });

        btnPaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), EntryCategoryActivity.class);
                intent.putExtra("title", getString(R.string.paper));
                startActivity(intent);
            }
        });

        btnElectronics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), EntryCategoryActivity.class);
                intent.putExtra("title", getString(R.string.electronic));
                startActivity(intent);
            }
        });

        btnGlass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), EntryCategoryActivity.class);
                intent.putExtra("title", getString(R.string.glass));
                startActivity(intent);
            }
        });

        btnCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), EntryCategoryActivity.class);
                intent.putExtra("title", getString(R.string.cardboard));
                startActivity(intent);
            }
        });

        btnSteel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), EntryCategoryActivity.class);
                intent.putExtra("title", getString(R.string.steel));
                startActivity(intent);
            }
        });

        btnTexiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), EntryCategoryActivity.class);
                intent.putExtra("title", getString(R.string.textiles));
                startActivity(intent);
            }
        });

        btnBatteries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), EntryCategoryActivity.class);
                intent.putExtra("title", getString(R.string.batteries));
                startActivity(intent);
            }
        });

        btnArtHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), EntryCategoryActivity.class);
                intent.putExtra("title", getString(R.string.artHome));
                startActivity(intent);
            }
        });

    }

    public void navButtons() {

        btnCategories = findViewById(R.id.btnCategories);
        btnStats = findViewById(R.id.btnStats);
        btnTips = findViewById(R.id.btnTips);

        btnCategories.setIconTint(getColorStateList(R.color.white));
        btnCategories.setTextColor(getColorStateList(R.color.white));

        btnStats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent StatsIntent = new Intent(getApplicationContext(), StatsActivity.class);
                startActivity(StatsIntent);
            }
        });

        btnTips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TipsActivity.class);
                startActivity(intent);
            }
        });

    }

}