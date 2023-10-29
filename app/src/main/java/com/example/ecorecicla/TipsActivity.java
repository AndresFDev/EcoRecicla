package com.example.ecorecicla;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.button.MaterialButton;

public class TipsActivity extends AppCompatActivity {
    private MaterialButton btnCategories, btnStats, btnTips, btnPlastic, btnPaper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tips);

        navButtons();
        managerButtons();

    }

    public void managerButtons() {
        btnPlastic = findViewById(R.id.btnPlastic);
        btnPaper = findViewById(R.id.btnPaper);

        btnPlastic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TipsDetailActivity.class);
                intent.putExtra("title", getString(R.string.plastic));
                startActivity(intent);
            }
        });

        btnPaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TipsDetailActivity.class);
                intent.putExtra("title", getString(R.string.paper));
                startActivity(intent);
            }
        });

    }

    public void navButtons() {

        btnCategories = findViewById(R.id.btnCategories);
        btnStats = findViewById(R.id.btnStats);
        btnTips = findViewById(R.id.btnTips);

        btnTips.setIconTint(getColorStateList(R.color.white));
        btnTips.setTextColor(getColorStateList(R.color.white));

        btnCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent categoriesIntent = new Intent(getApplicationContext(), CategoriesActivity.class);
                startActivity(categoriesIntent);
            }
        });

        btnStats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), StatsActivity.class);
                startActivity(intent);
            }
        });

    }
}